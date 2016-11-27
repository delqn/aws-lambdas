import scala.io.Source

object learn {

  val urls = Seq(
    "http://fcs001.xreflector.net/mitte.html",
    "http://fcs002.xreflector.net/mitte.html",
    "http://laboenligne.ca/fcs003/mitte.html"
  )

  Source.fromURL(urls(1)).toString.split("\<tr\>").toList

}