package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Users extends Controller with Secured {

  /*
   * display user's profile
   */
  def index ( name:String ) = IsAuthenticated { username => _ => 
    User.findByEmail(username).map { user =>
      if ( name == user.name ) { // the user owns the page
        Ok( html.users.index(user, user ) )
      } else { // find the owner of the page
        User.findByName(name).map { owner =>
          Ok( html.users.index(user, owner ) )
        }.getOrElse(NotFound)
      }
    }.getOrElse(Forbidden)
  }

  def profile  =  IsAuthenticated { username => _ => 
    User.findByEmail(username).map { user =>
      Redirect( routes.Users.index( user.name ) )
    }.getOrElse(Redirect(routes.Auth.login))
  }
}
