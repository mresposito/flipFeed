# -----------------------------------------------
# ElementView
# -----------------------------------------------

define [
  "jquery",
  "jqueryUI",
  "underscore",
  "backbone",
  "views/elements/base",
  "models/comment"
], ($, Ui, _, Backbone, BaseElement, Comment) ->
  class ElementView extends BaseElement
    events:
      "click button#sendForm" : "sendForm"

    initialize: ->
      # clone the item
      @el = $("ul#viewFeed > li#firstEl").clone()
      @el.attr("id", "")
      @el.appendTo( $("ul#viewFeed") )
      console.log(@model.get("name"))

      # set the comment to null
      @modelComment = null
      
      # parse the attributes of the model
      # check for invalid json models
      try
        @attr = JSON.parse( @model.get("attr") )
      catch e
        @attr = {}

      # render the view
      @el.html @template(model: @model)
      @render( @model.get("kind"), @el.children("#question") )

    # render functions
    renderSlider: (el) ->
      el.html("<div id=\"slider\" class=\"span5\"></div><br>")
      # el.children("#slider").slider()
      el.children("#slider").slider
        min: parseInt( @attr["min"] )
        max: parseInt( @attr["max"] )
        value: (parseInt( @attr["max"] ) - parseInt( @attr["min"] ) ) /2

    renderOpenQest: (el) ->
      el.html("<textarea id=\"text\" rows=\"3\" ></textarea><br>")

    renderMulChoice: ( el ) ->
      el.html @templateMulChoice(kind: "checkbox", labels: @attr)

    renderSelect:(el ) ->
      el.html @templateSelect( labels: @attr )

    sendForm: (el) ->
      # if @modelComment == null
      val = @retrieveAnswer()
      if val != ""
        @modelComment = new Comment
          value: val
          kind: @model.get("kind")
          form: @model.get("id")
          author: "michele"

        @modelComment.save()
      # else
      #   @modelComment.update()

      # location.reload()

    retrieveAnswer: () ->
      kind = @model.get("kind")

      console.log ("Retrieve type: " + kind)
      val = ""
      if ( kind == "slider" )
        val = @el.find("#slider").slider("value")
      else if ( kind == "openQuest" )
        val = @el.find("#text").val()

      else if ( kind == "mulChoice" )
        val = @el.find("input[type=checkbox]:checked").val()

      else
        val = @el.find("select").val()

      console.log( val )
      val

    ## TEMPLATES
    template: _.template '''
      <h3>
        <%= model.get("name") %>
      </h3>
      <p>
        <%= model.get("description") %> 
      </p>
      <div id="question"> 
      </div>
    '''

    templateSelect: _.template '''
    <select>
        <% _.each( labels, function(name) { %>
            <option ><%= name %></option>
        <% }); %>
    </select>
    '''

    templateMulChoice: _.template '''
      <% _.each( labels, function(name) { %>
        <label class="<%= kind %>">
          <input type="<%= kind %>" value=<%= name%> ><%= name %>
        </label>
      <% }); %>
    '''

  run: () ->
    require ['collections/elements'],(elements) ->
      # show the list of elements
      elements.map ( element ) ->
        new ElementView
          model: element
