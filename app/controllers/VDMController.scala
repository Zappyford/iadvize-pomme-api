package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import scrapper.VDMScrapper


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class VDMController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def showMeAVDM(id: String) = Action { implicit request: Request[AnyContent] =>
    Ok("Hello world")
  }

  def showMeAllUrVDM() = Action { implicit request: Request[AnyContent] =>
    val VDMScrapper = new VDMScrapper
    Ok(Json.prettyPrint(VDMScrapper.scrapeVDM(200)))
  }

  def showMeAllUrVDMWithOptions(option: String) = Action { implicit request: Request[AnyContent] =>
    Ok("Hello world"+option)
  }
}
