package services

import javax.inject.{Inject, Singleton}

import play.api.Configuration

@Singleton
class AuthService @Inject()(config: Configuration) {

  val username: String = config.getString("username").get
  val password: String = config.getString("password").get
}
