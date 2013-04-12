# -----------------------------------------------
# MAIN
# -----------------------------------------------

validFeedName: (element)->
  alert("editing")
  r = jsRoutes.controllers.Feeds.validName("michele")
  $.ajax
    url: r.url
    type: r.type
    dataType:'json'
    data:
      name: element.val()
    success: (data) ->
      true
    error: (err) ->
      false

# Just a log helper
log = (args...) ->
  console.log.apply console, args if console.log?

# ---------------------------------------- FEED

class Feed extends Backbone.View
  events:
    "change input#question "       :  "changeName"
    "change textarea#description " :  "changeDescription"
    "change select#formType"       :  "changeSelect"

  initialize: ->
    $("#name").text( $("#question").val() )
    $("p#description").text( $("textarea#description").val() )

  changeName: (e) ->
    # alert( e )
    $("h5#question").text( $("input#question").val() )
  changeDescription: (e) ->
    $("p#description").text( $("textarea#description").val() )
  changeSelect: (e) ->
    val = $("select#formType").val()
    el = $("div#type")
    if ( val == "slider" )
      @renderSlider( el )
    else if ( val == "openQuest" )
      @renderOpenQest( el )
    else if ( val == "mulChoice" )
      @renderMulChoice( el )
    else
      @renderSelect( el )

  renderSlider: ( el ) ->
   # el.html( "<div id=\"slider\"></div>" )
   $("#type").slider()
  renderOpenQest: ( el ) ->
   el.text( "openOpenQuest" )
  renderMulChoice: ( el ) ->
   el.text( "open" )
  renderSelect: ( el ) ->
   el.text( "select" )

class FeedElement extends Backbone.Model
        
# ------------------------------------- INIT APP
$ -> # document is ready!

  feedPage = new Feed el: $("#feedContainer")
  # app = new AppRouter()
  # Backbone.history.start
    # pushHistory: true
