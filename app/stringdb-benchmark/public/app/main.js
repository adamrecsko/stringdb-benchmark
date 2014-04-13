requirejs.config({
    paths: {
        'text': '../lib/require/text',
        'durandal':'../lib/durandal/js',
        'plugins' : '../lib/durandal/js/plugins',
        'transitions' : '../lib/durandal/js/transitions',
        'knockout': '../lib/knockout/knockout-3.1',
        'bootstrap': '../lib/bootstrap/js/bootstrap',
        'jquery': '../lib/jquery/jquery-1.9.1',
        'morris': '../lib/morris/js/morris',
        'raphael':'../lib/raphael/raphael-min',
        'komapping': '../lib/knockout/knockout.mapping',
        'benchmark': 'models/benchmark'
    },
    shim: {
        'bootstrap': {
            deps: ['jquery'],
            exports: 'jQuery'
       },
        'morris': {
            deps: ['raphael'],
            exports: 'Morris'
        },
        komapping: {
            deps: ['knockout'],
            exports: 'komapping'
        }

    }
});

define(['durandal/system', 'durandal/app', 'durandal/viewLocator','bootstrap','jquery'],  function (system, app, viewLocator) {
    //>>excludeStart("build", true);
    system.debug(true);
    //>>excludeEnd("build");

    app.title = 'CyborgTitan';

    app.configurePlugins({
        router:true,
        dialog: true,
        widget: true
    });

    app.start().then(function() {
        viewLocator.useConvention();
        app.setRoot('viewmodels/shell');
    });
});