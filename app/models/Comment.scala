package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Comment(  id:Pk[Long], value:String, kind:String, form:Long, author:String )

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
   * Parses a form from result set
   */
  val simple = {
    get[Pk[Long]]("cmment.id") ~
    get[String]("cmment.value") ~
    get[String]("cmment.kind") ~
    get[Long]("cmment.elementId") ~
    get[String]("cmment.author") map {
      case id~text~kind~form~author => 
        Comment( id, text, kind, form, author )
    }
  }

  // - Queries

  def findByFeed( id       : Long   ) : Seq[Comment] = findSeqByAttr ( "elementId", id )
  def findByElement( id       : Long   ) : Seq[Comment] = findSeqByAttr ( "elementId", id )
  def findByAuthor( user   : User   ) : Seq[Comment] = findByAuthor( user.name )
  def findByAuthor( author : String ) : Seq[Comment] = findSeqByAttr ( "author", author )

  /*
   * update the text in the comment
   */
  def edit( id:Pk[Long], newText:String ) {
    DB.withConnection{ implicit connection =>
      SQL("update cmment set value = {newText} where id = {id}").on(
        'id -> id, 'newText-> newText
      ).executeUpdate()
    }
  }

  def create( comment:Comment ): Comment = {
    DB.withTransaction{ implicit connection =>

      //  get new id
      val id: Long = 
        SQL("select next value for cmment_seq").as(scalar[Long].single)

      // insert new formback container
      SQL(
        """ 
          insert into cmment values (
            {id}, {elementId}, {text}, {anon}, {author} 
          )
        """
      ).on(
        'id->id, 'elementId-> comment.form, 'anon-> comment.kind,
        'text-> comment.value, 'author-> comment.author 
      ).executeUpdate()

      comment.copy( id=Id(id) )
    }
  }
}
