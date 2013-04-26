# -----------------------------------------------
# app
# -----------------------------------------------
#

define([], () ->
  class Main
    console.log("YEah!")
)
# define([
#   'jquery', 'underscore', 'backbone'
# ] ,
# ($, _, Backbone ) ->
#   init= () -> alert("Yeah!")
# 
# ) # end define


  # # Just a log helper
  # log = (args...) ->
  #   # console.log.apply console, args if console.log?
  #   console.log.apply console, args

  # feedNumber = $("#feedBackId").attr("value")

  # class Elements extends Backbone.Collection
  #   model: EditElement

  # ## request elements from the server
  # initModel= ->
  #   id =  $("#feedBackId").attr("value")
  #   log ( "id: " + id )
  #   elems = null
  #   r = jsRoutes.controllers.Elements.findByFeed( id )
  #   $.ajax
  #     url: r.url
  #     type: r.type
  #     dataType:'json'
  #     async: false
  #     success: (data) ->
  #       createViews( data )

  #     error: (err) ->
  #       log("Request failed")
  #       log ( err )

  # #### CREATE ALL OF THE BACKBONE VIEWS

  # createViews = (data) ->

  #   elements = new Elements( data.map ( el ) -> ( new ElementModel( el ) ) )

  #   # create first view
  #   elemViews = elements.map (element) ->
  #     new ElementView
  #       model: element

  #   # show the list of elements
  #   editView = elements.map ( element ) ->
  #     new ElementListEdit
  #       model: element
  #       collection: elements

  #   # init the sort function
  #   $("#sortable").sortable(
  #     start: (el,ui) ->
  #             start_pos = ui.item.index()
  #             ui.item.data('start_pos', start_pos)
  #     update: changeOrder
  #   )

  #   # collection links back to our view
  #   elem = new EditElement
  #     el: $("#addFeedElement")
  #     elements: elements

  # changeOrder = ( event, ui ) ->
  #     start_pos = ui.item.data('start_pos')
  #     end_pos = $(ui.item).index()
  #     console.log(start_pos + " end:" + end_pos )

  # #### MAIN FUNCTION

  # $ -> # document is ready!
  #   elements = initModel()

  #   $("#sendForm").bind("click", (el) ->
  #     elemViews. map ( comment ) ->
  #       comment.sendForm()
  #   )

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
