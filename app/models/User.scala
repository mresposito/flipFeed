package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class User(email:String, name:String, password:String)

object User extends DatabaseItem {

  type T = User
  def table = "user"

  def singleResult( set:SimpleSql[Row] ): Option[User] = 
    DB.withConnection { implicit connection =>
      set.as(User.simple.singleOpt)
    }

  def multipleResults( set:SimpleSql[Row] ): Seq[User] = 
    DB.withConnection { implicit connection =>
      set.as(User.simple * )
    }

    // -- Parsers

    val simple = {
        get[String]("user.email") ~
        get[String]("user.name") ~
        get[String]("user.password") map {
            case email~name~password => User(email,name,password)
        }
    }

    // - Queries

    /**
    * retrieve a User from email.
    */

  def findByName (name : String): Option[User] = findByAttr("name", name.toLowerCase)
  def findByEmail(email: String): Option[User] = findByAttr("email", email )
  def findName ( email: String ): Option[String] = Option( findByEmail(email).get.name )

  
  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
         select * from user where 
         email = {email} and password = {password}
        """
      ).on(
        'email -> email,
        'password -> password
      ).as(User.simple.singleOpt)
    }
  }
   
  /**
   * Create a User.
   */
  def create(user: User): User =
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user values (
            {email}, {name}, {password}
          )
        """
      ).on(
        'email -> user.email,
        'name -> user.name.toLowerCase,
        'password -> user.password
      ).executeUpdate()
      
      user
      
    }
}
