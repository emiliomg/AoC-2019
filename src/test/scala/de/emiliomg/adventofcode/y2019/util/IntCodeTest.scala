package de.emiliomg.adventofcode.y2019.util

import de.emiliomg.adventofcode.y2019.util.IntCode.{InvalidInstructionException, State}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.collection.immutable.ArraySeq

class IntCodeTest extends AnyFunSpec with Matchers with TableDrivenPropertyChecks {

  describe("IntCode") {
    describe("applyNounAndVerb") {
      val initial = State(0, ArraySeq(0, 999, 999, 0))
      it("should correctly apply noun and verb to the initial memory") {
        IntCode.applyNounAndVerb(initial, 0, 99) shouldEqual State(0, ArraySeq(0, 0, 99, 0))
      }
      it("should reject a noun not in (0..99)") {
        an[IllegalArgumentException] should be thrownBy IntCode.applyNounAndVerb(initial, 999, 1)
      }
      it("should reject a verb not in (0..99)") {
        an[IllegalArgumentException] should be thrownBy IntCode.applyNounAndVerb(initial, 1, 999)
      }
      it("should reject both a noun and verb not in (0..99)") {
        an[IllegalArgumentException] should be thrownBy IntCode.applyNounAndVerb(initial, 999, 999)
      }
    }
    describe("compute") {
      it("should completely process given instructions") {
        val testCases = Table(
          ("initial state", "final state"),
          (State(0, ArraySeq(1, 0, 0, 0, 99)), State(4, ArraySeq(2, 0, 0, 0, 99))), // simple addition
          (State(0, ArraySeq(2, 3, 0, 3, 99)), State(4, ArraySeq(2, 3, 0, 6, 99))), // simple multiplication
          (State(0, ArraySeq(2, 4, 4, 5, 99, 0)), State(4, ArraySeq(2, 4, 4, 5, 99, 9801))),
          (State(0, ArraySeq(1, 1, 1, 4, 99, 5, 6, 0, 99)), State(8, ArraySeq(30, 1, 1, 4, 2, 5, 6, 0, 99)))
        )
        forAll(testCases) { (input, expectedOutput) =>
          IntCode.compute(input) shouldEqual expectedOutput
        }
      }
      it("should throw an exception on encountering an invalid instruction during the execution") {
        val testMe = State(0, ArraySeq(1, 0, 0, 0, 123, 99))
        val thrown = the[InvalidInstructionException] thrownBy IntCode.compute(testMe)
        thrown.getMessage should include("position 4 (123)")
        thrown.getMessage should include("(2, 0, 0, 0, 123, 99)")
      }
    }
    describe("Instruction") {
      describe("Addition") {
        it("should compute correctly") {
          val testCases = Table(
            ("initial instructions", "expected computed instructions"),
            ( // simple addition
              State(0, ArraySeq(1, 2, 3, 4, 5)),
              State(4, ArraySeq(1, 2, 3, 4, 7))
            ),
            ( // overwrite current instruction pointer
              State(0, ArraySeq(500, 0, 0, 0, 99)),
              State(4, ArraySeq(1000, 0, 0, 0, 99))
            ),
            (
              State(4, ArraySeq(0, 0, 0, 0, 1, 5, 6, 0, 99)),
              State(8, ArraySeq(11, 0, 0, 0, 1, 5, 6, 0, 99))
            )
          )

          forAll(testCases) { (initialState, expectedState) =>
            IntCode.Instruction.Addition.compute(initialState) shouldEqual expectedState
          }
        }
      }
      describe("Multiplication") {
        it("should compute correctly") {
          val testCases = Table(
            ("initial instructions", "expected computed instructions"),
            ( // simple multiplication
              State(0, ArraySeq(2, 2, 3, 4, 5)),
              State(4, ArraySeq(2, 2, 3, 4, 12))
            ),
            ( // overwrite current instruction pointer
              State(0, ArraySeq(500, 0, 0, 0, 99)),
              State(4, ArraySeq(250000, 0, 0, 0, 99))
            ),
            (
              State(4, ArraySeq(0, 0, 0, 0, 2, 5, 6, 0, 99)),
              State(8, ArraySeq(30, 0, 0, 0, 2, 5, 6, 0, 99))
            ),
            (
              State(4, ArraySeq(0, 0, 0, 0, 2, 5, 6, 8, 99)),
              State(8, ArraySeq(0, 0, 0, 0, 2, 5, 6, 8, 30))
            )
          )

          forAll(testCases) { (initialState, expectedState) =>
            IntCode.Instruction.Multiplication.compute(initialState) shouldEqual expectedState
          }
        }
      }
    }
  }
}
