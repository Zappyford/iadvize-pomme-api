package api.post

import javax.inject.Inject

import controllers.VDMController
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class VDMRouter @Inject()(controller: VDMController) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/" ? q"from=$from" & q"to=$to") =>
      controller.showMeAllUrVDMFilteredByDates(from + to)

    case GET(p"/" ? q"author=$author") =>
      controller.showMeAllUrVDMFilteredByAuthor(author)

    case GET(p"/") =>
      controller.showMeAllUrVDM()

    case GET(p"/$id") =>
      controller.showMeAVDM(id)
  }

}