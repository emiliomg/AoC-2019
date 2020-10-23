package de.emiliomg.adventofcode.y2019.day2

import scala.annotation.tailrec
import scala.io.Source
import scala.collection.immutable.ArraySeq

/**
  * https://adventofcode.com/2019/day/2
  */
object Day2 extends App {

  val testData1: IntCode = parseData("1,0,0,0,99")
  val testData2: IntCode = parseData("2,3,0,3,99")
  val testData3: IntCode = parseData("2,4,4,5,99,0")
  val testData4: IntCode = parseData("1,1,1,4,99,5,6,0,99")

//  println(testData1)
  assert(testData1.compute.instructions == ArraySeq(2, 0, 0, 0, 99))
//  println(testData2)
  assert(testData2.compute.instructions == ArraySeq(2, 3, 0, 6, 99))
//  println(testData3)
  assert(testData3.compute.instructions == ArraySeq(2, 4, 4, 5, 99, 9801))
//  println(testData4)
  assert(testData4.compute.instructions == ArraySeq(30, 1, 1, 4, 2, 5, 6, 0, 99))

//  println(secondStar(testData))

//  assert(secondStar(testData) == ???)

  val data = parseData(getData("2019/2/input.txt"))

  val firstStarResult = firstStar(data)
//  val secondStarResult = secondStar(data)

  println(s"Result first star: $firstStarResult")
//  println(s"Result second star: $secondStarResult")

  assert(firstStarResult == 3224742)
//  assert(secondStarResult == ???)

  def firstStar(data: IntCode): Int = {
    val fixedData = data.copy(instructions = data.instructions.updated(1, 12).updated(2, 2))
    val computed  = fixedData.compute

    computed.instructions(0)
  }

//  def secondStar(data: ???) = {
//    ???
//  }

  def getData(path: String): String    = Source.fromResource(path).getLines().mkString(",")
  def parseData(data: String): IntCode = IntCode(0, ArraySeq.from(data.split(",").map(_.toInt)))
}

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
        case _                 => throw IntCode.InvalidInstructionException(this)
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
