<#include "header.ftl">
<#import "common.ftl" as lib>
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.ActivitiesClient;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;

/**
 * Generated from {@link ${qualifiedTypeName}}. 
 * Used to invoke activities asynchronously from workflow code.
 */
public interface ${clientInterfaceName} extends ActivitiesClient
<#list activities.superTypes as superType>
    , ${superType.qualifiedName}Client
</#list>
{
<#list activities.activities as activityMethod>
<#if (activityMethod.methodParameters?size == 0)>

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}();

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
<#else>

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParameters activityMethod/>);

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParameters activityMethod/>, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParameters activityMethod/>, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParametersAsPromise activityMethod/>);

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParametersAsPromise activityMethod/>, Promise<?>... waitFor);

    /**
     * Generated from {@link ${qualifiedTypeName}#${activityMethod.methodName}}
     */
    Promise<${activityMethod.methodReturnType}> ${activityMethod.methodName}(<@lib.printParametersAsPromise activityMethod/>, ActivitySchedulingOptions optionsOverride, Promise<?>... waitFor);
</#if>
</#list>

}