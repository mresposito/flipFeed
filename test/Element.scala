package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

import models._
import anorm._

class Element extends Specification {
  
  val fd = Element(Id(0), 1000, "what up?", "all good?", "select", "{min: 10}")
  
  "Element Model" should {
    
    "create" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        // check feed is not here
        Element.findById( fd.id.get ).isDefined must equalTo(false)

        val newFd = Element.create( fd )
        
        Element.findAll.isEmpty must equalTo(false)
      }
    }
    "find by feed" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Element.findByFeed( 1000 ).isEmpty must equalTo(false)
      }
    }
  }

  "Element Control" should {
    "retrieve Json" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val Some(ans) = routeAndCall( FakeRequest( GET, "/feed/1000/elements" ) )
        status(ans) must equalTo(200)
      }
    }
  }
}
