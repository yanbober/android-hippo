/**
 * MIT License
 *
 * Copyright (c) 2022 yanbo
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
package cn.yan.hippo.compiler.common

import androidx.room.compiler.processing.*
import cn.yan.hippo.annotations.*
import cn.yan.hippo.compiler.process.ActivityFragmentAnnotationClass
import cn.yan.hippo.compiler.process.AnnotationClass
import cn.yan.hippo.compiler.process.BroadcastReceiverAnnotationClass
import com.squareup.javapoet.ClassName
import javax.tools.Diagnostic
import kotlin.reflect.KClass

/**
 * a XProcessingStep for X Processing.
 * @author yanbo1
 */
class HippoProcessingStep(private val xMessager: XMessager): XProcessingStep {
    override fun annotations(): Set<String> {
        return getSupportedAnnotations()
    }

    @ExperimentalProcessingApi
    override fun process(env: XProcessingEnv, elementsByAnnotation: Map<String, Set<XElement>>
    ): Set<XElement> {
        printWaring("XProcessingStep start processing...")
        val targetClassMap: Map<XTypeElement, AnnotationClass> = createAssignedMethodAnnotations(elementsByAnnotation)
        for (annotationClass in targetClassMap.values) {
            try {
                env.filer.write(annotationClass.generateJavaFile())
            } catch (e: Exception) {
                printError(e.message)
            }
        }
        printWaring("XProcessingStep end processing...")
        return emptySet()
    }

    private fun createAssignedMethodAnnotations(elementsByAnnotation: Map<String, Set<XElement>>): Map<XTypeElement, AnnotationClass> {
        val targetClassMap: MutableMap<XTypeElement, AnnotationClass> = LinkedHashMap()
        parseActivityFragmentMethodAnnotations(targetClassMap, elementsByAnnotation)
        parseBroadcastMethodAnnotations(targetClassMap, elementsByAnnotation)
        return targetClassMap
    }

    private fun parseBroadcastMethodAnnotations(
        targetClassMap: MutableMap<XTypeElement, AnnotationClass>,
        elementsByAnnotation: Map<String, Set<XElement>>
    ) {
        val onReceiveElements = elementsByAnnotation[OnReceive::class.java.name] ?: return
        for (element in onReceiveElements) {
            if (!element.validate()) {
                continue
            }
            try {
                if (!element.isMethod()) {
                    printError(element, "Hippo annotations can only be applied to method!")
                    break
                }
                val enclosingElement = element.enclosingElement as XTypeElement
                var annotationClass = targetClassMap[enclosingElement]
                if (annotationClass == null) {
                    val classPackage = enclosingElement.packageName
                    val className =  getClassName(enclosingElement, classPackage) + "_HippoProxy"
                    val targetType = enclosingElement.qualifiedName
                    annotationClass =
                        BroadcastReceiverAnnotationClass(classPackage, className, targetType)
                    targetClassMap[enclosingElement] = annotationClass
                }
                val methodName = element.name
                val annotation: XAnnotation = element.getAnnotation(ClassName.get(OnReceive::class.java))!!
                annotationClass.create(methodName, annotation)
            } catch (e: Exception) {
                printError(element, e.message)
            }
        }
    }

    private fun parseActivityFragmentMethodAnnotations(
        targetClassMap: MutableMap<XTypeElement, AnnotationClass>,
        elementsByAnnotation: Map<String, Set<XElement>>
    ) {
        val annotations: MutableSet<KClass<out Annotation>> = LinkedHashSet()
        annotations.add(OnTrimMemory::class)
        annotations.add(OnKeyDown::class)
        annotations.add(OnKeyUp::class)
        annotations.add(OnKeyLongPress::class)
        annotations.add(OnKeyMultiple::class)
        annotations.add(OnKeyShortcut::class)
        annotations.add(OnActivityResult::class)
        annotations.add(OnRequestPermissionsResult::class)
        for (supported in annotations) {
            val supportedElements = elementsByAnnotation[supported.java.name] ?: continue
            for (element in supportedElements) {
                if (!element.validate()) {
                    continue
                }
                try {
                    if (!element.isMethod()) {
                        printError(element, "Hippo annotations can only be applied to method!")
                        break
                    }
                    val enclosingElement = element.enclosingElement as XTypeElement
                    var annotationClass = targetClassMap[enclosingElement]
                    if (annotationClass == null) {
                        val classPackage = enclosingElement.packageName
                        val className = getClassName(enclosingElement, classPackage) + "_HippoProxy"
                        val targetType = enclosingElement.qualifiedName
                        annotationClass =
                            ActivityFragmentAnnotationClass(classPackage, className, targetType)
                        targetClassMap[enclosingElement] = annotationClass
                    }
                    val methodName = element.name
                    val annotation: XAnnotation = element.getAnnotation(ClassName.get(supported.java))!!
                    annotationClass.create(methodName, annotation)
                } catch (e: Exception) {
                    printError(element, e.message)
                }
            }
        }
    }

    private fun printError(element: XElement, msg: String?, vararg args: Any) {
        xMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg!!, *args), element)
    }

    private fun printError(msg: String?, vararg args: Any) {
        xMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg!!, *args))
    }

    private fun printWaring(msg: String?, vararg args: Any) {
        xMessager.printMessage(Diagnostic.Kind.WARNING, String.format(msg!!, *args))
    }

    private fun getClassName(typeElement: XTypeElement, packageName: String): String {
        val packageLength = packageName.length + 1
        return typeElement.qualifiedName.substring(packageLength).replace(".", "$")
    }
}
