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

  private val cookie = "PLAY_SESSION=3b07e0d19797703cea79aaacc07db66a8cf715c0-WalletId=0; Path=/; HTTPOnly"
  "game" should {
    "handle internal error" in {
      val response = await(ws.url(url + "/games/mock").withHeaders("Cookie" -> cookie).get())

      response.status mustBe INTERNAL_SERVER_ERROR
      response.json \ "message" mustBe JsDefined(JsString("internal server error"))
    }
    "handle proxy error" in {
      stubFor(get(urlMatching("/wallets/0"))
        .willReturn(aResponse()
          .withStatus(500)))
      val response = await(ws.url(url + "/wallet").withHeaders("Cookie" -> cookie).get())

      response.status mustBe INTERNAL_SERVER_ERROR
      response.json \ "message" mustBe JsDefined(JsString("internal server error"))
    }
    "get game" in {
      stubFor(get(urlMatching("/games/mock"))
        .withHeader("PlayerId", equalTo("0"))
        .withHeader("Wallet", equalTo("http://wallet:8080/wallets/0"))
        .willReturn(aResponse()
          .withStatus(200)
          .withBody("{\"foo\": \"bar\"}")))

      val response = await(ws.url(url + "/games/mock").withHeaders("Cookie" -> cookie).get())

      response.status mustBe OK
      response.json \ "foo" mustBe JsDefined(JsString("bar"))
    }

    "create game event" in {
      stubFor(post(urlMatching("/games/mock"))
        .withHeader("PlayerId", equalTo("0"))
        .withHeader("Wallet", equalTo("http://wallet:8080/wallets/0"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withRequestBody(equalTo("{\"foo\":\"bar\"}"))
        .willReturn(aResponse()
          .withStatus(201)
          .withBody("{\"baz\":\"qux\"}"))
      )

      val response = await(ws.url(url + "/games/mock")
        .withHeaders("Content-Type" -> "application/json")
        .withHeaders("Cookie" -> cookie)
        .post("{\"foo\":\"bar\"}"))

      response.status mustBe CREATED
      response.json \ "baz" mustBe JsDefined(JsString("qux"))
    }
  }

}
