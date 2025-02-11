<#include "header.ftl">
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientFactoryBase;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;
import com.amazonaws.services.simpleworkflow.flow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.flow.model.WorkflowType;

public class ${clientFactoryImplName} extends WorkflowClientFactoryBase<${clientInterfaceName}> implements ${clientFactoryName} {
    
    public ${clientFactoryImplName}() {
        this(null, null, null);
    }

    public ${clientFactoryImplName}(StartWorkflowOptions startWorkflowOptions) {
        this(startWorkflowOptions, null, null);
    }

    public ${clientFactoryImplName}(StartWorkflowOptions startWorkflowOptions, DataConverter dataConverter) {
        this(startWorkflowOptions, dataConverter, null);
    }

    public ${clientFactoryImplName}(StartWorkflowOptions startWorkflowOptions, DataConverter dataConverter,
            GenericWorkflowClient genericClient) {
        super(startWorkflowOptions, dataConverter, genericClient);
    }
    
    @Override
    protected ${clientInterfaceName} createClientInstance(WorkflowExecution execution,
            StartWorkflowOptions options, DataConverter dataConverter, GenericWorkflowClient genericClient) {
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#assign workflowName = executeMethod.workflowName>
<#assign workflowVersion = executeMethod.workflowVersion>
        WorkflowType workflowType = WorkflowType.builder()
          .name("${workflowName}").version("${workflowVersion}").build();
        return new ${clientImplName}(execution, workflowType, options, dataConverter, genericClient);
<#else>
        return new ${clientImplName}(execution, null, options, dataConverter, genericClient);
</#if>
    }
   
}