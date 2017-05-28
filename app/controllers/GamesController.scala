package controllers

import javax.inject._

import play.api.libs.ws.WSClient
import play.api.mvc._
import services.{GamesConfig, WalletConfig}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GamesController @Inject()(ws: WSClient, walletConfig: WalletConfig, gamesConfig: GamesConfig, implicit val context: ExecutionContext) extends Controller {
  def get(path: String): Action[AnyContent] = Action.async { request =>
    walletId(request) match {
      case Some(walletId) =>
        gameName(path) match {
          case Some(gameName) =>
            ws.url(s"http://$gameName:8080/api/games/$path")
              .withHeaders(
                "PlayerId" -> walletId,
                "Wallet" -> s"${walletConfig.url}/wallets/$walletId"
              )
              .get()
              .map {
                resp => if (resp.status < 500) Status(resp.status)(resp.json) else BadGateway(ErrorFormatter.error("bad gateway"))
              }
          case _ => Future.successful(BadRequest(ErrorFormatter.error("bad game")))
        }
      case _ => Future.successful(BadRequest(ErrorFormatter.error("no wallet ID")))
    }
  }

  private def gameName(path: String): Option[String] = {
    val gameName = path.replaceFirst("/.*", "")
    if (!gamesConfig.isValidGame(gameName))
      None
    else
      Some(gameName)
  }

  def post(path: String): Action[AnyContent] = Action.async { request =>
    walletId(request) match {
      case Some(walletId) =>
        gameName(path) match {
          case Some(gameName) =>
            ws.url(s"http://$gameName:8080/api/games/$path")
              .withHeaders(
                "PlayerId" -> walletId,
                "Wallet" -> s"${walletConfig.url}/wallets/$walletId"
              )
              .post(request.body.asJson.get)
              .map {
                resp => if (resp.status < 500) Status(resp.status)(resp.json) else BadGateway(ErrorFormatter.error("bad gateway"))
              }
          case _ => Future.successful(BadRequest(ErrorFormatter.error("bad game")))
        }
      case _ => Future.successful(BadRequest(ErrorFormatter.error("no wallet ID")))
    }
  }

}
