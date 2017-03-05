package controllers

import javax.inject._

import play.api.Configuration
import play.api.libs.ws.{WSAuthScheme, WSClient}
import play.api.mvc._
import services.AuthService

import scala.concurrent.ExecutionContext

@Singleton
class WalletController @Inject()(ws: WSClient, config: Configuration, auth: AuthService, implicit val context: ExecutionContext) extends Controller {
  def get: Action[AnyContent] = Action.async {
    ws.url(s"${config.getString("wallet.url").get}/wallets/0")
      .withAuth(auth.username, auth.password, WSAuthScheme.BASIC)
      .get()
      .map { resp => Ok(resp.json) }
  }
}
