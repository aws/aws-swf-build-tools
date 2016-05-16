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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

import com.amazonaws.eclipse.simpleworkflow.asynchrony.common.PrimitiveTypeHelper;
import com.amazonaws.eclipse.simpleworkflow.asynchrony.common.ProcessorConstants;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.NullDataConverter;
import com.amazonaws.services.simpleworkflow.flow.annotations.Signal;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;

final class ProcessorUtils {

    private static final String JAVA_LANG_PREFIX = "java.lang.";

    public static boolean isPrimitive(TypeMirror typeMirror) {
        return typeMirror.getKind().isPrimitive();
    }

    public static boolean isVoidType(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.VOID;
    }
    
    public static boolean isJavaLangType(TypeMirror typeMirror) {
        return isTypePackage(typeMirror, JAVA_LANG_PREFIX);
    }
    
    public static boolean isTypePackage(TypeMirror typeMirror, String packageName) {
        if (typeMirror != null && !isVoidType(typeMirror)) {
            String fullName = typeMirror.toString();
            if (fullName.startsWith(packageName)) {
               return !fullName.substring(packageName.length()).contains(".");
            }
        }
        return false;
    }
    
    public static boolean isDeclaredType(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.DECLARED;
    }

    public static boolean isPromiseType(TypeMirror typeMirror) {
        if (typeMirror != null && !isVoidType(typeMirror)) {
            String fullName = typeMirror.toString();
            if (fullName != null && !fullName.isEmpty()) {
                return fullName.startsWith(Promise.class.getName());
            }
        }

        return false;
    }
    
    public static boolean isGenericType(TypeMirror typeMirror) {
        if (typeMirror != null && isDeclaredType(typeMirror)) {
            DeclaredType declaredType = (DeclaredType) typeMirror;
            return declaredType.getTypeArguments().size() > 0; 
        }
        return false;
    }

    public static boolean isArrayType(TypeMirror typeMirror) {
        return typeMirror.getKind() == TypeKind.ARRAY;
    }

    public static String getTypeName(TypeMirror typeMirror, String generatedTypePackageName) {
        return getTypeName(typeMirror, false, 1, generatedTypePackageName);
    }
    
    public static String getTypeNameUnboxed(TypeMirror typeMirror, String generatedTypePackageName) {
        return getTypeName(typeMirror, true, 1, generatedTypePackageName);
    }

    private static String getTypeName(TypeMirror typeMirror, boolean unboxed, int depth, String generatedTypePackageName) {
        String typeName;
        if (isPrimitive(typeMirror)) {
            if (unboxed) {
                typeName = typeMirror.toString();
            } else {
                typeName = PrimitiveTypeHelper.getWrapper(typeMirror.toString());
            }
        } 
        else if (isVoidType(typeMirror)) {
            typeName = "Void";
        }
        else if (depth == 1 && isPromiseType(typeMirror)) {
            DeclaredType pType = (DeclaredType) typeMirror;
            List<? extends TypeMirror> typeArguments = pType.getTypeArguments();
            if (typeArguments.size() == 0) {
                typeName = "Void";
            }
            else {
                typeName = getTypeName(typeArguments.iterator().next(), unboxed, depth + 1, generatedTypePackageName);
            }
        }
        else if (isArrayType(typeMirror)) {
            ArrayType aType = (ArrayType) typeMirror;
            typeName = getTypeName(aType.getComponentType(), unboxed, depth + 1, generatedTypePackageName) + "[]";
        }
        else {
            typeName = typeMirror.toString();
        }

        if (isJavaLangType(typeMirror)) {
            typeName = typeMirror.toString().substring(JAVA_LANG_PREFIX.length());
        }
        if (isTypePackage(typeMirror, generatedTypePackageName)) {
            typeName = typeMirror.toString().substring(generatedTypePackageName.length() + 1);
        }
        return typeName;
    }
    
    public static String getJustTypeName(TypeMirror typeMirror, String generatedTypePackageName) {
        return getJustTypeName(typeMirror, 1, generatedTypePackageName);
    }
    
    private static String getJustTypeName(TypeMirror typeMirror, int depth, String generatedTypePackageName) {
        String typeName;
        if (isPrimitive(typeMirror)) {
            typeName = PrimitiveTypeHelper.getWrapper(typeMirror.toString());
        }
        else if (depth == 1 && isPromiseType(typeMirror)) {
            DeclaredType pType = (DeclaredType) typeMirror;
            List<? extends TypeMirror> typeArguments = pType.getTypeArguments();
            if (typeArguments.size() == 0) {
                typeName = "Void";
            }
            else {
                typeName = getJustTypeName(typeArguments.iterator().next(), depth + 1, generatedTypePackageName);
            }
        }
        else if (isVoidType(typeMirror))
        {
            typeName = "Void";
        }
        else if (isArrayType(typeMirror)) {
            ArrayType aType = (ArrayType) typeMirror;
            typeName = getJustTypeName(aType.getComponentType(), depth + 1, generatedTypePackageName) + "[]";
        }
        else {
            DeclaredType pType = (DeclaredType) typeMirror;
            typeName = pType.asElement().toString();
        }

        if (typeMirror instanceof DeclaredType && isJavaLangType(typeMirror)) {
            DeclaredType pType = (DeclaredType) typeMirror;
            typeName = pType.asElement().toString().substring(JAVA_LANG_PREFIX.length());
        }
        
        if (typeMirror instanceof DeclaredType && isTypePackage(typeMirror, generatedTypePackageName)) {
            DeclaredType pType = (DeclaredType) typeMirror;
            typeName = pType.asElement().toString().substring(generatedTypePackageName.length() + 1);
        }
        return typeName;
    }

    public static DeclaredType getDeclaredType(ProcessingEnvironment processingEnv, String type) {
        TypeElement typeElem = processingEnv.getElementUtils().getTypeElement(type);
        if (typeElem != null) {
            return processingEnv.getTypeUtils().getDeclaredType(typeElem);
        }

        return null;
    }

    /*
     * public static boolean hasWaitAnnotation(ProcessingEnvironment
     * processingEnv, VariableElement element) { return
     * element.getAnnotation(Wait.class) != null; }
     * 
     * public static boolean hasNoWaitAnnotation(ProcessingEnvironment
     * processingEnv, VariableElement element) { return
     * element.getAnnotation(NoWait.class) != null; }
     * 
     * public static boolean isValue(ProcessingEnvironment processingEnv,
     * VariableElement element) { return isTypeOf(processingEnv, element,
     * "com.amazonaws.services.simpleworkflow.client.asynchrony.Promise"); }
     * 
     * public static boolean isSettable(ProcessingEnvironment processingEnv,
     * VariableElement element) { return isTypeOf(processingEnv, element,
     * "com.amazonaws.services.simpleworkflow.client.asynchrony.Settable"); }
     * 
     * public static boolean isCollection(ProcessingEnvironment processingEnv,
     * VariableElement element) { return isTypeOf(processingEnv, element,
     * "java.util.Collection"); }
     * 
     * public static boolean isValueArray(ProcessingEnvironment processingEnv,
     * VariableElement element) {
     * 
     * if (element.asType().getKind() != TypeKind.ARRAY) { return false; }
     * TypeMirror arrayComponentType = ((ArrayType)
     * element.asType()).getComponentType(); TypeMirror parentType =
     * processingEnv.getElementUtils().getTypeElement(
     * "com.amazonaws.services.simpleworkflow.client.asynchrony.Promise"
     * ).asType(); return
     * processingEnv.getTypeUtils().isAssignable(arrayComponentType,
     * parentType); }
     * 
     * public static boolean isCollectionWaitParameter(ProcessingEnvironment
     * processingEnv, VariableElement element) { return
     * isValueArray(processingEnv, element) || isCollection(processingEnv,
     * element); }
     * 
     * public static boolean isTypeOf(ProcessingEnvironment processingEnv,
     * VariableElement element, String typeOf) { Types types =
     * processingEnv.getTypeUtils(); TypeMirror parentType =
     * processingEnv.getElementUtils().getTypeElement(typeOf).asType(); return
     * types.isAssignable(element.asType(), parentType); }
     * 
     * public static boolean isWaitParameter(ProcessingEnvironment
     * processingEnv, VariableElement parameter) { return
     * (((isValue(processingEnv, parameter) && !isSettable(processingEnv,
     * parameter)) || isCollectionWaitParameter(processingEnv, parameter)) &&
     * !hasNoWaitAnnotation(processingEnv, parameter)); }
     */

    public static String getActivitiesNamePrefix(TypeElement activitiesElement) {
        if (activitiesElement == null) {
            return "";
        }

        Activities annotation = activitiesElement.getAnnotation(Activities.class);
        String activityPrefix = annotation.activityNamePrefix();
        if (activityPrefix == null) {
            activityPrefix = activitiesElement.getSimpleName().toString();
        }
        return activityPrefix;
    }

    public static String getActivitiesVersion(TypeElement activitiesElement) {
        if (activitiesElement == null) return null;

        Activities annotation = activitiesElement.getAnnotation(Activities.class);
        if (annotation == null) return null;
        
        return annotation.version();
    }
    
    public static String getParentActivitiesVersion(ProcessingEnvironment processingEnv, TypeElement activities) {
        for (TypeMirror superInterfaceType : activities.getInterfaces()) {
            Element superInterfaceDeclaration = processingEnv.getTypeUtils().asElement(superInterfaceType);
            String parentVersion = getActivitiesVersion((TypeElement) superInterfaceDeclaration);
            if (parentVersion != null && !parentVersion.isEmpty()) {
                return parentVersion;
            }
        }
        
        return null;
    }

    public static String getActivitiesDataConverter(ProcessingEnvironment env, TypeElement activitiesElement,
            DeclaredType activitiesAnnotation) {
        if (activitiesElement == null) {
            return ProcessorConstants.DEFAULT_DATACONVERTER.getName();
        }

        AnnotationValue dataConverter = getAnnotationValueForClassAttribute(env, activitiesElement, activitiesAnnotation,
                "dataConverter");
        String dataConverterTypeName = dataConverter != null ? dataConverter.getValue().toString() : null;
        if (dataConverterTypeName == null || dataConverterTypeName.equals(NullDataConverter.class.getName())) {
            dataConverterTypeName = ProcessorConstants.DEFAULT_DATACONVERTER.getName();
        }

        return dataConverterTypeName;
    }

    public static String computeActivityName(String prefix, String interfaceName, ExecutableElement activity) {
        assert(activity != null);
        String activityName = null;

        Activity activityAnnotation = activity.getAnnotation(Activity.class);
        if (activityAnnotation != null && !activityAnnotation.name().isEmpty()) {
            activityName = activityAnnotation.name();
        }
        else {
            if (prefix == null || prefix.isEmpty()) {
                activityName = interfaceName + "." + activity.getSimpleName().toString();
            }
            else {
                activityName = prefix + activity.getSimpleName().toString();
            }
        }

        return activityName;
    }

    public static String computeActivityVersion(String version,
            ExecutableElement activity) {
        Activity activityAnnotation = activity.getAnnotation(Activity.class);
        if (activityAnnotation != null && !activityAnnotation.version().isEmpty()) {
            version = activityAnnotation.version();
        }
        
        return version;
    }

    public static String getWorkflowDataConverter(ProcessingEnvironment env, TypeElement workflowElement,
            DeclaredType workflowAnnotation) {
        if (workflowElement == null) {
            return ProcessorConstants.DEFAULT_DATACONVERTER.getName();
        }

        AnnotationValue dataConverter = getAnnotationValueForClassAttribute(env, workflowElement, workflowAnnotation,
                "dataConverter");
        String dataConverterTypeName = dataConverter != null ? dataConverter.getValue().toString() : null;
        if (dataConverterTypeName == null || dataConverterTypeName.equals(NullDataConverter.class.getName())) {
            dataConverterTypeName = ProcessorConstants.DEFAULT_DATACONVERTER.getName();
        }

        return dataConverterTypeName;
    }

    private static AnnotationValue getAnnotationValueForClassAttribute(ProcessingEnvironment env, TypeElement typeElement,
            DeclaredType annotationType, String attribute) {
        AnnotationValue elementValue = null;

        for (AnnotationMirror mirror : typeElement.getAnnotationMirrors()) {
            if (env.getTypeUtils().isSameType(mirror.getAnnotationType(), annotationType)) {
                Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
                for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
                    ExecutableElement elementKey = entry.getKey();
                    if (attribute.equals(elementKey.getSimpleName().toString())) {
                        elementValue = entry.getValue();
                        break;
                    }
                }
            }
        }

        return elementValue;
    }

    public static String computeWorkflowName(String interfaceName, ExecutableElement workflow) {
        assert(workflow != null);
        String workflowName = null;

        Execute options = workflow.getAnnotation(Execute.class);
        if (options != null && !options.name().isEmpty()) {
            workflowName = options.name();
        }
        else {
            workflowName = interfaceName + "." + workflow.getSimpleName().toString();
        }
        
        return workflowName;
    }

    public static String computeWorkflowVersion(ExecutableElement workflow) {
        String version = "1.0"; // Default

        Execute options = workflow.getAnnotation(Execute.class);
        if (!options.version().isEmpty()) {
            version = options.version();
        }

        return version;
    }

    public static String computeSignalName(ExecutableElement signal) {
        String signalName = signal.getSimpleName().toString();

        Signal signalOptions = signal.getAnnotation(Signal.class);
        if (signalOptions != null && signalOptions.name() != null && !signalOptions.name().equals("")) {
            signalName = signalOptions.name();
        }

        return signalName;
    }

    public static String getAnnotationsText(ProcessingEnvironment env, ExecutableElement method, Set<DeclaredType> annotationsToExcludeFromCopying) {
        StringBuilder annotationsText = new StringBuilder();
        for (AnnotationMirror mirror : method.getAnnotationMirrors()) {
            boolean toInclude = true;
            if (annotationsToExcludeFromCopying != null) {
                for (DeclaredType toExclude : annotationsToExcludeFromCopying) {
                    if (env.getTypeUtils().isSameType(mirror.getAnnotationType(), toExclude)) {
                        toInclude = false;
                        break;
                    }
                }
            }
            if (toInclude) {
                StringBuilder annotationText = new StringBuilder();
                ProcessorUtils.writeAnnotation(annotationText, mirror);
                annotationsText.append(annotationText.toString());
            }
        }

        return annotationsText.toString();
    }

    public static void writeAnnotation(StringBuilder builder, AnnotationMirror annotation) {
        DeclaredType annotationDeclaration = annotation.getAnnotationType();
        builder.append("@" + annotationDeclaration.asElement().toString() + "(");

        boolean isFirst = true;
        for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
            ExecutableElement elementKey = entry.getKey();
            AnnotationValue elementValue = entry.getValue();

            builder.append("\n");
            if (isFirst) {
                isFirst = false;
            }
            else {
                builder.append(", ");
            }
            builder.append(elementKey.getSimpleName() + "=");
            writeAnnotationValue(builder, elementValue);
        }
        builder.append(")\n");
    }

    @SuppressWarnings("unchecked")
    public static void writeAnnotationValue(final StringBuilder builder, AnnotationValue annotationValue) {

        SimpleAnnotationValueVisitor6<Void, Void> annotationValueWriter = new SimpleAnnotationValueVisitor6<Void, Void>() {

            @Override
            public Void visitString(String value, Void processingEnv) {
                builder.append("\"");
                builder.append(value);
                builder.append("\"");

                return null;
            }

            @Override
            public Void visitAnnotation(AnnotationMirror value, Void processingEnv) {
                writeAnnotation(builder, (AnnotationMirror) value);

                return null;
            }

            @Override
            public Void visitType(TypeMirror value, Void processingEnv) {
                builder.append(value.toString());
                builder.append(".class");
                return null;
            }

            @Override
            public Void visitEnumConstant(VariableElement value, Void processingEnv) {
                String enumTypeName = value.asType().toString();
                String simpleName = value.getSimpleName().toString();
                builder.append(enumTypeName + "." + simpleName);

                return null;
            }

            @Override
            public Void visitArray(List<? extends AnnotationValue> value, Void processingEnv) {
                builder.append("{ ");
                boolean isFirst = true;
                for (AnnotationValue val : (Collection<AnnotationValue>) value) {
                    if (isFirst) {
                        isFirst = false;
                    }
                    else {
                        builder.append(", ");
                    }
                    writeAnnotationValue(builder, val);
                }
                builder.append(" }");

                return null;
            }

            @Override
            protected Void defaultAction(Object value, Void processingEnv) {
                builder.append(value.toString());

                return null;
            }
        };

        annotationValue.accept(annotationValueWriter, null);
    }

    public static com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities getActivitiesModel(
            ProcessingEnvironment processingEnv, TypeElement activities, DeclaredType activitiesAnnotationType,
            Set<DeclaredType> annotationsToExcludeFromCopying) {

        ActivitiesDeclarationVisitor visitor = new ActivitiesDeclarationVisitor(processingEnv, (TypeElement) activities,
                activitiesAnnotationType, annotationsToExcludeFromCopying);
        visitor.scan(activities, processingEnv);
        return visitor.getActivitiesDefinition();
    }

    public static com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow getWorkflowModel(
            ProcessingEnvironment processingEnv, TypeElement workflow, DeclaredType workflowAnnotationType,
            Set<DeclaredType> annotationsToExcludeFromCopying) {

        WorkflowTypeVisitor visitor = new WorkflowTypeVisitor(processingEnv, (TypeElement) workflow, workflowAnnotationType,
                annotationsToExcludeFromCopying);
        visitor.scan(workflow, processingEnv);
        return visitor.getWorkflowDefinition();
    }

    public static List<com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities> getAllSuperActivities(
            ProcessingEnvironment processingEnv, TypeElement activities, DeclaredType activitiesAnnotationType,
            Set<DeclaredType> annotationsToExcludeFromCopying) {

        List<com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities> superActivities = new ArrayList<com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Activities>();

        for (TypeMirror superInterfaceType : activities.getInterfaces()) {
            Element superInterfaceDeclaration = processingEnv.getTypeUtils().asElement(superInterfaceType);
            Activities annotation = superInterfaceDeclaration != null ? superInterfaceDeclaration.getAnnotation(Activities.class) : null;

            if (annotation != null) {
                superActivities.add(getActivitiesModel(processingEnv, (TypeElement) superInterfaceDeclaration,
                        activitiesAnnotationType, annotationsToExcludeFromCopying));
            }
        }

        return superActivities;
    }

    public static List<com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow> getAllSuperWorkflows(
            ProcessingEnvironment processingEnv, TypeElement workflow, DeclaredType workflowAnnotationType,
            Set<DeclaredType> annotationsToExcludeFromCopying) {

        List<com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow> superWorkflows = new ArrayList<com.amazonaws.eclipse.simpleworkflow.asynchrony.objectmodel.Workflow>();

        for (TypeMirror superInterfaceType : workflow.getInterfaces()) {
            Element superInterfaceDeclaration = processingEnv.getTypeUtils().asElement(superInterfaceType);
            Workflow annotation = superInterfaceDeclaration != null ? superInterfaceDeclaration.getAnnotation(Workflow.class) : null;

            if (annotation != null) {
                superWorkflows.add(getWorkflowModel(processingEnv, (TypeElement) superInterfaceDeclaration,
                        workflowAnnotationType, annotationsToExcludeFromCopying));
            }
        }

        return superWorkflows;
    }
}
