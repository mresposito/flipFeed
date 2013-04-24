import play.api._

import models._
import anorm._

object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    InitialData.insert()
  }
  
}

/**
 * Initial set of data to be imported 
 * in the sample application.
 */
object InitialData {
  
  def insert() = {
    
    if(User.findAll.isEmpty) {
      
      Seq(
        User("michele@sample.com" , "michele" , "secret"),
        User("sandra@sample.com"  , "Sandra"  , "secret"),
        User("cookie@sample.com"  , "Cookie!" , "secret")
      ).foreach(User.create)
    }

    if(Feed.findAll.isEmpty ) {
      Seq(
        Feed(Id(1) , "myf1"    , "how did i do?" , false , "michele" ) ,
        Feed(Id(2) , "myf2"    , "I hope well?"  , true  , "michele" ) ,
        Feed(Id(3) , "sandra1" , "and you?"      , true  , "sandra" )  ,
        Feed(Id(4) , "sandra2" , "Good"          , true  , "sandra" )
      ).foreach(Feed.create)
    }

    if( Element.findAll.isEmpty ) {
      Feed.findAll. map { feed =>
        Seq(
          Element(Id(1), feed.id.get, "Am I good?", "Please, tell me", "openQuest", ""),
          Element(Id(2), feed.id.get, "sure...", "now!", "openQuest", ""),
          Element(Id(3), feed.id.get, "Slide", "tell me baby", "slider", "{ \"min\": 1, \"max\": 10 }"),
          Element(Id(4), feed.id.get, "Multiple?", "Please, select all", "mulChoice", "[\"first\", \"second\", \"third\"]"),
          Element(Id(5), feed.id.get, "Which one?", "Please, select one", "select", "[\"first\", \"second\", \"third\"]")
        ).foreach( Element.create )
      }
    }

    if(Comment.findAll.isEmpty ) {
      Element.findAll. map { element =>
        Seq(
          Comment(Id(1), "THis is great!!"   , "openQuest", element.id.get, "michele"  ),
          Comment(Id(2), "THis too, its nice", "openQuest", element.id.get, "michele"  )
        ).foreach(Comment.create)
      }
    }
  }
}
