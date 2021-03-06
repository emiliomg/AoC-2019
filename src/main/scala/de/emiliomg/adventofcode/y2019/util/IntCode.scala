package de.emiliomg.adventofcode.y2019.util

import de.emiliomg.adventofcode.y2019.util.intcode.InstructionHelpers._
import enumeratum.values.{IntEnum, IntEnumEntry}

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq

case object IntCode {

  def compute(state: State, noun: Int, verb: Int): State = {
    compute(applyNounAndVerb(state, noun, verb))
  }

  @tailrec
  def compute(state: State): State = {
    state.memory(state.instructionPointer) match {
      case Instruction.Addition.value       => compute(Instruction.Addition.compute(state))
      case Instruction.Multiplication.value => compute(Instruction.Multiplication.compute(state))
      case Instruction.Termination.value    => state
      case _                                => throw InvalidInstructionException(state)
    }
  }

  protected[util] def applyNounAndVerb(state: State, noun: Int, verb: Int): State = {
    require(0.to(99).contains(noun))
    require(0.to(99).contains(verb))
    state.copy(memory = state.memory.updated(1, noun).updated(2, verb))
  }

  case class State(instructionPointer: Int, memory: ArraySeq[Int]) {
    def getOutput: Int = memory(0)
  }

  sealed abstract class Instruction(val value: Int) extends IntEnumEntry

  object Instruction extends IntEnum[Instruction] {
    val values: IndexedSeq[Instruction] = findValues

    case object Addition
        extends Instruction(1)
        with InstructionHasComputation
        with InstructionHasParameters
        with InstructionComputationIsArithmetic {
      override val amountOfParameters: Int = 3
      override def compute(state: State): State = {
        getArithmeticResult(state, amountOfParameters, _ + _)
      }

    }

    case object Multiplication
        extends Instruction(2)
        with InstructionHasComputation
        with InstructionHasParameters
        with InstructionComputationIsArithmetic {
      override val amountOfParameters: Int = 3
      override def compute(state: State): State = {
        getArithmeticResult(state, amountOfParameters, _ * _)
      }
    }

    case object Termination extends Instruction(99)
  }

  case class InvalidInstructionException(state: State)
      extends Exception(
        s"Invalid computing instruction found on position ${state.instructionPointer} (${state
          .memory(state.instructionPointer)}) - $state"
      )
}
