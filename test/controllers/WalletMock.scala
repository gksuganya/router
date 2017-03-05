package controllers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Suite}

trait WalletMock extends BeforeAndAfterAll with BeforeAndAfter {
  this: Suite =>
  val walletPort = 8080

  private val walletMock = new WireMockServer(walletPort)

  override def beforeAll(): Unit = walletMock.start()

  before {
    WireMock.configureFor(walletPort)
    WireMock.reset()
  }

  override def afterAll(): Unit = walletMock.stop()

}
