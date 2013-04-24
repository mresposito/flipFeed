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
        "description"-> text
    )
  )

  /*
   * add a new feedback page
   */
  def add = IsAuthenticated { username => implicit request =>
    User.findByEmail(username).map { user =>
      createFeedForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(html.users.index( user, user ))
        },
        feed => {
          val fd = Feed( Id(0), feed._1, feed._2, false, user.name  )
          Feed.create ( fd )
          Redirect(routes.Users.index( user.name ))
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
        Ok(html.feeds.index( user, feed ) )
      }.getOrElse(NotFound)
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
            Comment.create( Comment( Id(0), comment, "openQuest", post.id.get, poster.name ) )
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
