package de.emiliomg.adventofcode.y2019.day3

import de.emiliomg.adventofcode.y2019.getData

import scala.collection.immutable

object Day3 {
  def parseData(data: Array[String]): Array[Array[Vector]] =
    data.map { line =>
      line.split(",").map { rawVector =>
        val direction = rawVector.substring(0, 1)
        val length    = rawVector.substring(1).toInt
        direction match {
          case "U" => Vector(0, length)
          case "D" => Vector(0, -length)
          case "R" => Vector(length, 0)
          case "L" => Vector(-length, 0)
        }
      }
    }

  val data: Array[Array[Vector]] = parseData(getData("2019/3/input.txt"))

  def firstStar(data: Array[Array[Vector]]): Int = {
    val visitedPoints: Array[Accumulator] = data.map { vectorList =>
      vectorList.foldLeft(Accumulator(immutable.List.empty, Point(0, 0))) { (acc, elem) =>
        val newPoints: Array[Point] =
          acc.lastPosition.addVector(elem).drop(1) // drop the first one since it is the same point as the current point
        acc.copy(visitedPoints = acc.visitedPoints ++ newPoints.toList, lastPosition = newPoints.last)
      }
    }

    val commonPoints =
      visitedPoints
        .flatMap(vp => vp.visitedPoints.distinct)
        .groupBy(identity)
        .view
        .mapValues(_.length)
        .filter(_._2 > 1)
        .toMap
        .keys
        .toList

    val distances: List[Int] = commonPoints.map(_.getDistanceTo(Point(0, 0)))
    distances.min
  }

  case class Accumulator(visitedPoints: List[Point], lastPosition: Point) {
    override def toString: String = s"Accumulator(lastPosition: $lastPosition, visitedPoints:${visitedPoints})"
  }

  case class Point(x: Int, y: Int) {
    def addVector(vector: Vector): Array[Point] = {
      val xStep = if (vector.x >= 0) 1 else -1
      val yStep = if (vector.y >= 0) 1 else -1
      (for {
        xDiff <- x.to(x + vector.x, xStep)
        yDiff <- y.to(y + vector.y, yStep)
      } yield Point(xDiff, yDiff)).toArray
    }

    def getDistanceTo(other: Point): Int = {
      Math.abs(x - other.x) + Math.abs(y - other.y)
    }
  }
  case class Vector(x: Int, y: Int)
}
