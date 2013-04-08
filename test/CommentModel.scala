package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.Request

import models._
import anorm._

class CommentModel extends Specification {
  
  "Comment spec" should {

    /*
     * MODEL TESTING
     */
    
    "create" in {
  
       val fd = Comment(Id(0), "I think you are great!",  false, 1003, "michele")

      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val newFd = Comment.create( fd )
        Comment.findAll.isEmpty must equalTo(false)
        /* try to retrive the comment */
        Comment.findByAuthor( "michele" ).isEmpty must equalTo(false)
        Comment.findByFeed( 1003 ).isEmpty must equalTo(false)
      }
    }

    "change name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val newText = "this is my new message"
        Comment.edit( Id(1000), newText )
        
        Comment.findById(1000). map { m =>
          m.text must equalTo( newText )
        }.isDefined must equalTo(true)
      }
    }

    "find by feed" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Comment.findByFeed(1000).length must equalTo(2)
      }
    }

    "delete" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Comment.delete( 1000 )
        Comment.findById(1000).isDefined must equalTo(false)
      }
    }

    /*
     * CONTROLLER TESTING
     */

    /*"create from route" in {*/
    /*  running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {*/
    /*    val result = controllers.Feeds.commentOn("michele", "myfd1")(*/
    /*      Request().withFormUrlEncodedBody("text" -> "what up?", "anon" -> "anon")*/
    /*    )*/

    /*    status(result) must equalTo(200)*/
    /*    Comment.findByAuthor("michele").length must equalTo(3)*/
    /*  }*/
    /*}*/
  }
}
