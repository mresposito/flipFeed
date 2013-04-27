# -----------------------------------------------
# Router
# -----------------------------------------------

define [
  "jquery",
  "underscore",
  "backbone"
], ($, _, Backbone) ->

  class AppRouter extends Backbone.Router
    routes:
      ":user/:feedback" : "feedIndex"

    initialize: ->
      console.log("init")

      require ["elements"],(Elements) ->
        console.log( Elements )

    index: ->
      console.log("index!")

    feedIndex: (user,feedback) ->
      console.log("feedIndex")

  initialize: ->
    router = new AppRouter()
    Backbone.history.start
      pushHistory: true
