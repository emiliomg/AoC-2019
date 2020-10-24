package de.emiliomg.adventofcode.y2019.util

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq

case class IntCode(position: Int, instructions: ArraySeq[Int]) {
  def compute: IntCode = {
    def getPositionValue(state: IntCode, positionOffset: Int): Int =
      state.instructions(state.position + positionOffset)

    def getOperationPositions(state: IntCode): (Int, Int, Int) =
      Tuple3(
        getPositionValue(state, 1),
        getPositionValue(state, 2),
        getPositionValue(state, 3)
      )

    @tailrec
    def step(state: IntCode): IntCode = {
      //      println(s"state = ${state}")
      state.instructions(state.position) match {
        case IntCode.ADDITION =>
          val (aPos, bPos, resultPos) = getOperationPositions(state)
          val a                       = state.instructions(aPos)
          val b                       = state.instructions(bPos)
          val result                  = a + b
          step(IntCode(state.position + 4, state.instructions.updated(resultPos, result)))
        case IntCode.MULTIPLICATION =>
          val (aPos, bPos, resultPos) = getOperationPositions(state)
          val a                       = state.instructions(aPos)
          val b                       = state.instructions(bPos)
          val result                  = a * b
          step(IntCode(state.position + 4, state.instructions.updated(resultPos, result)))
        case IntCode.TERMINATE => state
        case _                 => throw IntCode.InvalidInstructionException(state)
      }
    }

    step(this)
  }

  override def toString: String = s"IntCode($position, (${instructions.mkString(",")}))"
}

object IntCode {
  type INSTRUCTION = Int

  val ADDITION: INSTRUCTION       = 1
  val MULTIPLICATION: INSTRUCTION = 2
  val TERMINATE: INSTRUCTION      = 99

  case class InvalidInstructionException(state: IntCode)
      extends Exception(
        s"Invalid computing instruction found on position ${state.position} (${state.instructions(state.position)}) - $state"
      )
}
