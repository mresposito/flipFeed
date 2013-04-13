package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

object Elements extends Controller with Secured {

  val createElement = Form (
    tuple (
      "feedId" -> nonEmptyText,
      "kind" -> nonEmptyText,
      "attrb" -> nonEmptyText
    )
  )
  /*
   * add a new feedback page
   */
  def add = IsAuthenticated { username => implicit request =>
    createElement.bindFromRequest.fold(
      formWithErrors => BadRequest,
      element => {
        val  el = Element( Id(0), element._1.toLong, element._2, element._3  )
        Element.create ( el )
        Redirect(routes.Users.profile)
      }
    )
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
}
