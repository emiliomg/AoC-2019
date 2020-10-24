package de.emiliomg.adventofcode.y2019.util

import de.emiliomg.adventofcode.y2019.util.IntCode.InvalidInstructionException
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor2}

import scala.collection.immutable.ArraySeq

class IntCodeTest extends AnyFlatSpec with Matchers with TableDrivenPropertyChecks {

  val testCases: TableFor2[ArraySeq[Int], ArraySeq[Int]] = Table(
    ("initial instructions", "expected computed instructions"),
    (ArraySeq(1, 0, 0, 0, 99), ArraySeq(2, 0, 0, 0, 99)), // simple addition
    (ArraySeq(2, 3, 0, 3, 99), ArraySeq(2, 3, 0, 6, 99)), // simple multiplication
    (ArraySeq(2, 4, 4, 5, 99, 0), ArraySeq(2, 4, 4, 5, 99, 9801)),
    (ArraySeq(1, 1, 1, 4, 99, 5, 6, 0, 99), ArraySeq(30, 1, 1, 4, 2, 5, 6, 0, 99))
  )

  "IntCode" should "parse example instruction sets" in {
    forAll(testCases) { (input, expected) =>
      val testme = IntCode(0, input)
      testme.compute.instructions shouldEqual expected
    }
  }

  it should "throw an exception on encountering an invalid instruction during the execution" in {
    val testMe = IntCode(0, ArraySeq(1, 0, 0, 0, 123, 99))
    val thrown = the[InvalidInstructionException] thrownBy testMe.compute
    thrown.getMessage should include("position 4 (123)")
    thrown.getMessage should include("(2,0,0,0,123,99)")
  }
}
