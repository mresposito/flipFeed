package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Feed( id:Pk[Long], name:String, description:String, anon:Boolean, owner: String )

object Feed extends DatabaseItem {

  type T    = Feed
  def table = "feed"

  def singleResult( set:SimpleSql[Row] ): Option[Feed] = 
    DB.withConnection { implicit connection =>
      set.as(Feed.simple.singleOpt)
    }

  def multipleResults( set:SimpleSql[Row] ): Seq[Feed] = 
    DB.withConnection { implicit connection =>
      set.as(Feed.simple * )
    }
  
  // - Parsers

  /*
   * Parses a feed from result set
   */
  val simple = {
    get[Pk[Long]]("feed.id") ~
    get[String]("feed.name") ~
    get[String]("feed.description") ~
    get[Boolean]("feed.anon") ~
    get[String]("feed.owner") map {
      case id~name~description~anon~email => 
        Feed( id, name, description, anon, email )
    }
  }


  // - Queries

  def findByAuthor( user:User   ): Seq[Feed] = findByAuthor( user.name )
  def findByAuthor( user:String ): Seq[Feed] = findSeqByAttr( "owner", user )

  def findByOwnerName( user:String, name:String ): Option[Feed] = {
      DB.withConnection { implicit connection =>
        SQL(
          """
          select * from feed
          where owner = {user} and name = {name}
          """
        ).on(
        'user -> user, 'name->name
      ).as(Feed.simple.singleOpt )
    }
  }

  /*
   * update naming project
   */
  def rename( id:Pk[Long], newName:String ) {
    DB.withConnection{ implicit connection =>
      SQL("update feed set name = {name} where id = {id}").on(
        'id -> id, 'name-> newName
      ).executeUpdate()
    }
  }

  def create( feed:Feed ): Feed = {
    DB.withTransaction{ implicit connection =>

      //  get new id
      val id: Long = 
        SQL("select next value for feed_seq").as(scalar[Long].single)

      // insert new feedback container
      SQL(
        """ 
          insert into feed values (
            {id},{name},{description},{anon},{owner}
          )
        """
      ).on(
        'id -> id, 'name -> feed.name,
        'description -> feed.description ,'anon -> feed.anon,
        'owner -> feed.owner
      ).executeUpdate()

      feed.copy( id=Id(id) )
    }
  }
}
