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
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ActivitiesCodeGenerator {
    
    private final String packageName;
    private final String interfaceName;
    private final Activities activities;
    private final SourceFileCreator fileCreator;
    private Configuration cfg;
    private Map<String, Object> root;
    
    public ActivitiesCodeGenerator(String packageName, String interfaceName, Activities activities, 
            SourceFileCreator fileCreator) {
        this.packageName = packageName;
        this.interfaceName = interfaceName;
        this.activities = activities;
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
            root.put("qualifiedTypeName", activities.getQualifiedName());
            root.put("activities", activities);
        }
        
        return root;
    }
    
    private String getClientInterfaceName() {
        return interfaceName + ProcessorConstants.CLIENT_INTERFACE_SUFFIX;
    }
    
    private String getClientImplName() {
        return interfaceName + ProcessorConstants.CLIENT_IMPL_SUFFIX;
    }
    
    public void generateCode() {
        generateActivitiesClientInterface();
        generateActivitiesClientImpl();
    }
    
    private void generateActivitiesClientInterface() {
        String clientInterfaceName = getClientInterfaceName();
        generate(clientInterfaceName, "resources/templates/activitiesclient.ftl");
    }
    
    private void generateActivitiesClientImpl() {
        String clientImplName = getClientImplName();
        generate(clientImplName, "resources/templates/activitiesclientimpl.ftl");
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
