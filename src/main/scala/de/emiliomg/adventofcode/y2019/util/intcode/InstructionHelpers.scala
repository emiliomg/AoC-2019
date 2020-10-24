package de.emiliomg.adventofcode.y2019.util.intcode

import de.emiliomg.adventofcode.y2019.util.IntCode.{Instruction, State}

object InstructionHelpers {
  trait InstructionHasComputation { _: Instruction =>
    val amountOfParameters: Int
    def compute(state: State): State
  }

  trait InstructionComputationIsArithmetic { _: InstructionHasParameters =>
    def getArithmeticResult(state: State, amountOfParameters: Int, f: (Int, Int) => Int): State = {
      val parameters    = getParameters(state, amountOfParameters)
      val result        = f(parameters(1).value, parameters(2).value)
      val updatedMemory = state.memory.updated(parameters(3).position, result)

      state.copy(instructionPointer = state.instructionPointer + amountOfParameters + 1, memory = updatedMemory)
    }
  }

  trait InstructionHasParameters {
    protected[util] def getParameters(state: State, amountOfParameters: Int): Map[Int, InstructionParameter] = {
      1.to(amountOfParameters)
        .toList
        .map { parameterNumber =>
          val instructionParameterAddress = state.instructionPointer + parameterNumber
          val instructionParameterAddressValue = state.memory
            .lift(instructionParameterAddress)
            .getOrElse(
              throw ParameterNotAvailableException(
                state,
                "instructionParameterAddressValue",
                instructionParameterAddress
              )
            )
          val instructionParameterValue = state.memory
            .lift(instructionParameterAddressValue)
            .getOrElse(
              throw ParameterNotAvailableException(
                state,
                "instructionParameterAddressValue",
                instructionParameterAddressValue
              )
            )

          parameterNumber -> InstructionParameter(
            position = instructionParameterAddressValue,
            value = instructionParameterValue
          )
        }
        .toMap
    }
  }

  case class InstructionParameter(position: Int, value: Int)

  case class ParameterNotAvailableException(state: State, step: String, attemptedPosition: Int)
      extends Exception(s"Failed to load value while fetching '$step' on position $attemptedPosition: $state")
}
