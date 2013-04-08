package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class AuthenticationBrowser extends Specification {

  "Authentication Browser" should {
    
    "try invalid login" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")
        browser.$("#email").text("try@sample.com")
        browser.$("#password").text("secret111")
        browser.$("#loginbutton").click()
        browser.url must equalTo("http://localhost:3333/login")
        /*browser.pageSource must contain("Invalid email or password")*/
      }
    }

    "log in" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")
        browser.$("#email").text("michele@sample.com")
        browser.$("#password").text("secret")
        browser.$("#loginbutton").click()
        browser.pageSource must contain("logout")
        browser.getCookies().size must equalTo(1)
        browser.goTo("http://localhost:3333/")
        browser.url must equalTo("http://localhost:3333/")
    }
  }

    "create double user" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        // test repeat email
        browser.goTo("http://localhost:3333/createUser")
        browser.$("#email").text("michele@sample.com")
        browser.$("#name").text("newName!!")
        browser.$("#passw1").text("secret111")
        browser.$("#passw1").text("secret111")
        browser.$("#createbutton").click()
        browser.url must equalTo("http://localhost:3333/createUser")
        // test repeat name
        browser.$("#email").text("michelaene@sample.com")
        browser.$("#name").text("michele")
        browser.$("#passw1").text("secret111")
        browser.$("#passw1").text("secret111")
        browser.$("#createbutton").click()
        browser.url must equalTo("http://localhost:3333/createUser")
      }
    }
  }
}
