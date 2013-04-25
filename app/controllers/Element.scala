package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models._
import views._

import play.api.libs.json._

object Elements extends Controller with Secured {

  def add = Action(parse.json) { request =>
    (request.body).asOpt[Map[String,String]].map { json =>
      Ok( Json.toJson( Map ( 
        "id" ->  Element.create( Element(
          Id(0), json("feedId").toLong, json("name"),
          json("description"), json("kind"), json("attr")
          ) ).id.get
       ) ) )
    }.getOrElse(BadRequest( "Not valid format "))
  }

  /*
   * serve all the feeds by ID in JSON
   */
  def findByFeed ( feedId: Long ) = Action {
    Ok(
      Json.toJson( Element.findByFeed ( feedId ).map { el =>
        Map (
          "id"   -> el.id.get.toString , "feedId"      -> el.feedId.toString ,
          "name" -> el.name           , "description" -> el.description      ,
          "kind" -> el.kind           , "attr"        -> el.attr
        )
      } ) )
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
   * logic to comment on a certain post
   */
  def comment = Action (parse.json) { request =>
    (request.body).asOpt[Map[String,String]].map { json =>
      Ok( Json.toJson( Map (
        "id" ->  Comment.create( Comment(
          Id(0)               , json("value") , json("kind") ,
          json("form").toLong , json("author")
          ) ).id.get
       ) ) )
    }.getOrElse(BadRequest( "Not valid format "))
  }


  def delete ( id:Long ) = IsAuthenticated { username => _ => 
    User.findByEmail( username ).map { user => {
        Element.delete( id ) 
        Redirect( routes.Users.index( user.name) )
      }
    }.getOrElse(Forbidden)
  }
  /*def getResultSet( feed:Long ) = ResultSet( feed )*/
}
