package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import models._

class UserSpec extends Specification {
  
  
  "User" should {

    "retrieve by email" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        User.findByEmail("michele@sample.com").isDefined must equalTo(true)
      }
    }

    "retrieve by name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        User.findByName("michele").isDefined must equalTo(true)
        User.findByName("MiChEle").isDefined must equalTo(true)

        User.findByName("MiC").isDefined must equalTo(false)
      }
    }
    
    "create" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val credentials = ("my@try.edu", "name", "issecret")
        // check user is not here
        User.findByEmail(credentials._1).isDefined must equalTo(false)

        val usr = User(credentials._1, credentials._2, credentials._3)
        usr.email must equalTo ( credentials._1 )

        User.create( usr )
        
        User.findByEmail(credentials._1).isDefined must equalTo(true)
      }
    }

    "authenticate" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        
        User.authenticate("michele@sample.com", "secret").isDefined must equalTo(true)
      }
    }
  }
}
