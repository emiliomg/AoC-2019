package de.emiliomg.adventofcode.y2019.day2

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day2Test extends AnyFlatSpec with Matchers {
  "Day 2" should "produce the correct first star result" in {
    Day2.firstStar(Day2.data) shouldEqual 3224742
  }

  it should "produce the correct second star result" in {
    Day2.secondStar(Day2.data) shouldEqual 7960
  }
}
