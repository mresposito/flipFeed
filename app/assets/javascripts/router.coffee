# -----------------------------------------------
# Router
# -----------------------------------------------

define (require, exports, module) ->
  $ = require 'jquery'
  _ = require 'underscore'
  Backbone = require 'backbone'

  AppRouter = class AppRouter extends Backbone.Router
  
  
    routes:
      "/" : "index"
      ":user/:feedback" : "feedIndex"
    initialize: ->
      console.log("Yeat")

    index: ->
      console.log("index!")

    feedIndex: (user,feedName) ->
      console.log("feedIndex")

  initialize: ->
    router = new AppRouter()
    # Backbone.history.start()
