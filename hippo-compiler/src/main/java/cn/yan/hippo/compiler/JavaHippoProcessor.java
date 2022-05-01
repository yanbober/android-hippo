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

import androidx.room.compiler.processing.XMessager;
import androidx.room.compiler.processing.XProcessingEnv;
import androidx.room.compiler.processing.XProcessingStep;
import androidx.room.compiler.processing.javac.JavacBasicAnnotationProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import cn.yan.hippo.compiler.common.HippoProcessingStep;

/**
 * Hippo compile process.
 */
public final class JavaHippoProcessor extends JavacBasicAnnotationProcessor {
    private XMessager xMessager;

    @Override
    public void initialize(@NotNull XProcessingEnv env) {
        super.initialize(env);
        this.xMessager = env.getMessager();
    }

    @NotNull
    @Override
    public Iterable<XProcessingStep> processingSteps() {
        List<XProcessingStep> steps = new ArrayList<>();
        steps.add(new HippoProcessingStep(this.xMessager));
        return steps;
    }
}
