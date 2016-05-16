/*
 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.eclipse.simpleworkflow.asynchrony.freemarker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.eclipse.simpleworkflow.asynchrony.common.ProcessorConstants;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WorkflowCodeGenerator {
    
    private final String packageName;
    private final String interfaceName;
    private final Workflow workflow;
    private final SourceFileCreator fileCreator;
    private Configuration cfg;
    private Map<String, Object> root;
    
    public WorkflowCodeGenerator(String packageName, String interfaceName, Workflow workflow, 
            SourceFileCreator fileCreator) {
        this.packageName = packageName;
        this.interfaceName = interfaceName;
        this.workflow = workflow;
        this.fileCreator = fileCreator;
    }
    
    private Configuration getConfiguration() {
        if (cfg == null) {
            cfg = new Configuration();
            cfg.setClassForTemplateLoading(getClass(), "/");
            cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        }
        
        return cfg;
    }
    
    private Map<String, Object> getRoot() {
        if (root == null) {
            root = new HashMap<String, Object>();
            root.put("packageName", packageName);
            root.put("clientInterfaceName", getClientInterfaceName());
            root.put("clientImplName", getClientImplName());
            root.put("clientFactoryName", getClientFactoryName());
            root.put("clientFactoryImplName", getClientFactoryImplName());
            root.put("clientExternalInterfaceName", getClientExternalInterfaceName());
            root.put("clientExternalImplName", getClientExternalImplName());
            root.put("clientExternalFactoryName", getClientExternalFactoryName());
            root.put("clientExternalFactoryImplName", getClientExternalFactoryImplName());
            root.put("selfClientInterfaceName", getSelfClientInterfaceName());
            root.put("selfClientImplName", getSelfClientImplName());
            root.put("qualifiedTypeName", workflow.getQualifiedName());
            root.put("workflow", workflow);
        }
        
        return root;
    }
    
    private String getClientInterfaceName() {
        return interfaceName + ProcessorConstants.CLIENT_INTERFACE_SUFFIX;
    }
    
    private String getClientImplName() {
        return interfaceName + ProcessorConstants.CLIENT_IMPL_SUFFIX;
    }
    
    private String getClientFactoryName() {
        return interfaceName + ProcessorConstants.CLIENT_FACTORY_SUFFIX;
    }
    
    private String getClientFactoryImplName() {
        return interfaceName + ProcessorConstants.CLIENT_FACTORY_IMPL_SUFFIX;
    }
    
    private String getClientExternalInterfaceName() {
        return interfaceName + ProcessorConstants.CLIENT_EXTERNAL_INTERFACE_SUFFIX;
    }
    
    private String getClientExternalImplName() {
        return interfaceName + ProcessorConstants.CLIENT_EXTERNAL_IMPL_SUFFIX;
    }
    
    private String getClientExternalFactoryName() {
        return interfaceName + ProcessorConstants.CLIENT_EXTERNAL_FACTORY_SUFFIX;
    }
    
    private String getClientExternalFactoryImplName() {
        return interfaceName + ProcessorConstants.CLIENT_EXTERNAL_FACTORY_IMPL_SUFFIX;
    }
    
    private String getSelfClientInterfaceName() {
        return interfaceName + ProcessorConstants.SELF_CLIENT_INTERFACE_SUFFIX;
    }
    
    private String getSelfClientImplName() {
        return interfaceName + ProcessorConstants.SELF_CLIENT_IMPL_SUFFIX;
    }
    
    public void generateCode() {
        generateWorkflowClientInterface();
        generateWorkflowClientImpl();
        generateWorkflowClientFactory();
        generateWorkflowClientFactoryImpl();
        
        generateWorkflowClientExternalInterface();
        generateWorkflowClientExternalImpl();
        generateWorkflowClientExternalFactory();
        generateWorkflowClientExternalFactoryImpl();
        
        generateWorkflowSelfClientInterface();
        generateWorkflowSelfClientImpl();
    }
    
    private void generateWorkflowClientInterface() {
        String clientInterfaceName = getClientInterfaceName();
        generate(clientInterfaceName, "resources/templates/workflowclient.ftl");
    }
    
    private void generateWorkflowClientImpl() {
        String clientImplName = getClientImplName();
        generate(clientImplName, "resources/templates/workflowclientimpl.ftl");
    }
    
    private void generateWorkflowClientFactory() {
        String clientFactoryName = getClientFactoryName();
        generate(clientFactoryName, "resources/templates/workflowclientfactory.ftl");
    }
    
    private void generateWorkflowClientFactoryImpl() {
        String clientFactoryName = getClientFactoryImplName();
        generate(clientFactoryName, "resources/templates/workflowclientfactoryimpl.ftl");
    }
    
    private void generateWorkflowClientExternalInterface() {
        String clientInterfaceName = getClientExternalInterfaceName();
        generate(clientInterfaceName, "resources/templates/workflowclientexternal.ftl");
    }
    
    private void generateWorkflowClientExternalImpl() {
        String clientImplName = getClientExternalImplName();
        generate(clientImplName, "resources/templates/workflowclientexternalimpl.ftl");
    }
    
    private void generateWorkflowClientExternalFactory() {
        String clientFactoryName = getClientExternalFactoryName();
        generate(clientFactoryName, "resources/templates/workflowclientexternalfactory.ftl");
    }
    
    private void generateWorkflowClientExternalFactoryImpl() {
        String clientFactoryName = getClientExternalFactoryImplName();
        generate(clientFactoryName, "resources/templates/workflowclientexternalfactoryimpl.ftl");
    }
    
    private void generateWorkflowSelfClientInterface() {
        String clientInterfaceName = getSelfClientInterfaceName();
        generate(clientInterfaceName, "resources/templates/workflowselfclient.ftl");
    }
    
    private void generateWorkflowSelfClientImpl() {
        String clientImplName = getSelfClientImplName();
        generate(clientImplName, "resources/templates/workflowselfclientimpl.ftl");
    }
    
    private void generate(String className, String templateName) {
        PrintWriter writer = fileCreator.createSourceFile(packageName, className);
        
        try {
            Template template = getConfiguration().getTemplate(templateName);
            template.process(getRoot(), writer);
        }
        catch (IOException e) {
            writer.println("Error loading template: " + templateName);
        }
        catch (TemplateException e) {
            writer.println("Error processing template: " + templateName);
            writer.println(e.getMessage());
        }
        
        writer.flush();
        writer.close();
    }
}
