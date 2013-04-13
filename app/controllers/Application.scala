package controllers

import play.api._
import play.api.mvc._

import views._
import models._

object Application extends Controller {
  
  def index = Action {
     Ok(views.html.index("Welcome to Flipfeed"))
  }

  // -- Javascript routing

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes") (
        Feeds.add, Feeds.validName,
        Elements.add
      )
    ).as("text/Javascript")
  }
}

/***
* Secutiry traits
*/

trait Secured {
  /*
  * get username
  */

  private def username( request: RequestHeader ) = request.session.get("email")

  /*
  * Redirect to login if not authorized
  */

  private def onUnauthorized( request:RequestHeader ) = Results.Redirect(routes.Auth.login)


  /*
   * Make action for secured user
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }
}
