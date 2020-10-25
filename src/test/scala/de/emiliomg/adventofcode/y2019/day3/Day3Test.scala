package de.emiliomg.adventofcode.y2019.day3

import de.emiliomg.adventofcode.y2019.day3.Day3.{data, Point, Vector}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class Day3Test extends AnyFunSpec with Matchers with TableDrivenPropertyChecks {
  describe("Day 3") {

    val exampleData = Table(
      ("exampleInput", "expectedDistance", "expectedParsedVectors"),
      (
        Array("R8,U5,L5,D3", "U7,R6,D4,L4"),
        6,
        Array(
          Array(Vector(8, 0), Vector(0, 5), Vector(-5, 0), Vector(0, -3)),
          Array(Vector(0, 7), Vector(6, 0), Vector(0, -4), Vector(-4, 0))
        )
      ),
      (
        Array("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"),
        159,
        Array(
          Array(
            Vector(75, 0),
            Vector(0, -30),
            Vector(83, 0),
            Vector(0, 83),
            Vector(-12, 0),
            Vector(0, -49),
            Vector(71, 0),
            Vector(0, 7),
            Vector(-72, 0)
          ),
          Array(
            Vector(0, 62),
            Vector(66, 0),
            Vector(0, 55),
            Vector(34, 0),
            Vector(0, -71),
            Vector(55, 0),
            Vector(0, -58),
            Vector(83, 0)
          )
        )
      ),
      (
        Array("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"),
        135,
        Array(
          Array(
            Vector(98, 0),
            Vector(0, 47),
            Vector(26, 0),
            Vector(0, -63),
            Vector(33, 0),
            Vector(0, 87),
            Vector(-62, 0),
            Vector(0, -20),
            Vector(33, 0),
            Vector(0, 53),
            Vector(51, 0)
          ),
          Array(
            Vector(0, 98),
            Vector(91, 0),
            Vector(0, -20),
            Vector(16, 0),
            Vector(0, -67),
            Vector(40, 0),
            Vector(0, 7),
            Vector(15, 0),
            Vector(0, 6),
            Vector(7, 0)
          )
        )
      )
    )

    describe("parser") {
      it("should convert the input strings to vector lists") {
        forAll(exampleData) { (input, _, expectedVectors) =>
          Day3.parseData(input) shouldEqual expectedVectors
        }
      }
    }

    describe("Point") {
      it("should calculate correct distances") {
        val testCases = Table(
          ("a", "b", "expectedDistance"),
          (Point(0, 0), Point(3, 4), 7),
          (Point(1, 2), Point(3, 4), 4),
          (Point(-1, -1), Point(1, 1), 4),
          (Point(10, 10), Point(10, 10), 0)
        )

        forAll(testCases) { (a, b, expectedDistance) =>
          a.getDistanceTo(b) shouldEqual expectedDistance
        }
      }
      it("should return all points between itself and another point on a vertical OR horizontal line") {
        val testCases = Table(
          ("a", "b", "result"),
          (Point(0, 0), Vector(0, 3), Array(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))),
          (Point(0, 0), Vector(3, 0), Array(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))),
          (Point(1, 2), Vector(3, 0), Array(Point(1, 2), Point(2, 2), Point(3, 2), Point(4, 2))),
          (Point(1, 2), Vector(0, -3), Array(Point(1, 2), Point(1, 1), Point(1, 0), Point(1, -1)))
        )
        forAll(testCases) { (a, b, result) =>
          a.addVector(b) shouldEqual result
        }
      }
    }

    describe("firstStar") {
      it("should provide the correct results for the example data") {
        forAll(exampleData) { (_, expectedDistance, vectors) =>
          Day3.firstStar(vectors) shouldEqual expectedDistance
        }
      }
      it("should compute the correct result for the input data") {
        Day3.firstStar(data) shouldEqual 1225
      }
    }
  }
}
