package de.emiliomg.adventofcode.y2019.util.intcode

import de.emiliomg.adventofcode.y2019.util.IntCode.State
import de.emiliomg.adventofcode.y2019.util.intcode.InstructionHelpers._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

import scala.collection.immutable.ArraySeq

class InstructionHelpersTest extends AnyFunSpec with Matchers with TableDrivenPropertyChecks {
  describe("IntCode") {
    describe("InstructionHelpers") {
      describe("InstructionHasParameters") {
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
              TestableInstructionParameters.getParameters(state, amountOfParameters) shouldEqual expectedResult
            }
          }

          it("should go boom when fetching parameters that are not in the instruction set") {
            val state = State(instructionPointer = 2, memory = ArraySeq(0, 1, 2, 3))
            val thrown =
              the[ParameterNotAvailableException] thrownBy TestableInstructionParameters.getParameters(state, 2)
            thrown.getMessage should include("position 4")
            thrown.getMessage should include("State(2,ArraySeq(0, 1, 2, 3))")
          }
        }
      }
      describe("InstructionComputationIsArithmetic") {
        describe("getArithmeticResult") {
          it("should process the state correctly") {
            val testCases = Table(
              ("initialState", "operation", "result"),
              (State(0, ArraySeq(1, 4, 4, 4, 99)), (a: Int, b: Int) => a + b, State(4, ArraySeq(1, 4, 4, 4, 198))),
              (State(0, ArraySeq(1, 4, 4, 4, 99)), (a: Int, b: Int) => a - b, State(4, ArraySeq(1, 4, 4, 4, 0))),
              (State(0, ArraySeq(1, 4, 4, 4, 99)), (a: Int, b: Int) => a * b, State(4, ArraySeq(1, 4, 4, 4, 9801))),
              (State(0, ArraySeq(1, 4, 4, 4, 99)), (a: Int, b: Int) => a / b, State(4, ArraySeq(1, 4, 4, 4, 1)))
            )

            forAll(testCases) { (input, operation, result) =>
              TestableInstructionArithmetic.getArithmeticResult(input, 3, operation) shouldEqual result
            }
          }
        }
      }
    }
  }

  case object TestableInstructionParameters extends InstructionHasParameters
  case object TestableInstructionArithmetic extends InstructionComputationIsArithmetic with InstructionHasParameters
}
