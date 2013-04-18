package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._


case class Element( id:Pk[Long], feedId: Long, name:String, description:String, kind:String, attr:String) 

object Element extends DatabaseItem  {

  type T    = Element
  def table = "element"

  def singleResult( set:SimpleSql[Row] ): Option[Element] = 
    DB.withConnection { implicit connection =>
      set.as(Element.simple.singleOpt)
    }

  def multipleResults( set:SimpleSql[Row] ): Seq[Element] = 
    DB.withConnection { implicit connection =>
      set.as(Element.simple * )
    }

  // -- Parsers

  val simple = {
      get[Pk[Long]]( table + ".id") ~
      get[Long]    ( table + ".feedId") ~
      get[String]  ( table + ".name") ~
      get[String]  ( table + ".description") ~
      get[String]  ( table + ".kind") ~
      get[String]  ( table + ".attrb")  map {
        case id~feedId~name~description~kind~attr => Element( id, feedId, name, description, kind, attr )
      }
  }

  // - Queries

  def findByFeed ( feed : Long ): Seq[Element] = findSeqByAttr ( "feedId", feed )
  
  /**
   * Create a Element.
   */
  def create( element: Element): Element =
    DB.withConnection { implicit connection =>

      val id: Long = 
        SQL("select next value for element_seq").as(scalar[Long].single)

      SQL(
        """
          insert into element values (
            {id}, {feedId}, {name}, {description}, {kind}, {attrb}
          )
        """
      ).on(
        'id          -> id,
        'feedId      -> element.feedId,
        'name        -> element.name,
        'description -> element.description,
        'kind        -> element.kind,
        'attrb       -> element.attr
      ).executeUpdate()
      
      element.copy ( id = Id(id) )
    }
}
