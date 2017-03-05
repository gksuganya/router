package controllers

import javax.inject._

import play.api.libs.ws.{WSAuthScheme, WSClient}
import play.api.mvc._
import services.{AuthConfig, WalletConfig}

import scala.concurrent.ExecutionContext

@Singleton
class WalletController @Inject()(ws: WSClient, walletConfig: WalletConfig, authConfig: AuthConfig, implicit val context: ExecutionContext) extends Controller {
  def get: Action[AnyContent] = Action.async {
    ws.url(s"${walletConfig.url}/wallets/0")
      .withAuth(authConfig.username, authConfig.password, WSAuthScheme.BASIC)
      .get()
      .map { resp => Ok(resp.json) }
  }
}
