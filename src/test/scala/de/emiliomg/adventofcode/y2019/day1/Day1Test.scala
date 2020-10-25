package de.emiliomg.adventofcode.y2019.day1

import de.emiliomg.adventofcode.y2019.day1.Day1.{data, firstStar, secondStar}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Day1Test extends AnyFunSpec with Matchers {
  describe("Day 1") {
    describe("first star") {
      it("should correctly process test data") {
        firstStar(Array(12)) shouldEqual 2
        firstStar(Array(14)) shouldEqual 2
        firstStar(Array(1969)) shouldEqual 654
        firstStar(Array(100756)) shouldEqual 33583
      }
      it("should compute correct result") {
        firstStar(data) shouldEqual 3502510
      }
    }
    describe("second start") {
      it("should correctly process test data") {
        secondStar(Array(14)) shouldEqual 2
        secondStar(Array(1969)) shouldEqual 966
        secondStar(Array(100756)) shouldEqual 50346
      }
      it("should compute correct result") {
        secondStar(data) shouldEqual 5250885
      }
    }
  }
}
