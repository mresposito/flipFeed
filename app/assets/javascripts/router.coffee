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
      "view" : "loadViewFeed"
      "edit" : "loadEditFeed"

    initialize: ->
      console.log("init router")
 
    loadEditFeed: ()->
      require ["views/elements/listEdit"], (ListEdit) ->
        ListEdit.run()

    loadViewFeed: ()->
      require ["views/elements/view"], (View) ->
        View.run()

  initialize: ->
    router = new AppRouter()
    Backbone.history.start
      pushHistory: true
