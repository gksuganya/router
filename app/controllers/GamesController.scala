package controllers

import javax.inject._

import play.api.libs.ws.WSClient
import play.api.mvc._
import services.{GamesConfig, WalletConfig}

import scala.concurrent.ExecutionContext

@Singleton
class GamesController @Inject()(ws: WSClient, walletConfig: WalletConfig, gamesConfig: GamesConfig, implicit val context: ExecutionContext) extends Controller {
  def get(path: String): Action[AnyContent] = Action.async { request =>
    val id = walletId(request).getOrElse(throw new BadRequestException)
    ws.url(s"${gamesConfig.url}/games/$path")
      .withHeaders(
        "PlayerId" -> id,
        "Wallet" -> s"${walletConfig.url}/wallets/$id"
      )
      .get()
      .map { resp => if (resp.status == 200) Ok(resp.json) else BadGateway(ErrorFormatter.error("bad gateway")) }
  }

  def post(path: String): Action[AnyContent] = Action.async { request =>
    val id = walletId(request).getOrElse(throw new BadRequestException)

    ws.url(s"${gamesConfig.url}/games/$path")
      .withHeaders(
        "PlayerId" -> id,
        "Wallet" -> s"${walletConfig.url}/wallets/$id"
      )
      .post(request.body.asJson.get)
      .map { resp => if (resp.status == 201) Created(resp.json) else BadGateway(ErrorFormatter.error("bad gateway")) }
  }
}
