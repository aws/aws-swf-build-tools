<#include "header.ftl">
<#import "common.ftl" as lib>
<#macro generateExecuteMethodImpl workflow>
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#assign workflowName = executeMethod.workflowName>
<#assign workflowVersion = executeMethod.workflowVersion>
<#assign parameterCount = executeMethod.methodParameters?size>
<#assign hasParameters = (parameterCount > 0)>
<#assign workflowImplMethodName = "${executeMethod.methodName}Impl">
<#if hasParameters>
    @Override
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParameters executeMethod/>) { 
        return ${executeMethod.methodName}(<@lib.printInputAsPromise executeMethod/>(StartWorkflowOptions)null);
    }
    
    @Override
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, Promise<?>... waitFor) {
        return ${executeMethod.methodName}(<@lib.printInputAsPromise executeMethod/>(StartWorkflowOptions)null, waitFor);
    }
    
    
    @Override
    ${executeMethod.annotationsToCopy}
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        return ${executeMethod.methodName}(<@lib.printInputAsPromise executeMethod/>optionsOverride, waitFor);
    }

    @Override
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>) {
        return ${executeMethod.methodName}(<@lib.printInput executeMethod/>(StartWorkflowOptions)null);
    }

    @Override
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, Promise<?>... waitFor) {
        return ${executeMethod.methodName}(<@lib.printInput executeMethod/>(StartWorkflowOptions)null, waitFor);
    }

    @Override
    ${executeMethod.annotationsToCopy}
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        Promise[] _input_ = new Promise[${parameterCount}];
<#list executeMethod.methodParameters as param>
        _input_[${param_index}] = ${param.parameterName};
</#list>
        return (Promise) startWorkflowExecution(_input_, optionsOverride, ${executeMethod.methodReturnTypeNoGenerics}.class, waitFor);
    }
<#else>
    @Override
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}() {
        return ${executeMethod.methodName}((StartWorkflowOptions)null);
    }

    @Override
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(Promise<?>... waitFor) {
        return ${executeMethod.methodName}((StartWorkflowOptions)null, waitFor);
    }
    
    @Override
    ${executeMethod.annotationsToCopy}
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        return (Promise) startWorkflowExecution(new Object[0], optionsOverride, ${executeMethod.methodReturnTypeNoGenerics}.class, waitFor);
    }
</#if>
    	
</#if>
</#macro>
<#macro generateSignalMethodImpl workflow>
<#list workflow.signals as signalMethod>
<#assign parameterCount = signalMethod.methodParameters?size>
    @Override
    public void ${signalMethod.methodName}(<@lib.printParameters signalMethod/>) { 
        Object[] _input_ = new Object[${parameterCount}];
<#list signalMethod.methodParameters as param>
        _input_[${param_index}] = ${param.parameterName};
</#list>
        signalWorkflowExecution("${signalMethod.signalName}", _input_);
    }
</#list>
</#macro>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientBase;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.model.WorkflowType;

class ${clientImplName} extends WorkflowClientBase implements ${clientInterfaceName} {

    public ${clientImplName}(WorkflowExecution workflowExecution, WorkflowType workflowType,  
            StartWorkflowOptions options, DataConverter dataConverter, GenericWorkflowClient genericClient) {
        super(workflowExecution, workflowType, options, dataConverter, genericClient);
    }
    
<@generateExecuteMethodImpl workflow/>
<#list workflow.superTypes as superWorkflow>
    <@generateExecuteMethodImpl superWorkflow/>
</#list>

<@generateSignalMethodImpl workflow/>
<#list workflow.superTypes as superWorkflow>
    <@generateSignalMethodImpl superWorkflow/>
</#list>
}