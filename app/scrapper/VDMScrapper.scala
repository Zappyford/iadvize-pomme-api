package scrapper

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneOffset}

import models.VDM
import org.jsoup.Jsoup
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * The scrapper
  */
class VDMScrapper {

  private val postDateTimeFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy kk:mm")

  /**
    * Scrapes the number of VDM and put it in the database
    * @param total the numbers of posts we want to scrape
    */
  def scrapeVDM(total: Int): Unit = {

    val vdmCollection: Future[BSONCollection] = MongoDriver().connection(List("localhost")).database("vdmposts").map(_.collection("posts"))

    var page = 1
    var count = 0
    var id = 0
    while (count < total) {
      val scrapedArticles = Jsoup.connect(s"http://www.viedemerde.fr/?page=" + page)
        .userAgent("Mozilla")
        .timeout(500000)
        .get()
        .select("p.block")
        .select("p.hidden-xs")
        .select("a[href*=/article/]")
        .toArray()
        .take(total - count)
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

        vdmCollection.flatMap(_.insert(
          VDM(id,
            content.toString,
            LocalDateTime.parse(date, postDateTimeFormatter).toInstant(ZoneOffset.UTC).toString,
            author)
        ))
        id += 1
      }
      page += 1
      count += scrapedArticles.length
    }
  }

}

/**
  * The companion object of the scrapper
  */
object VDMScrapper {

  /**
    * The main function to scrap the VDM posts
    * @param args
    */
  def main(args: Array[String]) = {
    val VDMScrapper = new VDMScrapper
    println("Scrapping...")
    VDMScrapper.scrapeVDM(10)
    println("End of scrapping")
    System.exit(0)
  }
}