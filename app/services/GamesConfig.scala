
package services

import javax.inject.{Inject, Singleton}

import play.api.Configuration

@Singleton
class GamesConfig @Inject()(config: Configuration) {

  val url: String = config.getString("games.url").get

}
