package controllers

import javax.inject._

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class WalletController @Inject()(ws: WSClient, config: Configuration, implicit val context: ExecutionContext) extends Controller {
  def get: Action[AnyContent] = Action.async {
    ws.url(s"${config.getString("wallet.url").get}/wallets/0").get().map { resp => Ok(resp.json) }
  }
}
