package com.haze.kettle.utils;

import com.haze.kettle.KLog;
import com.haze.kettle.Step;
import com.haze.kettle.StepFlow;
import com.haze.kettle.StepWrapper;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.parameters.UnknownParamException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.StringObjectId;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.switchcase.SwitchCaseMeta;
import org.pentaho.di.trans.steps.switchcase.SwitchCaseTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KettleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(KettleUtils.class);

    public static List<RepositoryElementMetaInterface> getAllTrans(Repository repository, String directory) throws KettleException {

        List<RepositoryElementMetaInterface> elements = new ArrayList<>();
        if (directory == null) {
            directory = "";
        }
        RepositoryDirectoryInterface rootDirectory = repository.findDirectory(directory);
        if (directory .equals("/")) {
            elements.addAll(repository.getTransformationObjects(rootDirectory.getObjectId(), true));
        }
        for (RepositoryDirectoryInterface f : rootDirectory.getChildren()) {
            elements.addAll(repository.getTransformationObjects(f.getObjectId(), true));
            if (!f.getChildren().isEmpty()) {
                elements.addAll(getAllTrans(repository, f.getPath()));
            }
        }
        return elements;
    }

    public static TransMeta getTransMetaByObjectId(Repository repository, String objectId) throws KettleException {
        Assert.notNull(objectId, "转换ID不能为空");
        TransMeta transMeta = repository.loadTransformation(new StringObjectId(objectId), null);
        LOGGER.debug(transMeta.getXML());
        for (String stepName : repository.loadTransformation(new StringObjectId(objectId), null).getStepNames()) {
            LOGGER.debug(stepName);
        }
        return repository.loadTransformation(new StringObjectId(objectId), null);
    }

    public static JobMeta getJobMetaByObjectId(Repository repository, String objectId) throws KettleException {
        Assert.notNull(objectId, "作业ID不能为空");
        return repository.loadJob(new StringObjectId(objectId), null);
    }

    public KLog runTrans(Repository repository,String transObjectId, Map<String, String> parameters) throws KettleException, SQLException {
        Assert.notNull(transObjectId, "转换ID不能为空");
        TransMeta transMeta = repository.loadTransformation(new StringObjectId(transObjectId), null);
        if (transMeta == null) {
            throw new KettleException("转换不存在或已删除");
        }

        Trans trans = new Trans(transMeta);
        parameters.forEach((k, v) -> {
            try {
                trans.setParameterValue(k, v);
            } catch (UnknownParamException e) {
                e.printStackTrace();
            }
        });
        //setVariables(trans, kettleConfig.getParams());
        trans.setLogLevel(LogLevel.BASIC);
        trans.execute(null);//执行trans
        trans.waitUntilFinished();
        KLog kettleLog = new KLog();
        kettleLog.setStartDate(trans.getStartDate());
        kettleLog.setEndDate(trans.getEndDate());
        if (trans.getErrors() > 0) {
            LOGGER.warn("kettle转换执行异常, 转换名称={}, 转换ID={}", trans.getName(), transObjectId);
            kettleLog.setSuccess(false);
            kettleLog.setErrors(trans.getErrors());
            throw new KettleException("kettle转换执行异常");
        } else {
            kettleLog.setSuccess(true);
            kettleLog.setErrors(0);
            LOGGER.debug("kettle转换执行成功, 转换名称={}, 转换ID={}", trans.getName(), transObjectId);
        }

        TransLogTable logTable = transMeta.getTransLogTable();
        if (logTable != null && logTable.getDatabaseMeta() != null) {
            DatabaseMeta dataBaseMeta = logTable.getDatabaseMeta();
            Database dataBase = new Database(transMeta, dataBaseMeta);
            dataBase.setVariable("db.sid", "orcl");
            dataBase.setVariable("db.username", "bkht");
            dataBase.setVariable("db.password", "123456");
            dataBase.setVariable("db.ip", "188.9.25.151");
            String tableName = logTable.getTableName();
            String key = logTable.getKeyField().getFieldName();
            LOGGER.debug(key);
            dataBase.connect();
            ResultSet result = dataBase.openQuery("select * from " + tableName + " where " + key + "=" + trans.getBatchId());
            while (result.next()) {
                kettleLog.setId(trans.getBatchId());
                kettleLog.setContent(result.getString(logTable.getLogField().getFieldName()));
                kettleLog.setName(result.getString(logTable.getNameField().getFieldName()));
            }
            dataBase.disconnect();
        }
        //close();
        return kettleLog;
    }


    public Object getTransMetaLogByObjectId(Repository repository, String objectId) throws KettleException {
        Assert.notNull(objectId, "转换ID不能为空");
        TransMeta transMeta = repository.loadTransformation(new StringObjectId(objectId), null);
        return null;
    }


    public List<KLog> getTransMetaLogs(Repository repository,String objectId) throws KettleException, SQLException {
        List<KLog> logList = new ArrayList<>();
        TransMeta transMeta = getTransMetaByObjectId(repository, objectId);
        TransLogTable logTable = transMeta.getTransLogTable();
        if (logTable != null) {
            DatabaseMeta dataBaseMeta = logTable.getDatabaseMeta();
            Database dataBase = new Database(transMeta, dataBaseMeta);
            dataBase.setVariable("db.sid", "orcl");
            dataBase.setVariable("db.username", "bkht");
            dataBase.setVariable("db.password", "123456");
            dataBase.setVariable("db.ip", "188.9.25.151");
            String tableName = logTable.getTableName();
            String key = logTable.getKeyField().getFieldName();
            dataBase.connect();
            ResultSet result = dataBase.openQuery("select * from " + tableName + " where " + logTable.getNameField().getFieldName() + "= '" + transMeta.getName() + "' order by " + key + " asc ");
            while (result.next()) {
                KLog log = new KLog();
                log.setId(result.getLong(key));
                log.setContent(result.getString(logTable.getLogField().getFieldName()));
                log.setName(result.getString(logTable.getNameField().getFieldName()));
                log.setStartDate(result.getTimestamp("startDate"));
                log.setEndDate(result.getTimestamp("endDate"));
                log.setStatus(result.getString(logTable.getStatusField().getFieldName()));
                int errors = result.getInt(logTable.getErrorsField().getFieldName());
                boolean success = errors == 0;
                log.setSuccess(success);
                log.setErrors(errors);
                logList.add(log);
            }
            result.close();
            dataBase.disconnect();
        }
        return logList;
    }

    public void setVariables(Trans trans, Map<String, String> variables) {
        variables.forEach(trans::setVariable);
    }



    public void runJob(Repository repository, String jobObjectId, Map<String, String> parameters) {
        Assert.notNull(jobObjectId, "作业ID不能为空");
        try {
            //加载指定的job
            JobMeta jobMeta = repository.loadJob(new StringObjectId(jobObjectId), null);
            Job job = new Job(repository, jobMeta);
            parameters.forEach((k, v) -> {
                try {
                    jobMeta.setParameterValue(k, v);
                } catch (UnknownParamException e) {
                    e.printStackTrace();
                }
            });
            //kettleConfig.getParams().forEach(job::setVariable);
            job.setLogLevel(LogLevel.BASIC);
            //启动执行指定的job
            job.run();
            job.waitUntilFinished();//等待job执行完；
            job.setFinished(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<RepositoryElementMetaInterface> getAllJobs(Repository repository,String directory) throws KettleException {
        List<RepositoryElementMetaInterface> elements = new ArrayList<>();
        if (directory == null) {
            directory = "/";
        }
        RepositoryDirectoryInterface rootDirectory = repository.findDirectory(directory);
        for (RepositoryDirectoryInterface f : rootDirectory.getChildren()) {
            elements.addAll(repository.getJobObjects(f.getObjectId(), true));
            if (!f.getChildren().isEmpty()) {
                elements.addAll(getAllJobs(repository, f.getName()));
            }
        }
        return elements;
    }

    public StepWrapper getStepWrapper(Repository repository,String objectId) throws KettleException {
        TransMeta transMeta = repository.loadTransformation(new StringObjectId(objectId), null);
        List<StepMeta> stepMetaList = transMeta.getSteps();
        StepWrapper stepWrapper = new StepWrapper();
        List<TransHopMeta> transHopMetaList = transMeta.getTransHops();
        for (StepMeta stepMeta : stepMetaList) {
            Step step = new Step();
            step.setName(stepMeta.getName());
            step.setX(stepMeta.getLocation().x);
            step.setY(stepMeta.getLocation().y);
            step.setType(stepMeta.getTypeId());
            step.setId(stepMeta.getObjectId().getId());
            stepWrapper.getStepList().add(step);
        }
        for (TransHopMeta transHopMeta : transHopMetaList) {
            StepFlow stepFlow = new StepFlow();
            String fromId = transHopMeta.getFromStep().getObjectId().getId();
            String toId = transHopMeta.getToStep().getObjectId().getId();
            for (Step step : stepWrapper.getStepList()) {
                if (step.getId().equals(fromId)) {
                    stepFlow.setFrom(step);
                    if (step.getType().equals("SwitchCase")) {
                        SwitchCaseMeta switchCaseMeta = (SwitchCaseMeta) transHopMeta.getFromStep().getStepMetaInterface();
                        //switchCaseMeta.
                        String fieldName = switchCaseMeta.getFieldname();
                        for (SwitchCaseTarget caseTarget : switchCaseMeta.getCaseTargets()) {
                            if (caseTarget.caseTargetStep.getObjectId().getId().equals(toId)) {
                                stepFlow.setLabel(fieldName + "=" + caseTarget.caseValue);
                                break;
                            }
                        }
                        if (switchCaseMeta.getDefaultTargetStep().getObjectId().getId().equals(toId)) {
                            stepFlow.setLabel("默认步骤" );
                        }
                    }
                }
                if (step.getId().equals(toId)) {
                    stepFlow.setTo(step);
                }
            }

            stepWrapper.getStepFlowList().add(stepFlow);
        }
        return stepWrapper;
    }
}