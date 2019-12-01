package de.emiliomg.adventofcode.y2019.day1

import scala.io.Source

/**
 * https://adventofcode.com/2019/day/1
 */
object Day1 extends App {

  val testData = parseData(getData("2019/1/test.txt"))
  println(testData)

  //  println(firstStar(testData))
  //  println(secondStar(testData))

  //  assert(firstStar(testData) == ???)
  //  assert(secondStar(testData) == ???)

  //  val data = parseData(getData("2018/1/input.txt"))
  //
  //  val firstStarResult = firstStar(data)
  //  val secondStarResult = secondStar(data)
  //
  //  println(s"Result first star: $firstStarResult")
  //  println(s"Result second star: $secondStarResult")

  //  assert(firstStarResult == ???)
  //  assert(secondStarResult == ???)

  //  def firstStar(data: ???) = {
  //    ???
  //  }

  //  def secondStar(data: ???) = {
  //    ???
  //  }

  def getData(path: String): List[String] = Source.fromResource(path).getLines().toList

  def parseData(data: List[String]): ??? = ???
}
