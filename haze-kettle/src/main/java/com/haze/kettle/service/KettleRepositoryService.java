package com.haze.kettle.service;

import com.haze.core.service.AbstractBaseService;
import com.haze.kettle.config.KettleConfig;
import com.haze.kettle.dao.KettleRepositoryDao;
import com.haze.kettle.entity.KettleRepository;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.service.DatabaseDialectService;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class KettleRepositoryService extends AbstractBaseService<KettleRepository, Long> implements InitializingBean, DisposableBean {

    private KettleRepositoryDao kettleRepositoryDao;

    @Autowired
    private KettleConfig kettleConfig;

    private DatabaseDialectService databaseDialectService = new DatabaseDialectService(false);

    private Map<Long, KettleDatabaseRepository> repositoryCache = new ConcurrentHashMap<>();

    @Autowired
    public void setKettleRepositoryDao(KettleRepositoryDao kettleRepositoryDao) {
        this.kettleRepositoryDao = kettleRepositoryDao;
        super.setDao(kettleRepositoryDao);
    }

    @Transactional(readOnly = true)
    public List<IDatabaseType> getDatabaseTypes() {
        return databaseDialectService.getDatabaseTypes();
    }

    @Transactional(readOnly = true)
    public boolean isInit(Long id) throws Exception {
        KettleDatabaseRepository dbRepository;
        if (repositoryCache.containsKey(id)) {
            dbRepository = getKettleDatabaseRepository(id);
            String userTableName = dbRepository.getDatabaseMeta().quoteField(KettleDatabaseRepository.TABLE_R_USER);
            return dbRepository.connectionDelegate.getDatabase().checkTableExists(userTableName);
        } else {
            KettleRepository repository = this.findById(id);
            DatabaseMeta dataMeta = new DatabaseMeta(repository.getName(), repository.getDbType(), "Native(JDBC)", repository.getHost(), repository.getSchemaName(), repository.getPort(), repository.getUserName(), repository.getPassword());
            dataMeta.setUsingConnectionPool(false);
            KettleDatabaseRepositoryMeta repInfo = new KettleDatabaseRepositoryMeta();
            repInfo.setConnection(dataMeta);
            repInfo.setName(repository.getName());
            dbRepository = new KettleDatabaseRepository();
            dbRepository.init(repInfo);
            String userTableName = dbRepository.getDatabaseMeta().quoteField(KettleDatabaseRepository.TABLE_R_USER);
            try {
                dbRepository.connectionDelegate.connect(true, true);
                return dbRepository.connectionDelegate.getDatabase().checkTableExists(userTableName);
            } catch (KettleException e) {
                logger.error("执行检查失败, 失败原因:{}", e.getMessage());
                throw e;
            } finally {
                dbRepository.connectionDelegate.disconnect();
            }
        }

    }

    @Transactional(readOnly = true)
    public void createSchema(Long id) throws Exception {
        if (isInit(id)) {
            logger.warn("资源库【{}】表已存在", id);
        } else {
            logger.debug("开始创建资源库【{}】表", id);
            KettleRepository repository = this.findById(id);
            DatabaseMeta dataMeta = new DatabaseMeta(repository.getName(), repository.getDbType(), "Native(JDBC)", repository.getHost(), repository.getSchemaName(), repository.getPort(), repository.getUserName(), repository.getPassword());
            dataMeta.setUsingConnectionPool(false);
            KettleDatabaseRepositoryMeta repInfo = new KettleDatabaseRepositoryMeta();
            repInfo.setConnection(dataMeta);
            repInfo.setName(repository.getName());
            KettleDatabaseRepository dbRepository = new KettleDatabaseRepository();
            dbRepository.init(repInfo);
            dbRepository.create();
            logger.debug("资源库【{}】表创建成功", id);
        }
    }

    private KettleDatabaseRepository getKettleDatabaseRepository(Long id) throws KettleException {
        if (repositoryCache.containsKey(id)) {
            return repositoryCache.get(id);
        } else {
            KettleRepository repository = this.findById(id);
            DatabaseMeta dataMeta = new DatabaseMeta(repository.getName(), repository.getDbType(), "Native(JDBC)", repository.getHost(), repository.getSchemaName(), repository.getPort(), repository.getUserName(), repository.getPassword());
            dataMeta.setUsingConnectionPool(false);
            KettleDatabaseRepositoryMeta repInfo = new KettleDatabaseRepositoryMeta();
            repInfo.setConnection(dataMeta);
            repInfo.setName(repository.getName());
            repInfo.setId(String.valueOf(id));
            KettleDatabaseRepository dbRepository = new KettleDatabaseRepository();
            dbRepository.init(repInfo);
            dbRepository.connect("admin", "admin", true);
            if (dbRepository.isConnected()) {
                logger.debug("资源库【{}】连接成功", dbRepository.getName());
                repositoryCache.put(id, dbRepository);
            } else {
                logger.error("资源库【{}】连接失败", dbRepository.getName());
            }
            return dbRepository;
        }
    }

    @Transactional(readOnly = true)
    public synchronized void disconnectionRepository(Long id) throws Exception {
        if (repositoryCache.containsKey(id)) {
            KettleDatabaseRepository dbRepository = repositoryCache.get(id);
            if (dbRepository != null) {
                dbRepository.disconnect();
                logger.debug("断开资源库【{}】连接", dbRepository.getName());
                repositoryCache.remove(id);
            }
        }
    }


    public synchronized void connectionRepository(Long id) throws Exception {
        if (!repositoryCache.containsKey(id)) {
            KettleRepository repository = this.findById(id);
            DatabaseMeta dataMeta = new DatabaseMeta(repository.getName(), repository.getDbType(), "Native(JDBC)", repository.getHost(), repository.getSchemaName(), repository.getPort(), repository.getUserName(), repository.getPassword());
            dataMeta.setUsingConnectionPool(false);
            KettleDatabaseRepositoryMeta repInfo = new KettleDatabaseRepositoryMeta();
            repInfo.setConnection(dataMeta);
            repInfo.setName(repository.getName());
            repInfo.setId(String.valueOf(id));
            KettleDatabaseRepository dbRepository = new KettleDatabaseRepository();
            dbRepository.init(repInfo);
            dbRepository.connect("admin", "admin", true);
            if (dbRepository.isConnected()) {
                logger.debug("资源库【{}】连接成功", dbRepository.getName());
                repositoryCache.put(id, dbRepository);
            } else {
                logger.error("资源库【{}】连接失败", dbRepository.getName());
                throw new Exception("资源库" + dbRepository.getName() + "连接失败");
            }
        }
    }

    public List<RepositoryElementMetaInterface> getAllTrans(Long id, String directory) throws Exception {
        if (!repositoryCache.containsKey(id)) {
            connectionRepository(id);
        }
        KettleDatabaseRepository dbRepository = getKettleDatabaseRepository(id);
        List<RepositoryElementMetaInterface> elements = new ArrayList<>();
        if (directory == null) {
            directory = "";
        }
        RepositoryDirectoryInterface rootDirectory = dbRepository.findDirectory(directory);
        if (directory.equals("/")) {
            elements.addAll(dbRepository.getTransformationObjects(rootDirectory.getObjectId(), true));
        }
        for (RepositoryDirectoryInterface f : rootDirectory.getChildren()) {
            elements.addAll(dbRepository.getTransformationObjects(f.getObjectId(), true));
            if (!f.getChildren().isEmpty()) {
                elements.addAll(getAllTrans(id, f.getPath()));
            }
        }
        return elements;
    }

    @Transactional(readOnly = true)
    public List<KettleRepository> getConnectedRepository() {
        List<KettleRepository> list = new ArrayList<>();
        repositoryCache.forEach((k, v) -> {
            KettleRepository repository = new KettleRepository();
            Long id = Long.valueOf(v.getRepositoryMeta().getId());
            String name = v.getRepositoryMeta().getName();
            repository.setId(id);
            repository.setName(name);
            list.add(repository);
        });
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("初始化kettle运行环境...");
        KettleEnvironment.init();
        logger.debug("kettle运行环境初始化完毕.");
        logger.debug("开始连接Kettle系统资源库...");
        //自动加载启用的资源库
        List<KettleRepository> list = this.findByProperty("enabled", true);
        list.forEach(r -> {
            try {
                connectionRepository(r.getId());
            } catch (Exception e) {
                //TODO
                e.printStackTrace();
            }
        });

    }

    @Override
    public void destroy() throws Exception {
        logger.debug("释放Kettle系统资源库...");
        repositoryCache.forEach((id, repository) -> {
            try {
                disconnectionRepository(id);
            } catch (Exception e) {
                //TODO
            }
        });
    }
}
