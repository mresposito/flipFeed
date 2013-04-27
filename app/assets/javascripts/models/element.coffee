# -----------------------------------------------
# Element  Model
# -----------------------------------------------

define [
  "underscore",
  "backbone",
  "routes"
], ( _, Backbone, jsRoutes ) ->
  class ElementModel extends Backbone.Model
    urlRoot: jsRoutes.controllers.Elements.add().url,

    defaults:
      feedId: null,
      name: "Question",
      description: "",
      kind: "select",
      attr: "{}"
