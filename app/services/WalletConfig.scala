package services

import javax.inject.{Inject, Singleton}

import play.api.Configuration

@Singleton
class WalletConfig @Inject()(config: Configuration) {

  val url: String = config.getString("wallet.url").get

}
