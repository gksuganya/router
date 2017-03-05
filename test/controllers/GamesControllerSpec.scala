package controllers

import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsDefined, JsString}
import play.api.libs.ws._
import play.api.test.Helpers._

class GamesControllerSpec extends PlaySpec with OneServerPerSuite with GameMock {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure("games.url" -> s"http://localhost:$gamePort").build

  private val ws = app.injector.instanceOf[WSClient]
  private val url = s"http://${s"localhost:$port"}"

  "game" should {
    "get game" in {
      configureFor(gamePort)
      stubFor(get(urlMatching("/games/mock"))
        .withHeader("PlayerId", equalTo("0"))
        .withHeader("Wallet", equalTo("http://wallet:8080/wallets/0"))
        .willReturn(aResponse()
          .withStatus(200)
          .withBody("{\"foo\": \"bar\"}")))

      val response = await(ws.url(url + "/games/mock").get())

      response.status mustBe OK
      response.json \ "foo" mustBe JsDefined(JsString("bar"))
    }
  }
}
