package controllers

import javax.inject._

import play.api.libs.ws.WSClient
import play.api.mvc._
import services.{GamesConfig, WalletConfig}

import scala.concurrent.ExecutionContext

@Singleton
class GamesController @Inject()(ws: WSClient, walletConfig: WalletConfig, gamesConfig: GamesConfig, implicit val context: ExecutionContext) extends Controller {
  def get(path: String): Action[AnyContent] = Action.async {
    ws.url(s"${gamesConfig.url}/games/$path")
      .withHeaders(
        "PlayerId" -> "0",
        "Wallet" -> s"${walletConfig.url}/wallets/0"
      )
      .get()
      .map { resp => Ok(resp.json) }
  }
}
