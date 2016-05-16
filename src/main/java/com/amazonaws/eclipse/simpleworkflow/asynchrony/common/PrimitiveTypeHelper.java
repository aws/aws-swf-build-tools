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
package com.amazonaws.eclipse.simpleworkflow.asynchrony.common;

import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypeHelper {

    private static final String[] primitiveTypes = { "boolean", "byte", "char", "short", "int", "long", "float", "double", "void" };

    private static final String[] wrapperTypes = { "Boolean", "Byte", "Character",
            "Short", "Integer", "Long", "Float", "Double", "Void" };

    public static final String[] methods = { "booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue",
            "floatValue", "doubleValue" };

    private static Map<String, String> primitiveToWrapper = new HashMap<String, String>();

    private static Map<String, String> wrapperToPrimitive = new HashMap<String, String>();

    private static Map<String, String> toPrimitiveMethods = new HashMap<String, String>();

    static {
        for (int i = 0; i < primitiveTypes.length; i++) {
            primitiveToWrapper.put(primitiveTypes[i], wrapperTypes[i]);
            wrapperToPrimitive.put(wrapperTypes[i], primitiveTypes[i]);
            if (i < primitiveTypes.length - 1) {
                toPrimitiveMethods.put(primitiveTypes[i], methods[i]);
            }
        }
    }

    public static String getWrapper(String primitive) {
        String result = primitiveToWrapper.get(primitive);
        if (result == null) {
            throw new IllegalArgumentException(primitive);
        }
        return result;
    }

    public static String getPrimitive(String wrapper) {
        String result = wrapperToPrimitive.get(wrapper);
        if (result == null) {
            throw new IllegalArgumentException(wrapper);
        }
        return result;
    }

    public static String getToPrimitiveMethod(String primitive) {
        String result = toPrimitiveMethods.get(primitive);
        if (result == null) {
            throw new IllegalArgumentException(primitive);
        }
        return result;
    }
}
