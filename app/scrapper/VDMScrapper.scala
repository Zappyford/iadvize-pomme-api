package scrapper

import java.time.{Instant, LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter

import org.jsoup.Jsoup
import play.api.libs.json.{JsObject, JsValue, Json, Writes}

import scala.collection.mutable.ArrayBuffer

class VDMScrapper {

  private val postDateTimeFormatter =DateTimeFormatter.ofPattern("EEEE d MMMM yyyy kk:mm")

  def scrapeVDM(total: Int) = {
    var page = 1
    var count = 0
    val jsonBuffer = new ArrayBuffer[JsObject]
    var id = 1
    while (count < total) {
      var scrapedArticles = Jsoup.connect(s"http://www.viedemerde.fr/?page=" + page)
        .userAgent("Mozilla")
        .timeout(500000)
        .get()
        .select("p.block")
        .select("p.hidden-xs")
        .select("a[href*=/article/]")
        .toArray()
        .take(total-count)
        .map(m => m.toString.substring(m.toString.indexOf("/article"), m.toString.indexOf("html") + 4))

      for (e <- scrapedArticles) {
        val scrapeArticle = Jsoup.connect(s"http://www.viedemerde.fr" + e)
          .userAgent("Mozilla")
          .timeout(500000)
          .get()
        val author = scrapeArticle
          .select("meta[name=author]")
          .attr("content")

        val content = scrapeArticle
          .select("meta[name=description]")
          .attr("content")

        val date = scrapeArticle
          .select("div.panel-body")
          .select("div.text-center")
          .text()
          .split(" / ")(1)

        jsonBuffer.append(Json.obj(
          "id" -> id,
          "content" -> content,
          //"date" -> DateTimeFormatter.ISO_INSTANT.format(postDateTimeFormatter.parse(date)),
          "date" -> LocalDateTime.parse(date,postDateTimeFormatter).toInstant(ZoneOffset.UTC),
          "author" -> author
        ))
        id += 1
      }
      page += 1
      count += scrapedArticles.length
    }
    Json.obj(
      "posts" -> jsonBuffer
    )
  }
}

object VDMScrapper {

  def main(args: Array[String]): Unit = {
    val VDMScrapper = new VDMScrapper
    val json = VDMScrapper.scrapeVDM(20)
    print(Json.prettyPrint(json))
  }
}