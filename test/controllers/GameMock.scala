package controllers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, Suite}

trait GameMock extends BeforeAndAfterAll with BeforeAndAfter {
  this: Suite =>
  val gamePort = 8080

  private val gameMock = new WireMockServer(gamePort)

  override def beforeAll(): Unit = gameMock.start()

  before {
    WireMock.reset()
  }

  override def afterAll(): Unit = gameMock.stop()

}
