package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Comment(  id:Pk[Long], text:String, anon:Boolean, feed:Long, author:String )

object Comment extends DatabaseItem {

  // - Inheritance properties
  type T    = Comment
  def table = "cmment"

  def singleResult( set:SimpleSql[Row] ): Option[Comment] = 
    DB.withConnection { implicit connection =>
      set.as(Comment.simple.singleOpt)
    }

  def multipleResults( set:SimpleSql[Row] ): Seq[Comment] = 
    DB.withConnection { implicit connection =>
      set.as(Comment.simple * )
    }
  
  
  // - Parsers

  /*
   * Parses a feed from result set
   */
  val simple = {
    get[Pk[Long]]("cmment.id") ~
    get[String]("cmment.text") ~
    get[Boolean]("cmment.anon") ~
    get[Long]("cmment.feed") ~
    get[String]("cmment.author") map {
      case id~text~anon~feed~author => 
        Comment( id, text, anon, feed, author )
    }
  }

  // - Queries

  def findByFeed( id       : Long   ) : Seq[Comment] = findSeqByAttr ( "feed", id )
  def findByAuthor( user   : User   ) : Seq[Comment] = findByAuthor( user.name )
  def findByAuthor( author : String ) : Seq[Comment] = findSeqByAttr ( "author", author )

  /*
   * update the text project
   */
  def edit( id:Pk[Long], newText:String ) {
    DB.withConnection{ implicit connection =>
      SQL("update cmment set text = {newText} where id = {id}").on(
        'id -> id, 'newText-> newText
      ).executeUpdate()
    }
  }

  def create( comment:Comment ): Comment = {
    DB.withTransaction{ implicit connection =>

      //  get new id
      val id: Long = 
        SQL("select next value for cmment_seq").as(scalar[Long].single)

      // insert new feedback container
      SQL(
        """ 
          insert into cmment values (
            {id}, {text}, {anon}, {feed}, {author} 
          )
        """
      ).on(
        'id->id,  'text-> comment.text, 'anon-> comment.anon,
        'feed-> comment.feed, 'author-> comment.author 
      ).executeUpdate()

      comment.copy( id=Id(id) )
    }
  }
}
