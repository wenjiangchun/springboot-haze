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

    public static StepWrapper getTransStepWrapper(Repository repository,String objectId) throws KettleException {
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
                        /*if (switchCaseMeta.getDefaultTargetStep().getObjectId().getId().equals(toId)) {
                            stepFlow.setLabel("默认步骤" );
                        }*/
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

    public static StepWrapper getJobStepWrapper(Repository repository, String objectId)throws KettleException {
        JobMeta jobMeta = repository.loadJob(new StringObjectId(objectId), null);
        return null;
    }
}