<#include "header.ftl">
<#import "common.ftl" as lib>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClient;

/**
 * Generated from {@link ${qualifiedTypeName}}. 
 * Used to invoke child workflows asynchronously from parent workflow code.
 * Created through {@link ${clientFactoryName}#getClient}.
 * <p>
 * When running outside of the scope of a workflow use {@link ${clientExternalInterfaceName}} instead.
 */
public interface ${clientInterfaceName} extends WorkflowClient
<#list workflow.superTypes as superType>
    , ${superType.qualifiedName}Client
</#list>
{
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#if (executeMethod.methodParameters?size == 0)>

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}();

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
<#else>

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParameters executeMethod/>);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    Promise<${executeMethod.methodReturnType}> ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
</#if>
</#if>
<#list workflow.signals as signalMethod>

    /**
     * Generated from {@link ${qualifiedTypeName}#${signalMethod.methodName}}
     */
    void ${signalMethod.methodName}(<@lib.printParameters signalMethod/>);
</#list>

}