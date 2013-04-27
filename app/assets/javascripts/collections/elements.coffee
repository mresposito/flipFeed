# -----------------------------------------------
# ELEMENTS
# -----------------------------------------------

define [
  "jquery",
  "jqueryUI",
  "underscore",
  "backbone",
  "routes",
  "models/element",
  "views/elements/view",
  "views/elements/edit",
  "views/elements/listEdit"
], ($, Ui, _, Backbone, jsRoutes, Element, ViewElement, EditElement, ElementListEdit) ->

  class Elements extends Backbone.Collection
    model: Element

  # request elements from the server
  initModel= ->
    id =  $("#feedBackId").attr("value")
    elems = null
    r = jsRoutes.controllers.Elements.findByFeed( id )
    ret = null
    $.ajax
      url: r.url
      type: r.type
      dataType:'json'
      async: false
      success: (data) ->
        ret = data

      error: (err) ->
        console.log("Request failed")
        console.log ( err )
    # create the models
    new Elements( ret.map ( el ) -> ( new Element( el ) ) )

  createViews = (data) ->
    console.log("Creating elements")

    # # create first view
    elemViews = elements.map (element) ->
      new ViewElement
        model: element

    # # collection links back to our view
    elem = new EditElement
      el: $("#addFeedElement")
      elements: elements

  initModel()
# 
#   $("#sendForm").bind("click", (el) ->
#     elemViews. map ( comment ) ->
#       comment.sendForm()
#   )
# 
#   $("#delete").bind("click", (el) ->
#     if (confirm("Are you sure you want to delete"))
#       id =  $("#feedBackId").attr("value")
#       r = jsRoutes.controllers.Feeds.delete( id )
#       $.ajax
#         url: r.url
#         type: "DELETE"
#         success: (data) ->
#           window.location.href =
#             $("#profileLink").attr("href")
#         error: (err) ->
#           console.log("Error in delete")
#   )
#   "why?"
# 
