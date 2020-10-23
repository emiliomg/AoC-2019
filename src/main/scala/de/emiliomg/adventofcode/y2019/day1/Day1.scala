package de.emiliomg.adventofcode.y2019.day1

import scala.annotation.tailrec
import scala.io.Source

/**
  * https://adventofcode.com/2019/day/1
  */
object Day1 extends App {

  assert(firstStar(List(12)) == 2)
  assert(firstStar(List(14)) == 2)
  assert(firstStar(List(1969)) == 654)
  assert(firstStar(List(100756)) == 33583)

  assert(secondStar(List(14)) == 2)
  assert(secondStar(List(1969)) == 966)
  assert(secondStar(List(100756)) == 50346)

  val data = parseData(getData("2019/1/input.txt"))

  val firstStarResult  = firstStar(data)
  val secondStarResult = secondStar(data)

  println(s"Result first star: $firstStarResult")
  println(s"Result second star: $secondStarResult")

  assert(firstStarResult == 3502510)
  assert(secondStarResult == 5250885)

  def firstStar(data: List[Int]): Int =
    data.map(calculateFuelForMass).sum

  def secondStar(data: List[Int]): Int = {
    @tailrec
    def calculateFuel(newMass: Int, baseMass: Int): Double = {
      calculateFuelForMass(newMass) match {
        case newFuel if newFuel >= 0 => calculateFuel(newFuel, baseMass + newFuel)
        case newFuel                 => baseMass
      }
    }
    data.map(mass => calculateFuel(mass, 0))
  }.sum.toInt

  def calculateFuelForMass(mass: Int): Int = {
    (Math.floor(mass / 3) - 2).toInt
  }

  def getData(path: String): List[String] = Source.fromResource(path).getLines().toList

  def parseData(data: List[String]): List[Int] = data.map(_.toInt)
}
