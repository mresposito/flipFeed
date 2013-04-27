# -----------------------------------------------
# Comment  Model
# -----------------------------------------------

define [
  "underscore",
  "backbone",
  "routes"
], ( _, Backbone, jsRoutes ) ->
  class CommentModel extends Backbone.Model
    urlRoot: jsRoutes.controllers.Elements.comment().url,

    defaults:
      value: null,
      form: null,
      kind: "openQuest",
      author: null
