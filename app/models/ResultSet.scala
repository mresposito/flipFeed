package models

import play.api.libs.json._

trait formUtils {

  def parseList( list:String ) : Seq[String] = Json.fromJson[Seq[String]](Json.parse( list ))

}

class SingleResult ( val elem: Element ) extends formUtils {

  def countElements: Seq[String] = 
  parseList( elem.attr ).map ( at =>
    at + ": " + comments.filter( _.value.contains( at ) ).length.toString
  )

  val kind = elem.kind

  val comments = Comment.findByElement ( elem.id.get )

  val results:Seq[String] = kind match {
    case "openQuest" => comments.map( _.value )
    case "slider"    => try {
      Seq(( "Average value: " + 
        comments.map( _.value.toInt ).sum / comments.length ).toString)
    } catch {
      case e:NumberFormatException => Seq("")
    }

    case "mulChoice" => countElements
    case "select"    => countElements
  }
}

class ResultSet ( val feed:Long ) {

  val elems = Element.findByFeed( feed )

  lazy val results = elems. map ( new SingleResult( _ ) )

  lazy val answers = results.map ( _.comments.length ).sum
}
