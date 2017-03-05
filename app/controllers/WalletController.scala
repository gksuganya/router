package controllers

import javax.inject._

import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class WalletController @Inject()(ws: WSClient, implicit val context: ExecutionContext) extends Controller {
  def get: Action[AnyContent] = Action.async {
    ws.url("http://localhost:9090/wallets/0").get().map { resp => Ok(resp.json) }
  }
}
