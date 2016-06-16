/*
 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. A copy of the License is
 * located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.eclipse.simpleworkflow.asynchrony.annotationprocessor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.amazonaws.eclipse.simpleworkflow.asynchrony.common.ProcessorConstants;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.freemarker.ActivitiesCodeGenerator;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.freemarker.SourceFileCreator;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.freemarker.WorkflowCodeGenerator;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;

@SupportedAnnotationTypes({ "com.amazonaws.services.simpleworkflow.flow.annotations.Activities",
        "com.amazonaws.services.simpleworkflow.flow.annotations.Workflow" })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AsynchronyDeciderAnnotationProcessor extends AbstractProcessor {

    private Set<DeclaredType> annotationsToExcludeFromCopying;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "AsynchronyDeciderAnnotationProcessor.process() invoked.");
            this.annotationsToExcludeFromCopying = new HashSet<DeclaredType>();
            this.annotationsToExcludeFromCopying.add(ProcessorUtils.getDeclaredType(processingEnv,
                    ProcessorConstants.EXECUTE_ANNOTATION));
            this.annotationsToExcludeFromCopying.add(ProcessorUtils.getDeclaredType(processingEnv,
                    ProcessorConstants.ACTIVITY_ANNOTATION));
            this.annotationsToExcludeFromCopying.add(ProcessorUtils.getDeclaredType(processingEnv,
                    ProcessorConstants.SIGNAL_ANNOTATION));
            this.annotationsToExcludeFromCopying.add(ProcessorUtils.getDeclaredType(processingEnv,
                    ProcessorConstants.GETSTATE_ANNOTATION));
            this.annotationsToExcludeFromCopying.add(ProcessorUtils.getDeclaredType(processingEnv,
                    ProcessorConstants.JAVA_LANG_OVERRIDE));

            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Activities.class);
            for (Element annotatedElement : annotatedElements) {
                ActivitiesValidator activitiesValidator = new ActivitiesValidator();
                activitiesValidator.scan(annotatedElement, processingEnv);

                if (!activitiesValidator.isHasErrors() && annotatedElement instanceof TypeElement) {
                    processingEnv.getMessager().printMessage(Kind.NOTE,
                            "Processing @Activities for " + annotatedElement.getSimpleName());
                    processActivities(annotatedElement);
                }
            }

            annotatedElements = roundEnv.getElementsAnnotatedWith(Workflow.class);
            for (Element annotatedElement : annotatedElements) {
                WorkflowValidator workflowValidator = new WorkflowValidator();
                workflowValidator.scan(annotatedElement, processingEnv);

                if (!workflowValidator.isHasErrors() && annotatedElement instanceof TypeElement) {
                    processingEnv.getMessager().printMessage(Kind.NOTE,
                            "Processing @Workflow for " + annotatedElement.getSimpleName());
                    processWorkflow(roundEnv, annotatedElement);
                }
            }
        }
        else {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Processing finished");
        }

        return false;
    }

    private void processActivities(final Element activities) {
        DeclaredType activitiesAnnotationType = ProcessorUtils.getDeclaredType(processingEnv,
                ProcessorConstants.ACTIVITIES_ANNOTATION_TYPE_CLASS_NAME);

        com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities activitiesDefinition = ProcessorUtils.getActivitiesModel(
                processingEnv, (TypeElement) activities, activitiesAnnotationType, annotationsToExcludeFromCopying);

        String packageName = processingEnv.getElementUtils().getPackageOf(activities).getQualifiedName().toString();
        SourceFileCreator fileCreator = new SourceFileCreator() {

            //@Override
            public PrintWriter createSourceFile(String packageName, String className) {
                OutputStream fileStream = null;

                try {
                    JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + className,
                            activities);
                    fileStream = sourceFile.openOutputStream();
                }
                catch (IOException e) {
                    processingEnv.getMessager().printMessage(Kind.ERROR,
                            "Unable to generate target class for element [reason: " + e.toString() + "]", activities);
                }

                if (fileStream != null)
                    return new PrintWriter(fileStream);

                return null;
            }
        };

        ActivitiesCodeGenerator codeGenerator = new ActivitiesCodeGenerator(packageName, activities.getSimpleName().toString(),
                activitiesDefinition, fileCreator);
        codeGenerator.generateCode();
    }

    private void processWorkflow(RoundEnvironment roundEnv, final Element workflow) {
        DeclaredType workflowAnnotationType = ProcessorUtils.getDeclaredType(processingEnv,
                ProcessorConstants.WORKFLOW_ANNOTATION_TYPE_CLASS_NAME);

        com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow workflowDefinition = ProcessorUtils.getWorkflowModel(
                processingEnv, (TypeElement) workflow, workflowAnnotationType, annotationsToExcludeFromCopying);

        String packageName = processingEnv.getElementUtils().getPackageOf(workflow).getQualifiedName().toString();
        SourceFileCreator fileCreator = new SourceFileCreator() {

            //@Override
            public PrintWriter createSourceFile(String packageName, String className) {
                OutputStream fileStream = null;

                try {
                    JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(packageName + "." + className, workflow);
                    fileStream = sourceFile.openOutputStream();
                }
                catch (IOException e) {
                    processingEnv.getMessager().printMessage(Kind.ERROR,
                            "Unable to generate" + " target class for elmenet [reason: " + e.toString() + "]", workflow);
                }

                if (fileStream != null)
                    return new PrintWriter(fileStream);

                return null;
            }
        };

        WorkflowCodeGenerator workflowCodeGenerator = new WorkflowCodeGenerator(packageName, workflow.getSimpleName().toString(),
                workflowDefinition, fileCreator);
        workflowCodeGenerator.generateCode();
    }
}
