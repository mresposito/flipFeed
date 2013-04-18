# -----------------------------------------------
# ElementView
# -----------------------------------------------

class @ElementView extends EditBase

  initialize: ->
    # clone the item
    @el = $("ul#viewFeed > li#firstEl").clone()
    @el.attr("id", "")
    @el.appendTo( $("ul#viewFeed") )
    console.log(@model.get("name"))
    
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

  renderOpenQest: (el) ->
    el.html("<textarea rows=\"3\" ></textarea><br>")

  renderMulChoice: ( el ) ->
    el.html @templateMulChoice(kind: "checkbox", labels: @attr)

  renderSelect:(el ) ->
    el.html @templateSelect( labels: @attr )

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
          <option><%= name %></option>
      <% }); %>
  </select>
  '''

  templateMulChoice: _.template '''
    <% _.each( labels, function(name) { %>
      <label class="<%= kind %>">
        <input type="<%= kind %>" ><%= name %>
      </label>
    <% }); %>
  '''
