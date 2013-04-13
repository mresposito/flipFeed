package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

trait DatabaseItem {

    type T

    def table:  String

    def singleResult    ( set:SimpleSql[Row] ): Option[T]
    def multipleResults ( set:SimpleSql[Row] ): Seq[T]
    /*def parse[T]: ResultSetParser[Option[T]]*/

    def accessDbWithKey[V] (  key:String, value:V ): SimpleSql[Row] = {
      DB.withConnection { implicit connection =>
        SQL("select * from "+ table +" where "+ key + " = {val}").on(
          'val -> value
        )
      }
    }

    def accessFullTable:SimpleSql[Row] = {
            DB.withConnection { implicit connection =>
            SQL("select * from " + table )
        }
    }

    /**
     * Queries wrappers
     **/
    def findByAttr[V]( key:String, value:V ):Option[T] = singleResult    ( accessDbWithKey( key, value ) )
    def findSeqByAttr[V]( key:String, value:V ):Seq[T] = multipleResults ( accessDbWithKey( key, value ) )

    /*
     * specific wrappers
     */

    def findById(id:Long    ): Option[T] = findById( Id(id) )
    def findById(id:Pk[Long]): Option[T] = findByAttr( "id", id.get )

   /**
    * Retrieve all element in the table.
    */
    def findAll: Seq[T] = multipleResults ( accessFullTable )

    
    /**
     * DELETE Operations
     **/
   def delete( id:Long ) = {
     DB.withConnection{ implicit connection =>
       SQL("delete from " + table + " where id = {id}").on(
         'id-> id
       ).executeUpdate()
     }
   }

   def deleteByKey ( key: String,  value:String  ) = {
     DB.withConnection{ implicit connection =>
       SQL("delete from " + table + " where " + key +" = {value}").on(
         'value-> value
       ).executeUpdate()
     }
   }
}

