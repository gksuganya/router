import play.api.mvc.Request

package object controllers {
  type WalletId = String
  def walletId(request:Request[_]):Option[WalletId] = {
    request.session.get("WalletId")
  }
}
