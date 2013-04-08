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

    Feed.findByAuthor ( "michele" ).map { feed =>
      Seq(
        Comment(Id(1), "THis is great!!"   , false, feed.id.get, feed.owner ),
        Comment(Id(2), "THis too, its nice", true , feed.id.get, feed.owner )
      ).foreach(Comment.create)
    }

    Feed.findByAuthor ( "sandra" ).map { feed =>
      Seq(
        Comment(Id(1), "I agree"       , false, feed.id.get, feed.owner ),
        Comment(Id(2), "keep it going!", true, feed.id.get, feed.owner )
      ).foreach( Comment.create )
    }
  }
}
