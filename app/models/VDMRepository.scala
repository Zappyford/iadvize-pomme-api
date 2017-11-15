package models

import javax.inject.Inject

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{MongoDriver, ReadPreference}
import reactivemongo.bson.{BSONArray, BSONDocument, BSONDocumentReader, BSONDocumentWriter}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Class to represent a VDM post
  *
  * @param id
  * @param content
  * @param date
  * @param author
  */
case class VDM(id: Int, content: String, date: String, author: String)

/**
  * The companion object where we override the definitions of the methods read and write,
  * with the goal to implicitly put a VDM in the database or read posts from the database
  */
object VDM {


  implicit object VDMReader extends BSONDocumentReader[VDM] {
    def read(doc: BSONDocument): VDM = {
      val id = doc.getAs[Int]("id").get
      val content = doc.getAs[String]("content").get
      val date = doc.getAs[String]("date").get
      val author = doc.getAs[String]("author").get

      VDM(id, content, date, author)
    }
  }

  implicit object VDMWriter extends BSONDocumentWriter[VDM] {
    def write(vdm: VDM): BSONDocument = {
      BSONDocument(
        "id" -> vdm.id,
        "content" -> vdm.content,
        "date" -> vdm.date,
        "author" -> vdm.author
      )
    }
  }

}

/**
  * Object that override OFormat of Json in order to translate VDM posts to Json
  */
object JsonFormats {

  import play.api.libs.json._

  implicit val VDMFormat: OFormat[VDM] = Json.format[VDM]
}

/**
  * The class that contains all the methods to read the VDM posts
  *
  * @param ec
  */
class VDMRepository @Inject()(implicit ec: ExecutionContext) {

  /**
    * Get 200 first VDM posts in the database
    *
    * @return a sequence of posts
    */
  def getAllTheVDM(): Future[Seq[VDM]] = {
    val query = BSONDocument()
    vdmCollection.flatMap(_.find(query).cursor[VDM](ReadPreference.primary).
      collect[Seq](200))
  }

  /**
    * Accessor to the posts collection from the database
    *
    * @return a collection from the database
    */
  def vdmCollection: Future[BSONCollection] = MongoDriver().connection(List("localhost")).database("vdmposts").map(_.collection("posts"))

  /**
    * Return the post from the given id
    *
    * @param id
    * @return the post
    */
  def getAVDM(id: Int): Future[Option[VDM]] = {
    val query = BSONDocument("id" -> id)
    vdmCollection.flatMap(_.find(query).one[VDM])
  }

  /**
    * Return posts filtered by author or dates
    *
    * @param author
    * @return the post
    */
  def getFilteredVDMByAuthor(author: String): Future[Seq[VDM]] = {
    val query = BSONDocument("author" -> author)
    vdmCollection.flatMap(_.find(query).cursor[VDM](ReadPreference.primary).
      collect[Seq]())
  }

  /**
    * Return posts filtered by author or dates
    *
    * @param dates
    * @return the post
    */
  def getFilteredVDMByDates(dates: String): Future[Seq[VDM]] = {
    val regexDate = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z".r
    val filters = regexDate.findAllIn(dates).toList
    val query = BSONDocument("$and" ->
      BSONArray(BSONDocument("date" -> BSONDocument("$gte" -> filters(0)))
      , BSONDocument("date" -> BSONDocument("$lte" -> filters(1))))
    )
    vdmCollection.flatMap(_.find(query).cursor[VDM](ReadPreference.primary).
      collect[Seq]())
  }

}
