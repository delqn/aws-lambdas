package com.delqn.aws

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
  def toJSON(in: Iterator[Iterator[String]]): List[List[String]] = {
    in.map(list => list.map(_.toString).toList).toList
  }
}

class Lambdas {
  def getFusionStations: List[List[String]] = {
    // TODO(delyan): go through the list of URLs and ++ them
    // TODO(delyan): what's the expected return type for AWS Lambda?
    Lambdas.toJSON(Lambdas.getUrl(Lambdas.urls.head))
  }
}
