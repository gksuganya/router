
package services

import javax.inject.{Inject, Singleton}

import play.api.Configuration

import scala.collection.JavaConversions._

@Singleton
class GamesConfig @Inject()(config: Configuration) {
  //noinspection ScalaUnusedSymbol
  private val games = config.getStringList("games.whitelist").get.toList

  def isValidGame(gameName: String): Boolean = games.contains(gameName)
}
