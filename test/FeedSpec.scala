package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

import models._
import anorm._

class FeedSpec extends Specification {
  
  val fd = Feed(Id(0), "myName", "is the most beautiful", false, "michele")
  
  "Feed" should {
    
    "create" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        // check feed is not here
        Feed.findById( fd.id ).isDefined must equalTo(false)

        val newFd = Feed.create( fd )
        
        Feed.findAll.isEmpty must equalTo(false)
      }
    }

    "rename" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val my1  = Feed.findById(1000). map { n =>
          n.name must equalTo("myf1")
        }
        my1.isDefined must equalTo(true)

        Feed.rename( Id(1000), "myNewName" )
        val mNew = Feed.findById(1000)
        mNew . map { m =>
          m.name must equalTo("myNewName")
        }

        mNew.isDefined must equalTo(true)
      }
    }

    "find by author" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Feed.findByAuthor("sandra").length must equalTo(2)
      }
    }

    /*"tell valid names" in {*/
    /*  running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {*/

    /*    val Some(result)  = routeAndCall( FakeRequest( GET, "/michele/validName").withJsonBody(Json.parse("{\"name\": \"myf1\"}")) )*/
    /*    status(result) must equalTo(404)*/

    /*    val Some(newResult)  = routeAndCall( FakeRequest( GET, "/michele/validName") )*/
    /*    status(result) must equalTo(200)*/
    /*  }*/
    /*}*/
  }
}
