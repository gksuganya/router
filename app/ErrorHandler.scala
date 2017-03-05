import javax.inject._

import controllers.ErrorFormatter
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent._

@Singleton
class ErrorHandler @Inject()(
                              env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router]
                            ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {


  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    exception.printStackTrace()
    Future.successful(InternalServerError(ErrorFormatter.error("internal server error")))
  }

  override def onForbidden(request: RequestHeader, message: String): Future[Result] =
    Future.successful(Forbidden(ErrorFormatter.error(message)))

  override protected def onBadRequest(request: RequestHeader, message: String): Future[Result] =
    Future.successful(BadRequest(ErrorFormatter.error(message)))

  override protected def onNotFound(request: RequestHeader, message: String): Future[Result] =
    Future.successful(NotFound(ErrorFormatter.error(message)))
}

