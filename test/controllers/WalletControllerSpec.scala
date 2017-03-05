package controllers

import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.libs.json.{JsDefined, JsNumber}
import play.api.libs.ws._
import play.api.test.Helpers._

class WalletControllerSpec extends PlaySpec with OneServerPerSuite {
  private val ws = app.injector.instanceOf[WSClient]
  private val url = s"http://${s"localhost:$port"}"
  "getting wallet" should {
    "have balance" in {
      val response = await(ws.url(url +"/api/wallet").get())

      response.status mustBe OK
      response.json \ "balance" mustBe  JsDefined(JsNumber(0))
    }
  }
}
