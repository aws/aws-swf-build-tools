<#include "header.ftl">
<#import "common.ftl" as lib>
<#macro generateExecuteMethodImpl workflow>
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#assign parameterCount = executeMethod.methodParameters?size>
<#assign hasParametersAndExecute = (parameterCount > 0)>
<#assign workflowImplMethodName = "${executeMethod.methodName}Impl">
<#if hasParametersAndExecute>
    @Override
    public final void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>) { 
        ${workflowImplMethodName}(<@lib.printInputAsPromise executeMethod/>(StartWorkflowOptions)null);
    }

    @Override
    public final void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, Promise<?>... waitFor) { 
        ${workflowImplMethodName}(<@lib.printInputAsPromise executeMethod/>(StartWorkflowOptions)null, waitFor);
    }
    
    @Override
    public final void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        ${workflowImplMethodName}(<@lib.printInputAsPromise executeMethod/>optionsOverride, waitFor);
    }
    
    @Override
    public final void ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>) {
        ${workflowImplMethodName}(<@lib.printInput executeMethod/>(StartWorkflowOptions)null);
    }

    @Override
    public final void ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, Promise<?>... waitFor) {
        ${workflowImplMethodName}(<@lib.printInput executeMethod/>(StartWorkflowOptions)null, waitFor);
    }

    @Override
    public final void ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        ${workflowImplMethodName}(<@lib.printInput executeMethod/>optionsOverride, waitFor);
    }
    
    protected void ${workflowImplMethodName}(<@lib.printParametersAsPromiseFinal executeMethod/>, final StartWorkflowOptions schedulingOptionsOverride, Promise<?>... waitFor) {
<#else>
    @Override
    public final void ${executeMethod.methodName}() { 
        ${workflowImplMethodName}(null);
    }
    
    @Override
    public final void ${executeMethod.methodName}(StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        ${workflowImplMethodName}(optionsOverride, waitFor);
    }
    
    protected void ${workflowImplMethodName}(final StartWorkflowOptions schedulingOptionsOverride, Promise<?>... waitFor) {
</#if>
<#if hasParametersAndExecute>
        new Task(new Promise[] { <@lib.printInput executeMethod/>new AndPromise(waitFor) }) {
<#else>
    	new Task(waitFor) {
</#if>
    		@Override
			protected void doExecute() throws Throwable {
                ContinueAsNewWorkflowExecutionParameters _parameters_ = new ContinueAsNewWorkflowExecutionParameters();
                Object[] _input_ = new Object[${parameterCount}];
<#list executeMethod.methodParameters as param>
                _input_[${param_index}] = ${param.parameterName}.get();
</#list>
                String _stringInput_ = dataConverter.toData(_input_);
				_parameters_.setInput(_stringInput_);
				_parameters_ = _parameters_.createContinueAsNewParametersFromOptions(schedulingOptions, schedulingOptionsOverride);
                
                if (genericClient == null) {
                    genericClient = decisionContextProvider.getDecisionContext().getWorkflowClient();
                }
                genericClient.continueAsNewOnCompletion(_parameters_);
			}
		};
    }
</#if>
</#macro>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.core.AndPromise;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Task;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowSelfClientBase;
import com.amazonaws.services.simpleworkflow.flow.generic.ContinueAsNewWorkflowExecutionParameters;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;

public class ${selfClientImplName} extends WorkflowSelfClientBase implements ${selfClientInterfaceName} {

    public ${selfClientImplName}() {
        this(null, new ${workflow.dataConverter}(), null);
    }

    public ${selfClientImplName}(GenericWorkflowClient genericClient) {
        this(genericClient, new ${workflow.dataConverter}(), null);
    }

    public ${selfClientImplName}(GenericWorkflowClient genericClient, 
            DataConverter dataConverter, StartWorkflowOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }

<@generateExecuteMethodImpl workflow/>
<#list workflow.superTypes as superWorkflow>
    <@generateExecuteMethodImpl superWorkflow/>
</#list>
}