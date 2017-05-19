package controllers

import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsDefined, JsString}
import play.api.test.FakeRequest
import play.api.test.Helpers.{route, _}

class GamesControllerSpec extends PlaySpec with OneServerPerSuite with GameMock {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure("games.whitelist" -> List("localhost")).build

  private val url = s"http://${s"localhost:$port"}"

  "game" should {

    "handle proxy error" in {
      stubFor(get(urlMatching("/api/games/localhost"))
        .willReturn(aResponse()
          .withStatus(500)))

      val request = FakeRequest(GET, "/api/games/localhost").withSession("WalletId" -> "0")

      val response = route(app, request).get

      status(response) mustBe BAD_GATEWAY
      contentAsJson(response) \ "message" mustBe JsDefined(JsString("bad gateway"))
    }

    "get game" in {
      stubFor(get(urlMatching("/api/games/localhost"))
        .withHeader("PlayerId", equalTo("0"))
        .withHeader("Wallet", equalTo("http://wallet:8080/wallets/0"))
        .willReturn(aResponse()
          .withStatus(200)
          .withBody("{\"foo\": \"bar\"}")))

      val request = FakeRequest(GET, "/api/games/localhost").withSession("WalletId" -> "0")

      val response = route(app, request).get

      status(response) mustBe OK
      contentAsJson(response) \ "foo" mustBe JsDefined(JsString("bar"))
    }

    "create game event" in {
      stubFor(post(urlMatching("/api/games/localhost"))
        .withHeader("PlayerId", equalTo("0"))
        .withHeader("Wallet", equalTo("http://wallet:8080/wallets/0"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withRequestBody(equalTo("{\"foo\":\"bar\"}"))
        .willReturn(aResponse()
          .withStatus(201)
          .withBody("{\"baz\":\"qux\"}"))
      )
      val request = FakeRequest(POST, "/api/games/localhost").withSession("WalletId" -> "0")
        .withHeaders("Content-Type" -> "application/json")
        .withBody("{\"foo\":\"bar\"}")

      val response = route(app, request).get

      status(response) mustBe CREATED
      contentAsJson(response) \ "baz" mustBe JsDefined(JsString("qux"))
    }
  }

}
