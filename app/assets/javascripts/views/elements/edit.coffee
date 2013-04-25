# -----------------------------------------------
# EDIT Feed
# -----------------------------------------------


class @EditElement extends EditBase
  events:
    "change select#formType"       :  "changeSelect"
    "change select#from"           :  "changeFromLabel"
    "change select#to"             :  "changeToLabel"
    "click  button#createbutton"   :  "saveModel"
    "click  input#addOption"       :  "addInput"

  initialize: ->
    $("#name").text( $("input#question").val() )
    $("p#description").text( $("textarea#description").val() )
    @changeSelect   ( null )
    @changeFromLabel( null )
    @changeToLabel  ( null )
  
  changeSelect: (e) ->
    # reset the form
    $("#slideVals").hide()
    $("#inputLabels").hide()
    # render the selected item
    @render( $("select#formType").val(), $("div#type") )

  #### RENDER FUNCTIONS
  renderSlider: ( el ) ->
    $("#slideVals").show()
    el.slider()

  renderOpenQest: ( el ) ->
    el.html ("<textarea rows=\"1\" ></textarea><br>")

  renderMulChoice: ( el ) ->
    $("#inputLabels").show()

  renderSelect: ( el ) ->
    $("#inputLabels").show()

  #### actions
  addInput: (e) ->
    console.log("Add element")
    newEl = $("#trueOptions > li#firstEl").clone()
    newEl.attr( "value", "Enter new name" )
    newEl.attr( "id", "" ) # make sure does not multiply
    newEl.appendTo( $("ul#trueOptions") )

  changeToLabel: ( ev ) ->
    $("label#to").text ( $("select#to").val() )

  changeFromLabel: ( ev ) ->
    $("label#from").text ( $("select#from").val() )

  getAttr: ->
    # return the json of the data that we need to 
    # personalize the icons
    val = $("select#formType").val()
    if ( val == "slider" )
      data =
        from: $("select#from").val()
        to: $("select#to").val()
        fromLabel: $("input#labelFrom").val()
        toLabel: $("input#labelTo").val()

    else if ( val == "mulChoice" || val == "select" )
      data = @getLabels()

    JSON.stringify data

  getLabels: ->
    $(".options").map (el)-> el

  saveModel: ( ev ) ->
    console.log("saving")
    newModel = new ElementModel
      feedId: $("#feedBackId").attr("value")
      name: $("input#question").val()
      description: $("textarea#description").val()
      kind: $("select#formType").val()
      attrb: @getAttr()

    newModel.save
      success: (el) -> console.log("Saved successfully")

    new ElementView
      model: newModel

    new ElementListEdit
      model: newModel
