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
import cn.yan.hippo.annotations.OnReceive;

/**
 * create BroadcastReceiver proxy java file.
 */

public final class BroadcastReceiverAnnotationClass extends AnnotationClass {
    public BroadcastReceiverAnnotationClass(String classPackage, String className, String targetType) {
        super(classPackage, className, targetType);
    }

    @Override
    protected List<TypeName> generateSuperInterface() {
        List<TypeName> typeNameList = new ArrayList<>();

        ClassName iHippoBroadcastReceiver = ClassName.get("cn.yan.hippo.inner", "IHippoBroadcastReceiver");
        TypeName typeName = ParameterizedTypeName.get(iHippoBroadcastReceiver, TypeVariableName.get("T"));

        typeNameList.add(typeName);
        return typeNameList;
    }

    @Override
    protected List<MethodSpec> generateMethods() {
        List<MethodSpec> methodSpecList = new ArrayList<>();

        methodSpecList.add(generateOnReceiveMethod());

        return methodSpecList;
    }

    private MethodSpec generateOnReceiveMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onReceive")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(TypeVariableName.get("T"), "target", Modifier.FINAL)
                .addParameter(ClassName.get("android.content", "Context"), "context", Modifier.FINAL)
                .addParameter(ClassName.get("android.content", "Intent"), "intent", Modifier.FINAL);

        final List<AnnotationMethod> annotationMethods = getAnnotationMethodMap().get(OnReceive.class.getSimpleName());
        if (annotationMethods == null || annotationMethods.isEmpty()) {
            builder.addStatement("target.onReceive(context, intent)");
            return builder.build();
        }

        boolean firstCondition = true;
        for (AnnotationMethod method : annotationMethods) {
            XAnnotation onReceive = method.getMethodAnnotation();
            List<String> actions = onReceive.getAsStringList("action");
            StringBuilder conditionBuilder = new StringBuilder("");
            for (String action : actions) {
                conditionBuilder.append("\"$L\".equals(intent.getAction())");
                conditionBuilder.append(" || ");
            }
            String condition = conditionBuilder.substring(0, conditionBuilder.length() - " || ".length());

            if (firstCondition) {
                builder.beginControlFlow("if (" + condition + ")", actions.toArray());
                firstCondition = false;
            } else {
                builder.nextControlFlow("else if (" + condition + ")", actions.toArray());
            }
            builder.addStatement("target.$L(context, intent)", method.getMethodName());
        }
        builder.endControlFlow();
        return builder.build();
    }
}
