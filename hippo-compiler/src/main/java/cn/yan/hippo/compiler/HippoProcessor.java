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
package cn.yan.hippo.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import cn.yan.hippo.annotations.OnActivityResult;
import cn.yan.hippo.annotations.OnKeyDown;
import cn.yan.hippo.annotations.OnKeyLongPress;
import cn.yan.hippo.annotations.OnKeyMultiple;
import cn.yan.hippo.annotations.OnKeyShortcut;
import cn.yan.hippo.annotations.OnKeyUp;
import cn.yan.hippo.annotations.OnReceive;
import cn.yan.hippo.annotations.OnRequestPermissionsResult;
import cn.yan.hippo.annotations.OnTrimMemory;
import cn.yan.hippo.compiler.process.AnnotationClass;
import cn.yan.hippo.compiler.process.ActivityFragmentAnnotationClass;
import cn.yan.hippo.compiler.process.BroadcastReceiverAnnotationClass;

/**
 * Hippo compile process.
 */

@AutoService(Processor.class)
public final class HippoProcessor extends AbstractProcessor {
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set == null || roundEnvironment == null) {
            return false;
        }

        Map<TypeElement, AnnotationClass> targetClassMap = createAssignedMethodAnnotations(roundEnvironment);
        for (AnnotationClass annotationClass : targetClassMap.values()) {
            try {
                annotationClass.writeTo(mFiler);
            } catch (Exception e) {
                error(null, e.getMessage());
            }
        }
        return true;
    }

    private Map<TypeElement, AnnotationClass> createAssignedMethodAnnotations(RoundEnvironment roundEnvironment) {
        Map<TypeElement, AnnotationClass> targetClassMap = new LinkedHashMap<>();

        parseActivityFragmentMethodAnnotations(targetClassMap, roundEnvironment);
        parseBroadcastMethodAnnotations(targetClassMap, roundEnvironment);
        return targetClassMap;
    }

    private void parseBroadcastMethodAnnotations(Map<TypeElement, AnnotationClass> targetClassMap,
                                                 RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(OnReceive.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }

            try {
                if (element.getKind() != ElementKind.METHOD) {
                    error(element, "Hippo annotations can only be applied to method!");
                    break;
                }
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                AnnotationClass annotationClass = targetClassMap.get(enclosingElement);
                if (annotationClass == null) {
                    annotationClass = new BroadcastReceiverAnnotationClass(mElementUtils, enclosingElement);
                    targetClassMap.put(enclosingElement, annotationClass);
                }

                annotationClass.create(element, OnReceive.class);
            } catch (Exception e) {
                error(element, e.getMessage());
            }
        }
    }

    private void parseActivityFragmentMethodAnnotations(Map<TypeElement, AnnotationClass> targetClassMap,
                                                        RoundEnvironment roundEnvironment) {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(OnTrimMemory.class);
        annotations.add(OnKeyDown.class);
        annotations.add(OnKeyUp.class);
        annotations.add(OnKeyLongPress.class);
        annotations.add(OnKeyMultiple.class);
        annotations.add(OnKeyShortcut.class);
        annotations.add(OnActivityResult.class);
        annotations.add(OnRequestPermissionsResult.class);

        for (Class<? extends Annotation> supported : annotations) {
            for (Element element : roundEnvironment.getElementsAnnotatedWith(supported)) {
                if (!SuperficialValidation.validateElement(element)) {
                    continue;
                }

                try {
                    if (element.getKind() != ElementKind.METHOD) {
                        error(element, "Hippo annotations can only be applied to method!");
                        break;
                    }
                    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                    AnnotationClass annotationClass = targetClassMap.get(enclosingElement);
                    if (annotationClass == null) {
                        annotationClass = new ActivityFragmentAnnotationClass(mElementUtils, enclosingElement);
                        targetClassMap.put(enclosingElement, annotationClass);
                    }
                    annotationClass.create(element, supported);
                } catch (Exception e) {
                    error(element, e.getMessage());
                }
            }
        }
    }


    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(OnTrimMemory.class);
        annotations.add(OnKeyDown.class);
        annotations.add(OnKeyUp.class);
        annotations.add(OnKeyLongPress.class);
        annotations.add(OnKeyMultiple.class);
        annotations.add(OnKeyShortcut.class);
        annotations.add(OnActivityResult.class);
        annotations.add(OnRequestPermissionsResult.class);
        annotations.add(OnReceive.class);
        return annotations;
    }

    private void error(Element element, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), element);
    }
}
