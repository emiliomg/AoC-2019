package de.emiliomg.adventofcode.y2019.day2

import de.emiliomg.adventofcode.y2019.getData
import de.emiliomg.adventofcode.y2019.util.IntCode
import de.emiliomg.adventofcode.y2019.util.IntCode.State

import scala.collection.immutable.ArraySeq

/**
  * https://adventofcode.com/2019/day/2
  */
object Day2 {

  def parseData(data: String): State = State(0, ArraySeq.from(data.split(",").map(_.toInt)))

  val data: State = parseData(getData("2019/2/input.txt").mkString(","))

  def firstStar(data: State): Int = {
    val computed = IntCode.compute(data, 12, 2)
    computed.memory(0)
  }

  def secondStar(data: State) = {
    ???
  }
}
