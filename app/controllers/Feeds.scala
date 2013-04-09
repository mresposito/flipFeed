package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Feeds extends Controller with Secured {
  /*
  * display the list of all feeds
  */

  val createFeedForm = Form (
    tuple(
        "name"  -> nonEmptyText,
        "description"-> text,
        "anon"-> text
    )
  )

  /*
   * display user's dashboard
   */
  def index = IsAuthenticated { username => _ => 
    User.findByEmail(username).map { user =>
      Ok(
        html.dashboard(user, Feed.findByAuthor(user), createFeedForm)
      )
    }.getOrElse(Forbidden)
  }

  /*
   * add a new feeodback page
   */
  def add = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      createFeedForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(html.dashboard( user, Feed.findByAuthor(user), formWithErrors ))
        },
        feed => {
          val fd = Feed( Id(0), feed._1, feed._2, false, user.name  )
          Feed.create ( fd )
          Redirect(routes.Feeds.index)
        }
      )
    }.getOrElse(Forbidden)
  }

  /*
   * display a particular feed
   */
  def display( owner: String, name:String ) = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      Feed.findByOwnerName( owner, name ).map { feed =>
        Ok(html.feeds.index( user, feed, Comment.findByFeed ( feed.id.get ) ) )
      }.getOrElse(NotFound)
    }.getOrElse(Forbidden)
  }

  /*
   * browse people's profile. If the name is the same as in the session, then
   * redirect do dashboard.
   */

  def browseProfile( name:String ) = IsAuthenticated { username => _ =>
    User.findByEmail( username ). map { user =>
      if ( user.name == name ) { // redirect to normal dashboard
        Redirect(routes.Feeds.index)
      }
      else { // find user's profile and render it.
        User.findByName( name ). map { profile =>
          Ok( html.dashboard( profile, Feed.findByAuthor( profile ), createFeedForm ) )
        }.getOrElse(NotFound)
      }
    }.getOrElse(Forbidden)
  }

  val commentOnForm = Form (
    tuple(
      "comment" -> text,
      "anon" ->text
    )
  )
  /*
   * logic to comment on a certain post
   */
  def commentOn( owner:String, feed:String ) = IsAuthenticated { username => implicit request =>
    User.findByEmail( username ).map { poster =>
      Feed.findByOwnerName( owner, feed ).map { post =>
      Form("comment"->nonEmptyText).bindFromRequest.fold(
          errors  => BadRequest,
          comment => { // register che comment
            Comment.create( Comment( Id(0), comment, false, post.id.get, poster.name ) )
            Redirect( routes.Feeds.display( owner, feed ) )
          }
        )
      }.getOrElse(NotFound)
    }.getOrElse(NotFound)
  }

  /*
   * check if a name is valid
   */
  def validName (owner:String) = Action { implicit request =>
    Form("name" -> nonEmptyText).bindFromRequest.fold ( 
      errors =>  BadRequest("Not valid"),
      name => {
        if( Feed.findByOwnerName ( owner, name ).isDefined ) {
          BadRequest("Name taken")
        } else {
          Ok
        }
      }
    )
  }
}
