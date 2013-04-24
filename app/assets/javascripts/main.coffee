# -----------------------------------------------
# MAIN
# -----------------------------------------------

# Just a log helper
log = (args...) ->
  # console.log.apply console, args if console.log?
  console.log.apply console, args
feedNumber = $("#feedBackId").attr("value")

class Elements extends Backbone.Collection
  model: EditElement


initModel= ->
  id =  ( $("#feedBackId").attr("value") )
  elems = null
  r = jsRoutes.controllers.Elements.findByFeed( id )
  $.ajax
    url: r.url
    type: r.type
    dataType:'json'
    async: false
    success: (data) ->
      elems = new Elements( data.map ( el ) -> ( new ElementModel( el ) ) )

    error: (err) ->
      log("Request failed")

  elems

$ -> # document is ready!
  elements = initModel()

  elemViews = elements.map (element) ->
    new ElementView
      model: element

  $("#sendForm").bind("click", (el) ->
    elemViews. map ( comment ) ->
      comment.sendForm()
  )

  # collection links back to our view
  elem = new EditElement
    el: $("#addFeedElement")
    elements: elements
