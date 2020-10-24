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
    val computed: State = IntCode.compute(data, 12, 2)
    computed.getOutput
  }

  def secondStar(data: State): Int = {
    val searchingFor = 19690720
    val (noun, verb) = (for {
      noun <- 0.to(99)
      verb <- 0.to(99)
      if IntCode.compute(data, noun, verb).getOutput == searchingFor
    } yield (noun, verb)).head

    (100 * noun) + verb
  }
}
