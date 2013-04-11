package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Users extends Controller with Secured {

  val createFeedForm = Form (
    tuple(
        "name"  -> nonEmptyText,
        "description"-> text,
        "anon"-> text
    )
  )

  /*
   * display user's profile
   */
  def index ( name:String ) = IsAuthenticated { username => _ => 
    User.findByEmail(username).map { user =>
      Ok(
        html.users.index(user, Feed.findByAuthor(user), createFeedForm)
      )
    }.getOrElse(Forbidden)
  }

  def profile  =  TODO

/**/
/*  /**/
/*   * browse people's profile. If the name is the same as in the session, then*/
/*   * redirect do dashboard.*/
/*   */*/
/*  def browseProfile( name:String ) = IsAuthenticated { username => _ =>*/
/*    User.findByEmail( username ). map { user =>*/
/*      if ( user.name == name ) { // redirect to normal dashboard*/
/*        Redirect(routes.Feeds.index)*/
/*      }*/
/*      else { // find user's profile and render it.*/
/*        User.findByName( name ). map { profile =>*/
/*          Ok( html.dashboard( profile, Feed.findByAuthor( profile ), createFeedForm ) )*/
/*        }.getOrElse(NotFound)*/
/*      }*/
/*    }.getOrElse(Forbidden)*/
/*  }*/
}
