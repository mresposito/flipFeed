package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Feed( id:Pk[Long], name:String, description:String, anon:Boolean, owner: String )

object Feed {
  
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

  def findById(id:Long    ): Option[Feed] = findById( Id(id) )

  def findById(id:Pk[Long]): Option[Feed] = {
    DB.withConnection { implicit connection =>
      SQL("select * from feed where id = {id}").on(
        'id -> id
      ).as(Feed.simple.singleOpt)
    }
  }

  def findAll: Seq[Feed] = {
    DB.withConnection { implicit connection =>
        SQL( " select * from feed ").as(Feed.simple *)
    }
  }

  def findByAuthor( user:User   ): Seq[Feed] = findByAuthor( user.name )

  def findByAuthor( user:String ): Seq[Feed] = {
      DB.withConnection { implicit connection =>
        SQL(
          """
          select * from feed
          where owner = {user}
          """
        ).on(
        'user -> user
      ).as(Feed.simple *)
    }
  }

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

  def delete( id:Long ) = {
    DB.withConnection{ implicit connection =>
      SQL("delete from feed where id = {id}").on(
        'id-> id
      ).executeUpdate()
    }
  }

  def deleteByOwner ( owner:String  ) = {
    DB.withConnection{ implicit connection =>
      SQL("delete from feed where owner = {owner}").on(
        'owner-> owner
      ).executeUpdate()
    }
  }
}
