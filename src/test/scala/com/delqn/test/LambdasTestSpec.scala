package com.delqn.test

import org.scalatest._
import com.delqn.aws.Lambdas

import scala.util.matching.Regex

/*

object Lambdas {
  val urls: Seq[String] = Seq(
    "http://fcs001.xreflector.net/mitte.html",
    "http://fcs002.xreflector.net/mitte.html",
    "http://laboenligne.ca/fcs003/mitte.html"
  )

  def removeAngles(s: String): String = s.filter(_ != '<').filter(_ != '>')
  def getCells(s: String): Iterator[String] = textPattern.findAllIn(s).map(removeAngles)

  def getUrl(url: String): Iterator[Iterator[String]] = {
    // get the HTML source and remove new lines so regexes work correctly
    val html = Source.fromURL(url).mkString.filter(_ != '\n')
    rowPattern.findAllIn(html).map(getCells)
  }
}

 */

class LambdasTestSpec extends FlatSpec with Matchers {
  val html = "blah <table> <tr><td>snth</td><td>aoeu</td></tr> <tr>x</tr></table>"

  "URLs" should "be 3" in {
    Lambdas.urls.size should be (3)
  }

  "rowPattern" should "extract rows from HTML" in {
    Lambdas.rowPattern.findAllIn(html).toList should be (List("<tr><td>snth</td><td>aoeu</td></tr>", "<tr>x</tr>"))
  }

  "cellPattern" should "extract cells text from HTML" in {
    Lambdas.cellPattern.findAllIn(html).toList should be (List("<td>snth</td>", "<td>aoeu</td>"))
  }

  "textPattern" should "extract cells' text from HTML" in {
    val firstRow = Lambdas.rowPattern.findAllIn(html).take(1)
    val cells = firstRow.map(Lambdas.cellPattern.findAllIn)
    val texts = cells.flatMap{
      _.flatMap(Lambdas.textPattern.findAllIn)
    }
    texts.toList should be (List(">snth<", ">aoeu<"))
  }

  "removeAngles" should "remove angles" in {
    List(">snth<", ">aoeu<").map(Lambdas.removeAngles) should be (List("snth", "aoeu"))
  }

  "getRows" should "parse the rows from the HTML" in {
    Lambdas.getRows(html).toList should be (List("<tr><td>snth</td><td>aoeu</td></tr>", "<tr>x</tr>"))
  }

  "getTextFieldsForRow" should "extract text from row cells" in {
    val row = "<tr><td>snth</td><td>aoeu</td></tr>"
    Lambdas.getTextFieldsForRow(row).toList should be (List("snth", "aoeu"))
  }

  "toJSON" should "convert iterator if iterators to JSON string" in {
    val in = List(List("a", "b").toIterator, List("c", "d").toIterator).toIterator
    Lambdas.toJSON(in) should be (List(List("a","b"),List("c","d")))
  }
}
