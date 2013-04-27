# -----------------------------------------------
# ELEMENTS
# -----------------------------------------------

define [
  "jquery",
  "jqueryUI",
  "underscore",
  "backbone",
  "routes",
  "element",
  "viewElement",
  "editElement",
  "listEditElement"
], ($, Ui, _, Backbone, jsRoutes, Element, ViewElement, EditElement, ElementListEdit) ->

  class Elements extends Backbone.Collection
    model: Element

  # request elements from the server
  initModel= ->
    id =  $("#feedBackId").attr("value")
    elems = null
    r = jsRoutes.controllers.Elements.findByFeed( id )
    $.ajax
      url: r.url
      type: r.type
      dataType:'json'
      async: false
      success: (data) ->
        createViews( data )

      error: (err) ->
        console.log("Request failed")
        console.log ( err )

  createViews = (data) ->
    console.log("Creating elements")

    elements = new Elements( data.map ( el ) -> ( new Element( el ) ) )

    # # create first view
    elemViews = elements.map (element) ->
      new ViewElement
        model: element

    # show the list of elements
    editView = elements.map ( element ) ->
      new ElementListEdit
        model: element
        collection: elements

    # init the sort function
    $("#sortable").sortable(
      start: (el,ui) ->
              start_pos = ui.item.index()
              ui.item.data('start_pos', start_pos)
      update: changeOrder
    )

    # # collection links back to our view
    elem = new EditElement
      el: $("#addFeedElement")
      elements: elements

  initModel()

  $("#sendForm").bind("click", (el) ->
    elemViews. map ( comment ) ->
      comment.sendForm()
  )

  $("#delete").bind("click", (el) ->
    if (confirm("Are you sure you want to delete"))
      id =  $("#feedBackId").attr("value")
      r = jsRoutes.controllers.Feeds.delete( id )
      $.ajax
        url: r.url
        type: "DELETE"
        success: (data) ->
          window.location.href =
            $("#profileLink").attr("href")
        error: (err) ->
          console.log("Error in delete")
  )
  "why?"

changeOrder = ( event, ui ) ->
    start_pos = ui.item.data('start_pos')
    end_pos = $(ui.item).index()
    console.log(start_pos + " end:" + end_pos )
