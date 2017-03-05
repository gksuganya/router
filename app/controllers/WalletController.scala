package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class WalletController @Inject extends Controller {
  def get = Action {
    Ok(Json.parse("{\"balance\": 0}"))
  }
}
