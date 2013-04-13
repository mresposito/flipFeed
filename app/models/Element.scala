package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._


case class Element( id:Pk[Long], feedId: Long, kind:String, attributes:String) 

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
      get[String]  ( table + ".kind") ~
      get[String]  ( table + ".attrb")  map {
        case id~feedId~kind~attributes => Element( id, feedId, kind, attributes )
      }
  }

  // - Queries

  def findByFeed ( feed : Long): Seq[Element] = findSeqByAttr ( "feedId", feed )
  
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
            {id}, {feedId}, {kind}, {attrb}
          )
        """
      ).on(
        'id     -> id,
        'feedId-> element.feedId,
        'kind  -> element.kind,
        'attrb -> element.attributes
      ).executeUpdate()
      
      element.copy ( id = Id(id) )
    }
}
