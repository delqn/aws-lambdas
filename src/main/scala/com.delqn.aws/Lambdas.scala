package com.delqn.aws

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.matching.Regex

object Lambdas {
  val urls: Seq[String] = Seq(
    "http://fcs001.xreflector.net/mitte.html",
    "http://fcs002.xreflector.net/mitte.html",
    "http://laboenligne.ca/fcs003/mitte.html"
  )
  val rowPattern: Regex = "<tr>(.*?)</tr>".r
  val cellPattern: Regex = "<td(.*?)</td>".r
  val textPattern: Regex = ">([a-zA-Z0-9 ]+)<".r

  def removeAngles(s: String): String = s.filter(_ != '<').filter(_ != '>')
  def getTextFieldsForRow(row: String): Iterator[String] = {
    val cells = Lambdas.cellPattern.findAllIn(row)
    cells.flatMap(Lambdas.textPattern.findAllIn).map(removeAngles)
  }
  def getRows(html: String): Iterator[String] = rowPattern.findAllIn(html)
  def getUrl(url: String): Iterator[Iterator[String]] = {
    // get the HTML source and remove new lines so regexes work correctly
    val html = Source.fromURL(url).mkString.filter(_ != '\n')
    getRows(html).map(getTextFieldsForRow)
  }
  implicit def toJavaList(in: List[TraversableOnce[String]]): java.util.List[java.util.List[String]] = {
    // AWS Lambda expects java.util.List !!!
    in.map(list => list.map(_.toString).toList.asJava).asJava
  }
}

import Lambdas._

class Lambdas {
  def getFusionStations: java.util.List[java.util.List[String]] = {
    // Fetch all URLs in parallel
    // Convert each fetched HTML table into List of Lists
    // Combine all tables into one list of lists
    urls.par.map(getUrl).scanLeft(List.empty[TraversableOnce[String]]){ case (a, b) => a ++ b }.reverse.head
  }
}
