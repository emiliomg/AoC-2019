package de.emiliomg.adventofcode.y2019.day1

import de.emiliomg.adventofcode.y2019.getData

import scala.annotation.tailrec

/**
  * https://adventofcode.com/2019/day/1
  */
object Day1 {

  def parseData(data: Array[String]): Array[Int] = data.map(_.toInt)

  val data: Array[Int] = parseData(getData("2019/1/input.txt"))

  def firstStar(data: Array[Int]): Int =
    data.map(calculateFuelForMass).sum

  def secondStar(data: Array[Int]): Int = {
    @tailrec
    def calculateFuel(newMass: Int, baseMass: Int): Int = {
      calculateFuelForMass(newMass) match {
        case newFuel if newFuel >= 0 => calculateFuel(newFuel, baseMass + newFuel)
        case _                       => baseMass
      }
    }
    data.map(mass => calculateFuel(mass, 0))
  }.sum

  def calculateFuelForMass(mass: Int): Int = {
    (Math.floor(mass.toDouble / 3) - 2).toInt
  }
}
