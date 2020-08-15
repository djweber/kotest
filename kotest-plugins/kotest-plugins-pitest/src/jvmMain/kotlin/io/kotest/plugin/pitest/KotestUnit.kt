package io.kotest.plugin.pitest

import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.engine.launcher.KotestEngineLauncher
import io.kotest.engine.listener.TestEngineListener
import io.kotest.engine.spec.AbstractSpec
import io.kotest.engine.test.toDescription
import kotlinx.coroutines.runBlocking
import org.pitest.testapi.Description
import org.pitest.testapi.ResultCollector
import org.pitest.testapi.TestUnit
import kotlin.reflect.KClass

class KotestUnit(val klass: KClass<out AbstractSpec>) : TestUnit {

   override fun getDescription(): Description = Description(klass.toDescription().displayPath(), klass.java)

   override fun execute(rc: ResultCollector) = runBlocking {

      val listener = object : TestEngineListener {

         private val started = mutableSetOf<io.kotest.core.test.Description>()
         private val completed = mutableSetOf<io.kotest.core.test.Description>()

         override fun testStarted(testCase: TestCase) {
            if (started.add(testCase.description))
               rc.notifyStart(Description(testCase.description.displayPath(), klass.java))
         }

         override fun testFinished(testCase: TestCase, result: TestResult) {
            val desc = Description(testCase.description.displayPath(), klass.java)
            if (completed.add(testCase.description)) {
               when (result.error) {
                  null -> rc.notifyEnd(desc)
                  else -> rc.notifyEnd(desc, result.error)
               }
            }
         }
      }

      KotestEngineLauncher(listener).forSpec(klass).launch()
   }
}
