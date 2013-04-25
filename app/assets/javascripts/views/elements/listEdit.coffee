# -----------------------------------------------
# ElementView
# -----------------------------------------------

class @ElementListEdit extends EditBase

  initialize: ->
    # create new item
    $("#sortable").append(  @template(model: @model) )
    # get the item which we have
    self = this
    @elem = @model
    @el = $("#sortable li").last()
    
    @el.find("#deleteEl").click( ( event) -> self.removeForm() )
    @el.find("#copyEl"  ).click( ( event) -> self.copyForm() )

  removeForm: ( ) ->
   @el.remove() # dont show this anymore
   @model.destroy
     success: () ->
       console.log("element successfully deleted")

  copyForm: ( ) ->
    attr = @model.toJSON()
    delete attr.id
    element = new ElementModel( attr )
    element.save()

    new ElementView
      model: element
    new ElementListEdit
      model: element

  # deleteElement: 
  ## TEMPLATES
  template: _.template '''
    <li class="ui-state-default">
      <div id="container" >
        <span class="ui-icon ui-icon-arrowthick-2-n-s"></span>
        <b><%=model.get("name") %></b>
        <div class="elemEditComands">
          <a id="copyEl" class="icon-copy icon-large"></a>
          <a id="editEl" class="icon-edit icon-large"></a>
          <a id="deleteEl" class="icon-trash icon-large"></a>
        </div> 
        <p><%= model.get("description") %></p>
      </div>
    </li>
  '''
