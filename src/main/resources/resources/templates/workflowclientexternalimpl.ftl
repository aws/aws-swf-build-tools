<#include "header.ftl">
<#import "common.ftl" as lib>
<#macro generateExecuteMethodImpl workflow>
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#assign workflowName = executeMethod.workflowName>
<#assign workflowVersion = executeMethod.workflowVersion>
<#assign parameterCount = executeMethod.methodParameters?size>
<#assign hasParameters = (parameterCount > 0)>
<#if hasParameters>
    @Override
    public void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>) { 
        ${executeMethod.methodName}(<@lib.printInput executeMethod/>null);
    }

    @Override
    public void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, StartWorkflowOptions startOptionsOverride) {
    
<#else>
    @Override
    public void ${executeMethod.methodName}() { 
        ${executeMethod.methodName}(null);
    }

    @Override
    public void ${executeMethod.methodName}(StartWorkflowOptions startOptionsOverride) {
    
</#if>
        Object[] _arguments_ = new Object[${parameterCount}]; 
<#list executeMethod.methodParameters as param>
        _arguments_[${param_index}] = ${param.parameterName};
</#list>
        dynamicWorkflowClient.startWorkflowExecution(_arguments_, startOptionsOverride);
    }
</#if>
</#macro>
<#macro generateSignalMethodImpl workflow>
<#list workflow.signals as signalMethod>
<#assign parameterCount = signalMethod.methodParameters?size>
    @Override
    public void ${signalMethod.methodName}(<@lib.printParameters signalMethod/>) {
        Object[] _arguments_ = new Object[${parameterCount}];
<#list signalMethod.methodParameters as param>
        _arguments_[${param_index}] = ${param.parameterName};
</#list>
        dynamicWorkflowClient.signalWorkflowExecution("${signalMethod.signalName}", _arguments_);
    }
</#list>
</#macro>
<#macro generateGetStateMethodImpl workflow>
<#if workflow.getStateMethod??>
<#assign getStateMethod = workflow.getStateMethod>
<#if getStateMethod.hasGenericReturnType>
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ${getStateMethod.methodReturnType} ${getStateMethod.methodName}() <@lib.printThrows getStateMethod/> {
        ${getStateMethod.methodReturnTypeNoGenerics} _state_ = null;
<@lib.printThrowsImpl method=getStateMethod returnType=getStateMethod.methodReturnTypeNoGenerics />

        return _state_;
    }
<#else>
<#if getStateMethod.primitiveReturnType>
    @Override
    public ${getStateMethod.methodReturnTypeUnboxed} ${getStateMethod.methodName}() <@lib.printThrows getStateMethod/> {
        ${getStateMethod.methodReturnType} _state_ = null;
<@lib.printThrowsImpl method=getStateMethod returnType=getStateMethod.methodReturnType />

        if (_state_ == null) {
            _state_ = defaultPrimitiveValue(${getStateMethod.methodReturnTypeUnboxed}.class);
        }
        
        return (${getStateMethod.methodReturnTypeUnboxed}) _state_;
    }
<#else>
    @Override
    public ${getStateMethod.methodReturnType} ${getStateMethod.methodName}() <@lib.printThrows getStateMethod/> {
        ${getStateMethod.methodReturnType} _state_ = null;
<@lib.printThrowsImpl method=getStateMethod returnType=getStateMethod.methodReturnType />

        return _state_;
    }
</#if>
</#if>
</#if>
</#macro>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClientExternalBase;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClientExternal;
import com.amazonaws.services.simpleworkflow.flow.model.WorkflowExecution;
import com.amazonaws.services.simpleworkflow.flow.model.WorkflowType;

class ${clientExternalImplName} extends WorkflowClientExternalBase implements ${clientExternalInterfaceName} {

    public ${clientExternalImplName}(WorkflowExecution workflowExecution, WorkflowType workflowType, 
            StartWorkflowOptions options, DataConverter dataConverter, GenericWorkflowClientExternal genericClient) {
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

<@generateGetStateMethodImpl workflow/>
<#list workflow.superTypes as superWorkflow>
    <@generateGetStateMethodImpl superWorkflow/>
</#list>
}