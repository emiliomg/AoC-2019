package de.emiliomg.adventofcode.y2019.util

import enumeratum.values.{IntEnum, IntEnumEntry}

import scala.collection.immutable.ArraySeq

case object IntCode {
  case class State(instructionPointer: Int, instructions: ArraySeq[Int])

  trait InstructionHelpers {
    protected[util] def getParameters(state: State, amountOfParameters: Int): Map[Int, InstructionParameter] = {
      1.to(amountOfParameters)
        .toList
        .map { parameterNumber =>
          val instructionParameterAddress = state.instructionPointer + parameterNumber
          val instructionParameterAddressValue = state.instructions
            .lift(instructionParameterAddress)
            .getOrElse(
              throw ParameterNotAvailableException(
                state,
                "instructionParameterAddressValue",
                instructionParameterAddress
              )
            )
          val instructionParameterValue = state.instructions
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

  sealed abstract class Instruction(val value: Int) extends IntEnumEntry {
    val amountOfParameters: Int
    def compute(state: State): State
  }

  object Instruction extends IntEnum[Instruction] {
    val values: IndexedSeq[Instruction] = findValues

    case object Addition extends Instruction(1) with InstructionHelpers {
      override val amountOfParameters: Int = 3
      override def compute(state: State): State = {
        val parameters        = getParameters(state, amountOfParameters)
        val result            = parameters(1).value + parameters(2).value
        val updatedParameters = state.instructions.updated(parameters(3).position, result)

        state.copy(instructionPointer = state.instructionPointer + amountOfParameters + 1, updatedParameters)
      }

    }
//    case class Multiplication() extends Instruction(2)
//    case class Termination()    extends Instruction(99)
  }

  case class InstructionParameter(position: Int, value: Int)

  case class ParameterNotAvailableException(state: State, step: String, attemptedPosition: Int)
      extends Exception(s"Failed to load value while fetching '$step' on position $attemptedPosition: $state")
}

//case class IntCode(position: Int, instructions: ArraySeq[Int]) {
//  def compute: IntCode = {
//    def getPositionValue(state: IntCode, positionOffset: Int): Int =
//      state.instructions(state.position + positionOffset)
//
//    def getOperationPositions(state: IntCode): (Int, Int, Int) =
//      Tuple3(
//        getPositionValue(state, 1),
//        getPositionValue(state, 2),
//        getPositionValue(state, 3)
//      )
//
//    @tailrec
//    def step(state: IntCode): IntCode = {
//      //      println(s"state = ${state}")
//      state.instructions(state.position) match {
//        case IntCode.ADDITION =>
//          val (aPos, bPos, resultPos) = getOperationPositions(state)
//          val a                       = state.instructions(aPos)
//          val b                       = state.instructions(bPos)
//          val result                  = a + b
//          step(IntCode(state.position + 4, state.instructions.updated(resultPos, result)))
//        case IntCode.MULTIPLICATION =>
//          val (aPos, bPos, resultPos) = getOperationPositions(state)
//          val a                       = state.instructions(aPos)
//          val b                       = state.instructions(bPos)
//          val result                  = a * b
//          step(IntCode(state.position + 4, state.instructions.updated(resultPos, result)))
//        case IntCode.TERMINATE => state
//        case _                 => throw IntCode.InvalidInstructionException(state)
//      }
//    }
//
//    step(this)
//  }
//
//  override def toString: String = s"IntCode($position, (${instructions.mkString(",")}))"
//}
//
//object IntCode {
//  type INSTRUCTION = Int
//
//  val ADDITION: INSTRUCTION       = 1
//  val MULTIPLICATION: INSTRUCTION = 2
//  val TERMINATE: INSTRUCTION      = 99
//
//  case class InvalidInstructionException(state: IntCode)
//      extends Exception(
//        s"Invalid computing instruction found on position ${state.position} (${state.instructions(state.position)}) - $state"
//      )
//}
