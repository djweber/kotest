package com.sksamuel.kotest.specs.describe

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.Spec
import io.kotest.engine.spec.SpecExecutor
import io.kotest.engine.listener.TestEngineListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.scopes.DslState
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass

@OptIn(kotlin.time.ExperimentalTime::class)
@Ignored
class DescribeSpecIncompleteUsageTest : FunSpec({

   test("it block without .config should error at runtime") {

      var result: Throwable? = null

      val listener = object : TestEngineListener {
         override fun specFinished(kclass: KClass<out Spec>, t: Throwable?, results: Map<TestCase, TestResult>) {
            result = t
         }
      }

      val executor = SpecExecutor(listener)
      executor.execute(DescribeDslTestSpec::class)
      result!!.message shouldBe "Test 'It: incomplete it usage' is incomplete"
      DslState.state = null
   }

})

@Ignored
class DescribeDslTestSpec : DescribeSpec({
   describe("foo") {
      it("incomplete it usage")
   }
})
