package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

import org.openqa.selenium.Cookie

class FeedBrowser extends Specification {

  "Feed Browser" should {
    
    /*"check feeds" in {*/
    /*  running(TestServer(3333), HTMLUNIT) { browser =>*/
    /*    browser.webDriver.manage().addCookie( new Cookie("PLAY_SESSION",*/
    /*      "1dd6811c9df64e03a892f55f57dd0f1190656d88-email%3Amichele%40sample.com") )*/

    /*    browser.goTo("http://localhost:3333/michele")*/

    /*    browser.pageSource must contain("myf1")*/
    /*    browser.pageSource must contain("myf2")*/
    /*  }*/
    /*}*/

    "create feeds" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")
        browser.$("#email").text("michele@sample.com")
        browser.$("#password").text("secret")
        browser.$("#loginbutton").click()
        browser.goTo("http://localhost:3333/michele")
        browser.getCookie("PLAY_SESSION").getValue() must contain("michele%40sample.com")

        browser.$("#popupCreate").click()
        browser.$("#name").text("testFeed")
        browser.$("#description").text("this is just a test")
        browser.$("#createbutton").click()
        
        browser.pageSource must contain("testFeed")

        browser.$("#popupCreate").click()
        browser.$("#name").text("testAnonFeed")
        browser.$("#description").text("this is just an anonymous test")
        browser.$("#anon").click()
        browser.$("#createbutton").click()
        
        browser.pageSource must contain("testAnonFeed")
      }
    }

    "view feed" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")
        browser.$("#email").text("michele@sample.com")
        browser.$("#password").text("secret")
        browser.$("#loginbutton").click()
        browser.goTo("http://localhost:3333/michele")

        browser.$("#myf1").click()
        browser.pageSource must contain("myf1")
      }
    }

    "Comment on feed" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")
        browser.$("#email").text("michele@sample.com")
        browser.$("#password").text("secret")
        browser.$("#loginbutton").click()
        browser.goTo("http://localhost:3333/sandra/sandra1")
        
        val commentText = "I love you!"
        browser.$("#comment").text("I love you!")
        browser.$("#commentbutton").click()
        browser.pageSource must contain( commentText )
      }
    }
  }

}
