package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class ApplicationSpec extends Specification {
  
  
  "Application" should {
    
    "go to login page without credentials" in {
      running(FakeApplication()) {
        val result  = routeAndCall( FakeRequest( GET, "/")).get
        status(result) must equalTo(200)
      }
    }

    "get login page" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result  = routeAndCall( FakeRequest( GET, "/login")).get
        status(result) must equalTo(200)
      }
    }

    "Get dashboard page with credentials" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result  = routeAndCall( FakeRequest( GET, "/").withSession("email"->"michele@sample.com")).get
        status(result) must equalTo(200)
      }
    }
  }
}
