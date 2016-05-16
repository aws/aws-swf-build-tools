<#include "header.ftl">
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.WorkflowClientFactoryExternal;

/**
 * Generated from {@link ${qualifiedTypeName}}. 
 * Used to create external workflow client used to start workflow executions or send signals from outside of the scope of a workflow.
 * <p>
 * When starting child workflow from a parent workflow use {@link ${clientFactoryName}} instead.
 */
public interface ${clientExternalFactoryName} extends WorkflowClientFactoryExternal<${clientExternalInterfaceName}> {

}