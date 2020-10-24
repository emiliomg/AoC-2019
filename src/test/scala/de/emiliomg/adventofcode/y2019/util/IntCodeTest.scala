package de.emiliomg.adventofcode.y2019.util

import de.emiliomg.adventofcode.y2019.util.IntCode.State
import de.emiliomg.adventofcode.y2019.util.intcode.InstructionHelpers._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.collection.immutable.ArraySeq

class IntCodeTest extends AnyFunSpec with Matchers with TableDrivenPropertyChecks {

  describe("IntCode") {
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
    describe("InstructionHelpers") {
      describe("getParameters") {
        it("should correctly parse the parameters of a given state") {
          val testCases = Table(
            ("initialState", "amountOfParameters", "expectedParameters"),
            (
              State(0, ArraySeq(0, 3, 2, 1)),
              3,
              Map(
                1 -> InstructionParameter(position = 3, value = 1),
                2 -> InstructionParameter(position = 2, value = 2),
                3 -> InstructionParameter(position = 1, value = 3)
              )
            ),
            (
              State(3, ArraySeq(5, 6, 7, 0, 0, 2, 9, 0, 123)),
              2,
              Map(
                1 -> InstructionParameter(position = 0, value = 5),
                2 -> InstructionParameter(position = 2, value = 7)
              )
            )
          )
          forAll(testCases) { (state, amountOfParameters, expectedResult) =>
            TestableInstruction.getParameters(state, amountOfParameters) shouldEqual expectedResult
          }
        }

        it("should go boom when fetching parameters that are not in the instruction set") {
          val state  = State(instructionPointer = 2, instructions = ArraySeq(0, 1, 2, 3))
          val thrown = the[ParameterNotAvailableException] thrownBy TestableInstruction.getParameters(state, 2)
          thrown.getMessage should include("position 4")
          thrown.getMessage should include("State(2,ArraySeq(0, 1, 2, 3))")
        }
      }
    }

//  val testCases: TableFor2[ArraySeq[Int], ArraySeq[Int]] = Table(
//    ("initial instructions", "expected computed instructions"),
//    (ArraySeq(1, 0, 0, 0, 99), ArraySeq(2, 0, 0, 0, 99)), // simple addition
//    (ArraySeq(2, 3, 0, 3, 99), ArraySeq(2, 3, 0, 6, 99)), // simple multiplication
//    (ArraySeq(2, 4, 4, 5, 99, 0), ArraySeq(2, 4, 4, 5, 99, 9801)),
//    (ArraySeq(1, 1, 1, 4, 99, 5, 6, 0, 99), ArraySeq(30, 1, 1, 4, 2, 5, 6, 0, 99))
//  )
//

  }
//  should "parse example instruction sets" in {
//    forAll(testCases) { (input, expected) =>
//      val testme = IntCode(0, input)
//      testme.compute.instructions shouldEqual expected
//    }
//  }
//
//  it should "throw an exception on encountering an invalid instruction during the execution" in {
//    val testMe = IntCode(0, ArraySeq(1, 0, 0, 0, 123, 99))
//    val thrown = the[InvalidInstructionException] thrownBy testMe.compute
//    thrown.getMessage should include("position 4 (123)")
//    thrown.getMessage should include("(2,0,0,0,123,99)")
//  }

  case object TestableInstruction extends InstructionHasParameters {}
}
