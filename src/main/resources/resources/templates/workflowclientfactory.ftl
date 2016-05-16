<#include "header.ftl">
package ${packageName};

import com.amazonaws.services.simpleworkflow.flow.WorkflowClientFactory;

/**
 * Generated from {@link ${qualifiedTypeName}}. 
 * Used to create workflow clients for use within the scope of a parent workflow.
 * <p>
 * When running outside of the scope of a workflow use {@link ${clientExternalFactoryName}} instead.
 */
public interface ${clientFactoryName} extends WorkflowClientFactory<${clientInterfaceName}> {
    
}