package controllers

import javax.inject._

import play.api.mvc._


/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class VDMController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def showMeAVDM(id: String) = Action { implicit request: Request[AnyContent] =>
    Ok("hello world")
  }

  def showMeAllUrVDM() = Action { implicit request: Request[AnyContent] =>
    Ok("hello world")
  }

  def showMeAllUrVDMWithOptions(option: String) = Action { implicit request: Request[AnyContent] =>
    Ok("hello world"+option)
  }
}
