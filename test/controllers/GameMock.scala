package controllers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.scalatest.{BeforeAndAfterAll, Suite}

trait GameMock extends BeforeAndAfterAll {
  this: Suite =>
  val gamePort = 8080

  private val gameMock = new WireMockServer(gamePort)

  override def beforeAll(): Unit = gameMock.start()

  override def afterAll(): Unit = {
    WireMock.reset()
    gameMock.stop()
  }

}
