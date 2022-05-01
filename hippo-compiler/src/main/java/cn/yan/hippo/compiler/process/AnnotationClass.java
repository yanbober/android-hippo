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
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Annotation class bean.
 */

public abstract class AnnotationClass {
    private String mPackage;
    private String mName;
    private String mTarget;
    private Map<String, List<AnnotationMethod>> mAnnotationMethodMap;

    public String getPackage() {
        return mPackage;
    }

    public String getName() {
        return mName;
    }

    public String getTarget() {
        return mTarget;
    }

    public Map<String, List<AnnotationMethod>> getAnnotationMethodMap() {
        return mAnnotationMethodMap;
    }

    public AnnotationClass(String classPackage, String className, String targetType) {
        this.mPackage = classPackage;
        this.mName = className;
        this.mTarget = targetType;
        this.mAnnotationMethodMap = new HashMap<>();
    }

    public void create(String methodName, XAnnotation methodAnnotation) {
        AnnotationMethod method = new AnnotationMethod(methodName, methodAnnotation);
        String simpleName = methodAnnotation.getName();
        List<AnnotationMethod> annotationTypedMethods = mAnnotationMethodMap.get(simpleName);
        if (annotationTypedMethods == null) {
            annotationTypedMethods = new ArrayList<>();
            mAnnotationMethodMap.put(simpleName, annotationTypedMethods);
        }

        //TODO check repeat values?
        annotationTypedMethods.add(method);
    }

    public void writeTo(Filer filer) throws IOException {
        generateJavaFile().writeTo(filer);
    }

    public JavaFile generateJavaFile() {
        ClassName targetClassName = ClassName.get(mPackage, mTarget);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(mName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T", targetClassName));

        List<MethodSpec> methodSpecList = generateMethods();
        typeSpec.addMethods(methodSpecList);

        List<TypeName> typeNameList = generateSuperInterface();
        typeSpec.addSuperinterfaces(typeNameList);

        return JavaFile.builder(mPackage, typeSpec.build()).build();
    }

    protected abstract List<TypeName> generateSuperInterface();

    protected abstract List<MethodSpec> generateMethods();
}
