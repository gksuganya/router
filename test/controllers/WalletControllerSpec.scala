package controllers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsDefined, JsNumber, JsString}
import play.api.libs.ws._
import play.api.test.Helpers._

class WalletControllerSpec extends PlaySpec with OneServerPerSuite with WalletMock {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure("wallet.url" -> s"http://localhost:$walletPort").build

  private val ws = app.injector.instanceOf[WSClient]
  private val wiremock = new WireMockServer(9090)
  private val url = s"http://${s"localhost:$port"}"

  "getting wallet" should {
    "handle internal error" in {
      val response = await(ws.url(url + "/wallet").get())

      response.status mustBe BAD_GATEWAY
      response.json \ "message" mustBe JsDefined(JsString("bad gateway"))
    }
    "handle proxy error" in {
      stubFor(get(urlMatching("/wallets/0"))
        .willReturn(aResponse()
          .withStatus(502)))
      val response = await(ws.url(url + "/wallet").get())

      response.status mustBe BAD_GATEWAY
      response.json \ "message" mustBe JsDefined(JsString("bad gateway"))
    }
    "have balance" in {
      stubFor(post(urlMatching("/wallets"))
        .withHeader("Authorization", equalTo("Basic cm91dGVyOnJvdXRlcg=="))
        .willReturn(aResponse()
          .withStatus(201)
          .withHeader("Location", ",'.p.,/0")))

      stubFor(get(urlMatching("/wallets/0"))
        .withHeader("Authorization", equalTo("Basic cm91dGVyOnJvdXRlcg=="))
        .willReturn(aResponse()
          .withStatus(200)
          .withBody("{\"balance\": 0}")))

      val response = await(ws.url(url + "/wallet").get())

      response.status mustBe OK
      // TODO we should probably encrypt this
      response.header("Set-Cookie").get must include("WalletId")
      response.json \ "balance" mustBe JsDefined(JsNumber(0))
    }
  }
}
