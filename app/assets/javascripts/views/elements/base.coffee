# -----------------------------------------------
# Base
# -----------------------------------------------

define [
  "jquery",
  "underscore",
  "backbone"
], ($, _, Backbone) ->
  class EditBase extends Backbone.View
    render: (kind, el ) ->
      if ( kind == "slider" )
        @renderSlider( el )
      else if ( kind == "openQuest" )
        @renderOpenQest( el )
      else if ( kind == "mulChoice" )
        @renderMulChoice( el )
      else
        @renderSelect( el )

    renderSlider: ( el ) ->
      $("#slideVals").show()
      el.slider()

    renderOpenQest: ( el ) ->
      el.html ("<textarea rows=\"1\" ></textarea><br>")

    renderMulChoice: ( el ) ->
      $("#inputLabels").show()

    renderSelect: ( el ) ->
      $("#inputLabels").show()
