package example

import scala.io.Source

class Lambdas {
  def getFusionStations(): Iterator[Iterator[String]] = {
    val urls = Seq(
      "http://fcs001.xreflector.net/mitte.html",
      "http://fcs002.xreflector.net/mitte.html",
      "http://laboenligne.ca/fcs003/mitte.html"
    )

    val rowPattern = "<tr>(.*?)</tr>".r
    val textPattern = ">([a-zA-Z0-9 ]+)<".r
    def removeAngles(s: String) = s.filter(_ != '<').filter(_ != '>')
    def getCells(s: String) = textPattern.findAllIn(s).map(removeAngles)

    def getUrl(url: String): Iterator[Iterator[String]] = {
      // get the HTML source and remove new lines so regexes work correctly
      val html = Source.fromURL(url).mkString.filter(_ != '\n')
      rowPattern.findAllIn(html).map(getCells)
    }

    getUrl(urls(0))
  }
}
