package de.emiliomg.adventofcode

import scala.collection.immutable.List
import scala.io.Source

package object y2019 {
  def getData(path: String): Array[String] = Source.fromResource(path).getLines().toArray
}
