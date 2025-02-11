<#include "header.ftl">
package ${packageName};

import software.amazon.awssdk.services.swf.SwfClient;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientFactoryExternalBase;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClientExternal;
import com.amazonaws.services.simpleworkflow.flow.config.SimpleWorkflowClientConfig;
import com.amazonaws.services.simpleworkflow.flow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.flow.model.WorkflowType;

public class ${clientExternalFactoryImplName} extends WorkflowClientFactoryExternalBase<${clientExternalInterfaceName}>  implements ${clientExternalFactoryName} {

    public ${clientExternalFactoryImplName}(SwfClient service, String domain) {
		super(service, domain);
		setDataConverter(new ${workflow.dataConverter}());
	}

    public ${clientExternalFactoryImplName}(SwfClient service, String domain, SimpleWorkflowClientConfig config) {
        super(service, domain, config);
        setDataConverter(new ${workflow.dataConverter}());
    }

	public ${clientExternalFactoryImplName}() {
        super(null);
		setDataConverter(new ${workflow.dataConverter}());
    }
    
    public ${clientExternalFactoryImplName}(GenericWorkflowClientExternal genericClient) {
        super(genericClient);
		setDataConverter(new ${workflow.dataConverter}());
    }
	
    @Override
    protected ${clientExternalInterfaceName} createClientInstance(WorkflowExecution workflowExecution,
            StartWorkflowOptions options, DataConverter dataConverter, GenericWorkflowClientExternal genericClient) {
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#assign workflowName = executeMethod.workflowName>
<#assign workflowVersion = executeMethod.workflowVersion>
        WorkflowType workflowType = WorkflowType.builder()
            .name("${workflowName}").version("${workflowVersion}").build();
        return new ${clientExternalImplName}(workflowExecution, workflowType, options, dataConverter, genericClient);
<#else>
        return new ${clientExternalImplName}(workflowExecution, null, options, dataConverter, genericClient);
</#if>
    }
    
}