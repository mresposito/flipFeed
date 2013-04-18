# -----------------------------------------------
# Element  Model
# -----------------------------------------------

feedNumber = $("#feedBackId").attr("value")

class @ElementModel extends Backbone.Model
  urlRoot: jsRoutes.controllers.Elements.add(feedNumber),

  defaults:
    feedId: null,
    name: "Question",
    description: "",
    kind: "select",
    attr: "{}"
  
