package io.kotest.engine.test

import io.kotest.core.spec.DisplayName
import io.kotest.core.spec.Spec
import io.kotest.core.test.Description
import io.kotest.core.test.DescriptionName
import io.kotest.mpp.annotation
import io.kotest.mpp.bestName
import kotlin.reflect.KClass

/**
 * Returns a [SpecDescription] for this kclass.
 *
 * If the spec has been annotated with @DisplayName (on supported platforms), then that will be used,
 * otherwise the default is to use the fully qualified class name.
 *
 * Note: This name must be globally unique. Two specs, even in different packages,
 * cannot share the same name, so if @DisplayName is used, developers must ensure it does not
 * clash with another spec.
 */
fun KClass<out Spec>.toDescription(): Description.SpecDescription {
   val name = annotation<DisplayName>()?.name ?: bestName()
   return Description.SpecDescription(this, DescriptionName.SpecName(this.bestName(), name))
}
