package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Comment(  id:Pk[Long], text:String, anon:Boolean, feed:Long, author:String )

object Comment {
  
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

  def findById(id:Long    ): Option[Comment] = findById( Id(id) )

  def findById(id:Pk[Long]): Option[Comment] = {
    DB.withConnection { implicit connection =>
      SQL("select * from cmment where id = {id}").on(
        'id -> id
      ).as(Comment.simple.singleOpt)
    }
  }

  def findAll: Seq[Comment] = {
    DB.withConnection { implicit connection =>
        SQL( " select * from cmment ").as(Comment.simple *)
    }
  }

  def findByFeed( id:Long   ): Seq[Comment] = {
      DB.withConnection { implicit connection =>
        SQL(
          """
          select * from cmment
          where feed = {id}
          """
        ).on(
        'id -> id
      ).as(Comment.simple *)
    }
  }

  def findByAuthor( user:User   ): Seq[Comment] = findByAuthor( user.name )

  def findByAuthor( author:String ): Seq[Comment] = {
      DB.withConnection { implicit connection =>
        SQL(
          """
          select * from cmment
          where author = {author}
          """
        ).on(
        'author -> author
      ).as(Comment.simple *)
    }
  }

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

  def delete( id:Long ) = {
    DB.withConnection{ implicit connection =>
      SQL("delete from cmment where id = {id}").on(
        'id-> id
      ).executeUpdate()
    }
  }
}
