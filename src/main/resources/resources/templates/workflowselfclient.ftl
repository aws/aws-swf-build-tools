<#include "header.ftl">
<#import "common.ftl" as lib>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowSelfClient;

/**
 * Generated from {@link ${qualifiedTypeName}}. 
 * Used to continue a workflow execution as a new run.
 * Must be used from a worklfow scope. 
 */
public interface ${selfClientInterfaceName} extends WorkflowSelfClient<#list workflow.superTypes as superType>, 
    ${superType.qualifiedName}SelfClient</#list>
{
<#if workflow.executeMethod??>
<#assign executeMethod = workflow.executeMethod>
<#if (executeMethod.methodParameters?size == 0)>

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}();

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
<#else>

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(<@lib.printParameters executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${executeMethod.methodName}}
     */
    void ${executeMethod.methodName}(<@lib.printParametersAsPromise executeMethod/>, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);
</#if>
</#if>
}