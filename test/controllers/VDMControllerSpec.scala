package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._


class VDMControllerSpec  extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "VDMController GET" should {

    "render the posts page from a controller" in {
      val controller = inject[VDMController]
      val posts = controller.showMeAllUrVDM().apply(FakeRequest(GET, "/api/posts/"))

      status(posts) mustBe OK
      contentType(posts) mustBe Some("text/plain")
      contentAsString(posts) must include("\"posts\"")
    }
  }

  "VDMController GET" should {

    "render a post page from a controller" in {
      val controller = inject[VDMController]
      val posts = controller.showMeAVDM("1").apply(FakeRequest(GET, "/api/posts/1"))

      status(posts) mustBe OK
      contentType(posts) mustBe Some("text/plain")
      contentAsString(posts) must include("\"post\"")
      contentAsString(posts) must include("\"count\"")
    }
  }

  "VDMController GET" should {

    "render a post page filter by his author from a controller" in {
      val controller = inject[VDMController]
      val posts = controller.showMeAllUrVDMFilteredByAuthor("Genius").apply(FakeRequest(GET, "/api/posts/"))

      status(posts) mustBe OK
      contentType(posts) mustBe Some("text/plain")
      contentAsString(posts) must include("\"posts\"")
    }
  }

  "VDMController GET" should {

    "render a post page filter by dates from a controller" in {
      val controller = inject[VDMController]
      val posts = controller.showMeAllUrVDMFilteredByDates("2017-01-01T00:00:00Z2017-12-31T00:00:00Z").apply(FakeRequest(GET, "/api/posts/"))

      status(posts) mustBe OK
      contentType(posts) mustBe Some("text/plain")
      contentAsString(posts) must include("\"posts\"")
    }
  }
}
