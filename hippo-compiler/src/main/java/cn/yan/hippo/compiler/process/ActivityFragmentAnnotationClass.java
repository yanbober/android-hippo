/**
 * MIT License
 *
 * Copyright (c) 2018 yanbo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn.yan.hippo.compiler.process;

import androidx.room.compiler.processing.XAnnotation;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import cn.yan.hippo.annotations.OnActivityResult;
import cn.yan.hippo.annotations.OnKeyDown;
import cn.yan.hippo.annotations.OnKeyLongPress;
import cn.yan.hippo.annotations.OnKeyMultiple;
import cn.yan.hippo.annotations.OnKeyShortcut;
import cn.yan.hippo.annotations.OnKeyUp;
import cn.yan.hippo.annotations.OnRequestPermissionsResult;
import cn.yan.hippo.annotations.OnTrimMemory;

/**
 * create Activity or Fragment or other proxy java file.
 */

public final class ActivityFragmentAnnotationClass extends AnnotationClass {
    public ActivityFragmentAnnotationClass(String classPackage, String className, String targetType) {
        super(classPackage, className, targetType);
    }

    @Override
    protected List<TypeName> generateSuperInterface() {
        List<TypeName> typeNameList = new ArrayList<>();

        ClassName iHippoActivityFragment = ClassName.get("cn.yan.hippo.inner", "IHippoActivityFragment");
        TypeName typeName = ParameterizedTypeName.get(iHippoActivityFragment, TypeVariableName.get("T"));

        typeNameList.add(typeName);
        return typeNameList;
    }

    @Override
    protected List<MethodSpec> generateMethods() {
        List<MethodSpec> methodSpecList = new ArrayList<>();

        methodSpecList.add(generateOnActivityResultMethod());
        methodSpecList.add(generateOnRequestPermissionResultMethod());
        methodSpecList.add(generateOnKeyDownMethod());
        methodSpecList.add(generateOnKeyLongPressMethod());
        methodSpecList.add(generateOnKeyMultipleMethod());
        methodSpecList.add(generateOnKeyShortcutMethod());
        methodSpecList.add(generateOnKeyUpMethod());
        methodSpecList.add(generateOnTrimMemoryMethod());

        return methodSpecList;
    }

    private MethodSpec generateOnActivityResultMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onActivityResult")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "requestCode", Modifier.FINAL)
                .addParameter(int.class, "resultCode", Modifier.FINAL)
                .addParameter(ClassName.get("android.content", "Intent"), "data", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnActivityResult.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onActivityResult = method.getMethodAnnotation();
            List<Integer> requestCodes = onActivityResult.getAsIntList("requestCode");
            Object[] objectRequestCodes = new Object[requestCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<requestCodes.size(); index++) {
                objectRequestCodes[index] = requestCodes.get(index);
                conditionBuilder.append("$L == requestCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("target.$L(resultCode, data)", method.getMethodName());
        }
        builder.endControlFlow();
        return builder.build();
    }

    private MethodSpec generateOnRequestPermissionResultMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "requestCode", Modifier.FINAL)
                .addParameter(String[].class, "permissions", Modifier.FINAL)
                .addParameter(int[].class, "grantResults", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnRequestPermissionsResult.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod binding : annotationMethods) {
            XAnnotation onRequestPermissionsResult = binding.getMethodAnnotation();
            List<Integer> requestCodes = onRequestPermissionsResult.getAsIntList("requestCode");
            Object[] objectRequestCodes = new Object[requestCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<requestCodes.size(); index++) {
                objectRequestCodes[index] = requestCodes.get(index);
                conditionBuilder.append("$L == requestCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("target.$L(permissions, grantResults)", binding.getMethodName());
        }
        builder.endControlFlow();
        return builder.build();
    }

    private MethodSpec generateOnTrimMemoryMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onTrimMemory")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "level", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnTrimMemory.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onTrimMemory = method.getMethodAnnotation();
            List<Integer> requestCodes = onTrimMemory.getAsIntList("level");
            Object[] objectRequestCodes = new Object[requestCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<requestCodes.size(); index++) {
                objectRequestCodes[index] = requestCodes.get(index);
                conditionBuilder.append("$L == level");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("target.$L(level)", method.getMethodName());
        }
        builder.endControlFlow();
        return builder.build();
    }

    private MethodSpec generateOnKeyDownMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onKeyDown")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "keyCode", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "KeyEvent"), "event", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnKeyDown.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            builder.addStatement("return false");
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onKeyDown = method.getMethodAnnotation();
            List<Integer> keyCodes = onKeyDown.getAsIntList("keyCode");
            Object[] objectRequestCodes = new Object[keyCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<keyCodes.size(); index++) {
                objectRequestCodes[index] = keyCodes.get(index);
                conditionBuilder.append("$L == keyCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("return target.$L(keyCode, event)", method.getMethodName());
        }
        builder.endControlFlow();
        builder.addStatement("return false");
        return builder.build();
    }

    private MethodSpec generateOnKeyUpMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onKeyUp")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "keyCode", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "KeyEvent"), "event", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnKeyUp.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            builder.addStatement("return false");
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onKeyUp = method.getMethodAnnotation();
            List<Integer> keyCodes = onKeyUp.getAsIntList("keyCode");
            Object[] objectRequestCodes = new Object[keyCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<keyCodes.size(); index++) {
                objectRequestCodes[index] = keyCodes.get(index);
                conditionBuilder.append("$L == keyCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("return target.$L(keyCode, event)", method.getMethodName());
        }
        builder.endControlFlow();
        builder.addStatement("return false");
        return builder.build();
    }

    private MethodSpec generateOnKeyLongPressMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onKeyLongPress")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "keyCode", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "KeyEvent"), "event", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnKeyLongPress.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            builder.addStatement("return false");
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onKeyLongPress = method.getMethodAnnotation();
            List<Integer> keyCodes = onKeyLongPress.getAsIntList("keyCode");
            Object[] objectRequestCodes = new Object[keyCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<keyCodes.size(); index++) {
                objectRequestCodes[index] = keyCodes.get(index);
                conditionBuilder.append("$L == keyCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("return target.$L(keyCode, event)", method.getMethodName());
        }
        builder.endControlFlow();
        builder.addStatement("return false");
        return builder.build();
    }

    private MethodSpec generateOnKeyMultipleMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onKeyMultiple")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "keyCode", Modifier.FINAL)
                .addParameter(int.class, "repeatCount", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "KeyEvent"), "event", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnKeyMultiple.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            builder.addStatement("return false");
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onKeyMultiple = method.getMethodAnnotation();
            List<Integer> keyCodes = onKeyMultiple.getAsIntList("keyCode");
            Object[] objectRequestCodes = new Object[keyCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<keyCodes.size(); index++) {
                objectRequestCodes[index] = keyCodes.get(index);
                conditionBuilder.append("$L == keyCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("return target.$L(keyCode, repeatCount, event)", method.getMethodName());
        }
        builder.endControlFlow();
        builder.addStatement("return false");
        return builder.build();
    }

    private MethodSpec generateOnKeyShortcutMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onKeyShortcut")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(int.class, "keyCode", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "KeyEvent"), "event", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnKeyShortcut.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            builder.addStatement("return false");
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onKeyShortcut = method.getMethodAnnotation();
            List<Integer> keyCodes = onKeyShortcut.getAsIntList("keyCode");
            Object[] objectRequestCodes = new Object[keyCodes.size()];
            StringBuilder conditionBuilder = new StringBuilder("");
            for (int index=0; index<keyCodes.size(); index++) {
                objectRequestCodes[index] = keyCodes.get(index);
                conditionBuilder.append("$L == keyCode");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", objectRequestCodes);
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", objectRequestCodes);
            }
            builder.addStatement("return target.$L(keyCode, event)", method.getMethodName());
        }
        builder.endControlFlow();
        builder.addStatement("return false");
        return builder.build();
    }
}
