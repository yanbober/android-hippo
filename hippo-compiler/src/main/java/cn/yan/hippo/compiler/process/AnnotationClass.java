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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

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

    public AnnotationClass(Elements elementUtils, TypeElement typeElement) {
        String targetType = typeElement.getQualifiedName().toString();
        String classPackage = getPacakgeName(elementUtils, typeElement);
        String className = getClassName(typeElement, classPackage) + "_HippoProxy";
        this.mPackage = classPackage;
        this.mName = className;
        this.mTarget = targetType;
        this.mAnnotationMethodMap = new HashMap<>();
    }

    public void create(Element element, Class<? extends Annotation> annotationClass) {
        AnnotationMethod method = new AnnotationMethod(element, annotationClass);
        List<AnnotationMethod> annotationTypedMethods = mAnnotationMethodMap.get(annotationClass.getSimpleName());
        if (annotationTypedMethods == null) {
            annotationTypedMethods = new ArrayList<>();
            mAnnotationMethodMap.put(annotationClass.getSimpleName(), annotationTypedMethods);
        }

        //TODO check repeat values?
        annotationTypedMethods.add(method);
    }

    public void writeTo(Filer filer) throws IOException {
        ClassName targetClassName = ClassName.get(mPackage, mTarget);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(mName)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(TypeVariableName.get("T", targetClassName));

        List<MethodSpec> methodSpecList = generateMethods();
        typeSpec.addMethods(methodSpecList);

        List<TypeName> typeNameList = generateSuperInterface();
        typeSpec.addSuperinterfaces(typeNameList);

        JavaFile javaFile = JavaFile.builder(mPackage, typeSpec.build()).build();
        javaFile.writeTo(filer);
    }

    protected abstract List<TypeName> generateSuperInterface();

    protected abstract List<MethodSpec> generateMethods();

    private String getPacakgeName(Elements elementUtils, TypeElement typeElement) {
        return elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    private String getClassName(TypeElement typeElement, String packageName) {
        int packageLength = packageName.length() + 1;
        return typeElement.getQualifiedName().toString().substring(packageLength).replace(".", "$");
    }
}
