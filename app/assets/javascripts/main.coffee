# -----------------------------------------------
# MAIN
# -----------------------------------------------
  
require.config (
  paths:
    # libraries
    jquery: 'libraries/jquery-1.7.1.min'
    jqueryUI: 'http://code.jquery.com/ui/1.10.2/jquery-ui'
    underscore: 'libraries/underscore-min'
    backbone: 'libraries/backbone-min'
    router: 'router'
    routes: 'routes' # jsRoutes
    # backbone models 
    element: 'models/element'
    comment: 'models/comment'
    # backbone collections
    elements: 'collections/elements'
    # backbone views
    baseElement: 'views/elements/base'
    viewElement: 'views/elements/view'
    editElement: 'views/elements/edit'
    listEditElement: 'views/elements/listEdit'

  shim:
    'jquery':
      exports: '$'
    'jqueryUI':
      deps: ['jquery']
      exports: 'Ui'
    'routes':
      exports: 'jsRoutes'
    'underscore':
      exports : '_'
    'backbone':
      deps: ["underscore"]
      exports:'Backbone'
)

require(['router'], ( Router ) ->
  Router.initialize()
)
