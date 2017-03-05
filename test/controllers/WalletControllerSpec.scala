package controllers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsDefined, JsNumber}
import play.api.libs.ws._
import play.api.test.Helpers._

class WalletControllerSpec extends PlaySpec with OneServerPerSuite with BeforeAndAfterAll {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure("wallet.url" -> "http://localhost:9090").build

  private val ws = app.injector.instanceOf[WSClient]
  private val wiremock = new WireMockServer(9090)
  private val url = s"http://${s"localhost:$port"}"

  override def beforeAll(): Unit = wiremock.start()

  override def afterAll(): Unit = wiremock.stop()

  "getting wallet" should {
    "have balance" in {
      configureFor(wiremock.port())
      stubFor(get(urlMatching("/wallets/0"))
        .withHeader("Authorization", equalTo("Basic cm91dGVyOnJvdXRlcg=="))
        .willReturn(aResponse()
          .withStatus(200)
          .withBody("{\"balance\": 0}")))


      val response = await(ws.url(url + "/api/wallet").get())

      response.status mustBe OK
      response.json \ "balance" mustBe JsDefined(JsNumber(0))
    }
  }
}
