package controllers

import javax.inject._

import models.JsonFormats._
import models.VDMRepository
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class VDMController @Inject()(cc: ControllerComponents, vDMRepository: VDMRepository) extends AbstractController(cc) {

  /**
    * Show a VDM from the database
    * @param id of the VDM
    * @return a JSON representation of the post
    */
  def showMeAVDM(id: String) = Action.async { implicit request: Request[AnyContent] =>
    vDMRepository.getAVDM(id.toInt).map { maybePost =>
      maybePost.map { post =>
        Ok(Json.prettyPrint(Json.toJson(
          Json.obj(
          "post" -> post,
          "count" -> 1
          )
        )))
      }.getOrElse(NotFound)
    }
  }

  /**
    * Return all the posts
    * @return a JSON representation of the posts in the database
    */
  def showMeAllUrVDM() = Action.async { implicit request: Request[AnyContent] =>
    vDMRepository.getAllTheVDM().map { posts =>
      Ok(Json.prettyPrint(Json.toJson(
        Json.obj(
          "post" -> posts
        )
      )))
    }
  }

  /**
    * Show VDMs from the database
    * @param option the filter we want to apply
    * @return a JSON representation of the posts filtered
    */
  def showMeAllUrVDMWithOptions(option: String) = Action { implicit request: Request[AnyContent] =>
    Ok("Hello world" + option)
  }
}
