package controllers

import com.github.tomakehurst.wiremock.WireMockServer
import org.scalatest.{BeforeAndAfterAll, Suite}

trait WalletMock extends BeforeAndAfterAll {
  this: Suite =>
  val walletPort = 8080

  private val walletMock = new WireMockServer(walletPort)

  override def beforeAll(): Unit = walletMock.start()

  override def afterAll(): Unit = walletMock.stop()

}
