package controllers

import play.api.libs.json.{JsObject, JsString}

object ErrorFormatter {
  def error(message: String) = JsObject(Seq("message" -> JsString(message)))
}