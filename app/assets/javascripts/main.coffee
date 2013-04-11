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
  initialize: ->
    alert("load feed page")
    # $(".editFeedName").change @editName
    # $(".editFeedName").focus @editName
    # HTML is our model
    # @el.children("li").each (i,group) ->
      # new Group
      #   el: $(group)
      # $("li",group).each (i,project) ->
      #   new Project
      #     el: $(project)
        
# ------------------------------------- INIT APP
$ -> # document is ready!

  feedPage = new Feed el: $("#feedContainer")
  # app = new AppRouter()

  Backbone.history.start
  
    pushHistory: true
