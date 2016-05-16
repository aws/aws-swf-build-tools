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

import com.amazonaws.eclipse.simpleworkflow.asynchrony.common.SharedProcessorUtils;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.ActivityMethod;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Method;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.MethodParameter;

public class ActivitiesDeclarationVisitor extends ElementScanner6<Void, ProcessingEnvironment> {
    private Set<DeclaredType> annotationsToExcludeFromCopying;
    private final Activities activitiesDefinition;
    private Activities parentActivities;
    
    public ActivitiesDeclarationVisitor(ProcessingEnvironment processingEnv, TypeElement activities, DeclaredType activitiesAnnotationType,
            Set<DeclaredType> annotationsToExcludeFromCopying) {
        this.annotationsToExcludeFromCopying = annotationsToExcludeFromCopying;
        
        String prefix = ProcessorUtils.getActivitiesNamePrefix(activities);
        String version = ProcessorUtils.getActivitiesVersion(activities);
        String dataConverter = ProcessorUtils.getActivitiesDataConverter(processingEnv, activities, activitiesAnnotationType);
        String interfaceName = activities.getSimpleName().toString();
        String qualifiedName = activities.toString();
        this.activitiesDefinition = new Activities(prefix, version, dataConverter, interfaceName, qualifiedName);
        List<Activities> superTypes = ProcessorUtils.getAllSuperActivities(processingEnv, activities, activitiesAnnotationType, 
                annotationsToExcludeFromCopying);
        this.activitiesDefinition.setSuperTypes(superTypes);
        this.parentActivities = SharedProcessorUtils.getParentActivities(activitiesDefinition);
    }
    
    public Activities getActivitiesDefinition() {
        return activitiesDefinition;
    }
    
    @Override
    public Void visitExecutable(ExecutableElement method, ProcessingEnvironment env) {
        String prefix = activitiesDefinition.getPrefix();
        String version = activitiesDefinition.getVersion();
        if (parentActivities != null) {
            if (prefix == null || prefix.isEmpty()) {
                prefix = parentActivities.getPrefix();
            }
            
            if (version == null || version.isEmpty()) {
                version = parentActivities.getVersion();
            }
        }
        
        String activityName = ProcessorUtils.computeActivityName(
                prefix, activitiesDefinition.getInterfaceName(), method);
        String activityVersion = ProcessorUtils.computeActivityVersion(version,
                method);
        
        ActivityMethod activity = new ActivityMethod(activityName, activityVersion);
        setMethodInfo(method, activity, activitiesDefinition.getPackageName());
        
        activity.setAnnotationsToCopy(ProcessorUtils.getAnnotationsText(env, method, annotationsToExcludeFromCopying));
        
        activitiesDefinition.getActivities().add(activity);
        
        return super.visitExecutable(method, env);
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
