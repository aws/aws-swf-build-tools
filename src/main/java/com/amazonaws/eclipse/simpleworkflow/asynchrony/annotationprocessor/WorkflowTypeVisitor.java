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
package com.amazonaws.eclipse.simpleworkflow.asynchrony.annotationprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner6;

import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.ExecuteMethod;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.GetStateMethod;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Method;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.MethodParameter;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.SignalMethod;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.GetState;
import com.amazonaws.services.simpleworkflow.flow.annotations.Signal;


public class WorkflowTypeVisitor extends ElementScanner6<Void, ProcessingEnvironment> {
    
    private Workflow workflowDefinition;
    private Set<DeclaredType> annotationsToExcludeFromCopying;
    
    public WorkflowTypeVisitor(ProcessingEnvironment processingEnv, TypeElement workflow, DeclaredType workflowAnnotationType,
            Set<DeclaredType> annotationsToExcludeFromCopying) {
        this.annotationsToExcludeFromCopying = annotationsToExcludeFromCopying;
        
        String dataConverter = ProcessorUtils.getWorkflowDataConverter(processingEnv, workflow, workflowAnnotationType);
        String interfaceName = workflow.getSimpleName().toString();
        String qualifiedName = workflow.toString();
        this.workflowDefinition = new Workflow(null, null, dataConverter, interfaceName, qualifiedName);
        List<Workflow> superTypes = ProcessorUtils.getAllSuperWorkflows(processingEnv, workflow, workflowAnnotationType, 
                annotationsToExcludeFromCopying);
        this.workflowDefinition.setSuperTypes(superTypes);
    }
    
    @Override
    public Void visitExecutable(ExecutableElement method, ProcessingEnvironment env) {
        
        if (method.getAnnotation(Execute.class) != null) {
            String workflowName = ProcessorUtils.computeWorkflowName(
                    workflowDefinition.getInterfaceName(), method);
            String workflowVersion = ProcessorUtils.computeWorkflowVersion(method);
            
            ExecuteMethod executeMethod = new ExecuteMethod(workflowName, workflowVersion);
            setMethodInfo(method, executeMethod, workflowDefinition.getPackageName());
            
            executeMethod.setAnnotationsToCopy(ProcessorUtils.getAnnotationsText(env, method, annotationsToExcludeFromCopying));
            
            workflowDefinition.setExecuteMethod(executeMethod);
        } 
        else if (method.getAnnotation(Signal.class) != null) {
            String signalName = ProcessorUtils.computeSignalName(method);
            
            SignalMethod signalMethod = new SignalMethod(signalName);
            setMethodInfo(method, signalMethod, workflowDefinition.getPackageName());
            
            workflowDefinition.getSignals().add(signalMethod);
        }
        else if (method.getAnnotation(GetState.class) != null) {
            GetStateMethod getStateMethod = new GetStateMethod();
            setMethodInfo(method, getStateMethod, workflowDefinition.getPackageName());
            
            workflowDefinition.setGetStateMethod(getStateMethod);
        }
        
        return super.visitExecutable(method, env);
    }
    
    public Workflow getWorkflowDefinition() {
        return workflowDefinition;
    }
    
    private void setMethodInfo(ExecutableElement methodDeclaration, Method method, String generatedTypePackageName) {
        method.setMethodName(methodDeclaration.getSimpleName().toString());
        TypeMirror returnType = methodDeclaration.getReturnType();
        method.setMethodReturnType(ProcessorUtils.getTypeName(returnType, generatedTypePackageName));
        method.setMethodReturnTypeNoGenerics(ProcessorUtils.getJustTypeName(returnType, generatedTypePackageName));
        method.setMethodReturnTypeUnboxed(ProcessorUtils.getTypeNameUnboxed(returnType, generatedTypePackageName));
        method.setHasGenericReturnType(ProcessorUtils.isGenericType(returnType));
        method.setPrimitiveReturnType(ProcessorUtils.isPrimitive(returnType));
        
        List<? extends VariableElement> parameters = methodDeclaration.getParameters();
        for (VariableElement parameter: parameters)
        {
            TypeMirror paramterType = parameter.asType();
            String parameterTypeName = ProcessorUtils.getTypeName(paramterType, generatedTypePackageName);
            String parameterName = parameter.toString();
            
            MethodParameter methodParam = new MethodParameter(parameterName, parameterTypeName);
            methodParam.setParameterTypeUnboxed(ProcessorUtils.getTypeNameUnboxed(paramterType, generatedTypePackageName));
            method.getMethodParameters().add(methodParam);
        }
        
        List<String> thrownTypes = new ArrayList<String>();
        for (TypeMirror thrownExceptionType: methodDeclaration.getThrownTypes()) {
            thrownTypes.add(thrownExceptionType.toString());
        }
        method.setThrownExceptions(thrownTypes);
    }
}
