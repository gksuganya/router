package controllers

import javax.inject._

import play.api.libs.ws.WSClient
import play.api.mvc._
import services.{GamesConfig, WalletConfig}

import scala.concurrent.ExecutionContext

@Singleton
class GamesController @Inject()(ws: WSClient, walletConfig: WalletConfig, gamesConfig: GamesConfig, implicit val context: ExecutionContext) extends Controller {
  def getAsset(path: String): Action[AnyContent] = get("assets", path)

  def get(base: String = "api", path: String): Action[AnyContent] = Action.async { request =>
    val id = walletId(request).getOrElse(throw new BadRequestException("no wallet id"))
    val gameName = game(path)
    if (!gamesConfig.isValidGame(gameName)) {
      BadRequest(ErrorFormatter.error("bad game"))
    }
    ws.url(s"http://$gameName:8080/$base/games/$path")
      .withHeaders(
        "PlayerId" -> id,
        "Wallet" -> s"${walletConfig.url}/wallets/$id"
      )
      .get()
      .map { resp => if (resp.status < 500) Status(resp.status)(resp.json) else BadGateway(ErrorFormatter.error("bad gateway")) }
  }

  def post(path: String): Action[AnyContent] = Action.async { request =>
    val id = walletId(request).getOrElse(throw new BadRequestException("no wallet id"))
    val gameName = game(path)
    if (!gamesConfig.isValidGame(gameName)) {
      BadRequest(ErrorFormatter.error("bad game"))
    }

    ws.url(s"http://$gameName:8080/api/games/$path")
      .withHeaders(
        "PlayerId" -> id,
        "Wallet" -> s"${walletConfig.url}/wallets/$id"
      )
      .post(request.body.asJson.get)
      .map { resp => if (resp.status < 500) Status(resp.status)(resp.json) else BadGateway(ErrorFormatter.error("bad gateway")) }
  }

  private def game(path: String) = {
    path.replaceFirst("/.*", "")
  }
}
