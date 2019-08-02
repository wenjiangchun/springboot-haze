package com.haze.kettle.service;

import com.haze.common.HazeStringUtils;
import com.haze.core.service.AbstractBaseService;
import com.haze.kettle.StepWrapper;
import com.haze.kettle.config.KettleProperties;
import com.haze.kettle.dao.KettleLogDao;
import com.haze.kettle.dao.KettleRepositoryDao;
import com.haze.kettle.entity.KettleLog;
import com.haze.kettle.entity.KettleRepository;
import com.haze.kettle.utils.KettleUtils;
import org.pentaho.database.model.IDatabaseType;
import org.pentaho.database.service.DatabaseDialectService;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.StringObjectId;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class KettleRepositoryService extends AbstractBaseService<KettleRepository, Long> implements InitializingBean, DisposableBean {

    private final KettleRepositoryDao kettleRepositoryDao;

    private final KettleLogDao kettleLogDao;

    @Autowired
    private KettleProperties kettleProperties;

    private DatabaseDialectService databaseDialectService = new DatabaseDialectService(false);

    /**
     * 资源库连接缓存存放对象，当资源库链接成功时将链接成功的KettleDatabaseRepository对象存放进来，使用KettleRepository的id作为key.
     */
    private final Map<Long, KettleDatabaseRepository> repositoryCache = new ConcurrentHashMap<>();

    public KettleRepositoryService(KettleRepositoryDao kettleRepositoryDao, KettleLogDao kettleLogDao) {
        this.kettleRepositoryDao = kettleRepositoryDao;
        this.kettleLogDao = kettleLogDao;
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
            logger.error("资源库【{}】表已存在", id);
            throw new KettleException("资源库已存在");
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


    public synchronized void connectionRepository(Long id) throws KettleException {
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
                throw new KettleException("资源库" + dbRepository.getName() + "连接失败");
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


    public List<RepositoryElementMetaInterface> getAllJobs(Long repositoryId, String directory) throws KettleException {
        if (!repositoryCache.containsKey(repositoryId)) {
            connectionRepository(repositoryId);
        }
        KettleDatabaseRepository dbRepository = getKettleDatabaseRepository(repositoryId);
        List<RepositoryElementMetaInterface> elements = new ArrayList<>();
        if (directory == null) {
            directory = "/";
        }
        RepositoryDirectoryInterface rootDirectory = dbRepository.findDirectory(directory);
        if (directory.equals("/")) {
            elements.addAll(dbRepository.getJobObjects(rootDirectory.getObjectId(), true));
        }
        for (RepositoryDirectoryInterface f : rootDirectory.getChildren()) {
            elements.addAll(dbRepository.getJobObjects(f.getObjectId(), true));
            if (!f.getChildren().isEmpty()) {
                elements.addAll(getAllJobs(repositoryId, f.getName()));
            }
        }
        return elements;
    }

    /**
     * 执行转换
     * @param repositoryId 资源库ID
     * @param transObjectId 作业ID
     * @param parameters 参数值
     */
    public KettleLog runTrans(Long repositoryId, String transObjectId, Map<String, String> parameters) throws KettleException {
        Assert.notNull(repositoryId, "资源库ID不能为空");
        Assert.notNull(transObjectId, "转换ID不能为空");
        String errorMessage;
        if (!repositoryCache.containsKey(repositoryId)) {
            errorMessage = String.format("获取资源库对象出错，资源库【id=%d】不存在或无法连接", repositoryId);
            logger.error(errorMessage);
            throw new KettleException(errorMessage);
        } else {
            KettleDatabaseRepository repository = repositoryCache.get(repositoryId);
            TransMeta transMeta = repository.loadTransformation(new StringObjectId(transObjectId), null);
            if (transMeta == null) {
                errorMessage = String.format("转换对象不存在，转换【id=%s】,资源库【id=%d】", transObjectId, repositoryId);
                logger.error(errorMessage);
                throw new KettleException(errorMessage);
            }

            Trans trans = new Trans(transMeta);
            for (String param : parameters.keySet()) {
                try {
                    trans.setParameterValue(param, parameters.get(param));
                } catch (UnknownParamException e) {
                    errorMessage = String.format("设置参数出错，参数name=%s,value=%s",param, parameters.get(param));
                    logger.error(errorMessage);
                    throw new KettleException(errorMessage);
                }
            }
            setVariables(trans, parameters);
            Date startTime = new Date();
            trans.setLogLevel(LogLevel.BASIC);
            trans.execute(null);//执行trans
            trans.waitUntilFinished();
            Date endTime = new Date();
            KettleLog kettleLog = new KettleLog(transObjectId, KettleLog.ObjectType.TRANSFORMATION, transMeta.getName(), this.findById(repositoryId));
            kettleLog.setStartTime(startTime);
            kettleLog.setEndTime(endTime);
            kettleLog.setTaskId(String.valueOf(trans.getBatchId()));
            if (trans.getErrors() > 0) {
                int errors = trans.getErrors();
                String errorContent = trans.getResult().getLogText();
                kettleLog.setSuccess(false);
                kettleLog.setErrorCount(errors);
                kettleLog.setErrorText(errorContent);
                logger.warn("转换执行完毕,出现【{}】个错误", errors);
                TransLogTable logTable = transMeta.getTransLogTable();
                if (logTable != null && logTable.getDatabaseMeta() != null) {
                    DatabaseMeta dataBaseMeta = logTable.getDatabaseMeta();
                    Database dataBase = new Database(transMeta, dataBaseMeta);
                    String tableName = logTable.getTableName();
                    String key = logTable.getKeyField().getFieldName();
                    dataBase.connect();
                    ResultSet result = dataBase.openQuery("select * from " + tableName + " where " + key + "=" + trans.getBatchId());
                    while (true) {
                        try {
                            if (!result.next()) {
                                //kettleLog.setId(trans.getBatchId());
                                //kettleLog.setErrorCount(result.getString(logTable.getLogField().getFieldName()));
                                kettleLog.setName(result.getString(logTable.getNameField().getFieldName()));
                            }
                            ;
                        } catch (SQLException e) {
                            logger.warn("获取转换日志出错,忽略本次转换记录");
                            break;
                        }
                    }
                    dataBase.disconnect();
                }
            } else {
                kettleLog.setSuccess(true);
                kettleLog.setErrorCount(0);
                logger.debug("转换执行成功");
            }
            kettleLogDao.save(kettleLog);

        return kettleLog;
        }
    }

    /**
     * 执行Job作业
     * @param repositoryId 资源库ID
     * @param jobObjectId 作业ID
     * @param parameters 参数值
     */
    public KettleLog runJob(Long repositoryId, String jobObjectId, Map<String, String> parameters) throws KettleException {
        Assert.notNull(repositoryId, "资源库ID不能为空");
        Assert.notNull(jobObjectId, "作业ID不能为空");
        String errorMessage;
        if (!repositoryCache.containsKey(repositoryId)) {
            errorMessage = String.format("获取资源库对象出错，资源库【id=%d】不存在或无法连接", repositoryId);
            logger.error(errorMessage);
            throw new KettleException(errorMessage);
        } else {
            KettleDatabaseRepository repository = repositoryCache.get(repositoryId);
            JobMeta jobMeta;
            try {
                jobMeta = repository.loadJob(new StringObjectId(jobObjectId), null);
            } catch (KettleException e) {
                errorMessage = String.format("作业对象不存在，作业【id=%s】,资源库【id=%d】",jobObjectId, repositoryId);
                logger.error(errorMessage);
                throw new KettleException(errorMessage);
            }
            Job job = new Job(repository, jobMeta);
            for (String param : parameters.keySet()) {
                try {
                    jobMeta.setParameterValue(param, parameters.get(param));
                } catch (UnknownParamException e) {
                    errorMessage = String.format("设置参数出错，参数name=%s,value=%s",param, parameters.get(param));
                    logger.error(errorMessage);
                    throw new KettleException(errorMessage);
                }
            }
            logger.debug("开始执行作业,【资源库={},作业={}】...", repository.getName(), jobMeta.getName());
            Date startTime = new Date();
            job.setLogLevel(LogLevel.ERROR);
            job.run();
            job.waitUntilFinished();
            job.setFinished(true);
            Date endTime = new Date();
            //执行结果处理-写日志
            KettleLog kettleLog = new KettleLog(jobObjectId, KettleLog.ObjectType.JOB, jobMeta.getName(), this.findById(repositoryId));
            kettleLog.setStartTime(startTime);
            kettleLog.setEndTime(endTime);
            kettleLog.setTaskId(String.valueOf(job.getBatchId()));
            kettleLog.setParams(KettleLog.getParams(parameters));
            if (job.getErrors() > 0) {
                int errors = job.getErrors();
                String errorContent = job.getResult().getLogText();
                kettleLog.setSuccess(false);
                kettleLog.setErrorCount(errors);
                kettleLog.setErrorText(errorContent);
                logger.warn("作业执行完毕,出现【{}】个错误", errors);
            } else {
                kettleLog.setSuccess(true);
                kettleLog.setErrorCount(0);
                logger.debug("作业执行成功");
            }
            kettleLogDao.save(kettleLog);
            return kettleLog;
        }
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

    @Transactional(readOnly = true)
    public TransMeta getTransMetaByObjectId(Long repositoryId, String objectId) throws KettleException {
        Assert.notNull(repositoryId, "资源库ID不能为空");
        Assert.notNull(objectId, "转换ID不能为空");
        Repository repository = getKettleDatabaseRepository(repositoryId);
        //TransMeta transMeta = repository.loadTransformation(new StringObjectId(objectId), null);
        /*for (String stepName : repository.loadTransformation(new StringObjectId(objectId), null).getStepNames()) {
            LOGGER.debug(stepName);
        }*/
        return repository.loadTransformation(new StringObjectId(objectId), null);
    }

    @Transactional(readOnly = true)
    public JobMeta getJobMetaByObjectId(Long repositoryId, String objectId) throws KettleException {
        Assert.notNull(repositoryId, "资源库ID不能为空");
        Assert.notNull(objectId, "作业ID不能为空");
        Repository repository = getKettleDatabaseRepository(repositoryId);
        return repository.loadJob(new StringObjectId(objectId), null);
    }

    @Transactional(readOnly = true)
    public StepWrapper getTransStepWrapper(Long repositoryId, String objectId) throws KettleException {
        Repository repository = getKettleDatabaseRepository(repositoryId);
        return KettleUtils.getTransStepWrapper(repository, objectId);
    }

    @Transactional(readOnly = true)
    public StepWrapper getJobStepWrapper(Long repositoryId, String objectId) throws KettleException {
        Repository repository = getKettleDatabaseRepository(repositoryId);
        return KettleUtils.getJobStepWrapper(repository, objectId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("初始化kettle运行环境...");
        String pluginFolder = kettleProperties.getPluginFolder();
        if (HazeStringUtils.isNotBlank(pluginFolder) && Files.isDirectory(Paths.get(pluginFolder))) {
            Files.list(Paths.get(pluginFolder)).forEach(path -> {
                logger.debug("加载kettle插件，{}[{}]", path.getFileName(), path.toString());
                StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(path.toString(), false, true));
            });
        } else {
            logger.error("插件文件夹不存在,[{}]", pluginFolder);
        }
        KettleEnvironment.init();
        logger.debug("kettle运行环境初始化完毕.");
        logger.debug("开始连接Kettle系统资源库...");
        //自动加载启用的资源库
        List<KettleRepository> list = this.findByProperty("enabled", true);
        list.forEach(r -> {
            try {
                connectionRepository(r.getId());
            } catch (Exception e) {
                logger.error("连接资源库失败", e);
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

    private void setVariables(Trans trans, Map<String, String> variables) {
        variables.forEach(trans::setVariable);
    }

}
