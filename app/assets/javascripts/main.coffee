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
    crossroads: 'libraries/crossroads.min'
    signals: 'libraries/signals.min'
    routes: 'routes'

  shim:
    'jquery':
      exports: '$'

    'routes':
      exports: 'jsRoutes'

    'signals':
      exports: 'Signals'

    'crossroads':
      deps: ['signals']
      exports: 'X'

    'jqueryUI':
      deps: ['jquery']
      exports: 'Ui'
    'underscore':
      exports : '_'
    'backbone':
      deps: ["underscore"]
      exports:'Backbone'
)

require(['router', 'crossroads'], ( Router, X ) ->

  Router.initialize()
  
  #### CROSSROADS ROUTES
  X.addRoute '/{user}', (user) ->
    console.log( user )

  X.addRoute '/{user}/{feed}', (user, feed) ->
    console.log( "user: " + user + " feed: " + feed )

  # start listening to the routes
  X.parse( window.location.pathname )
)
