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
        'benchmark': 'models/benchmark',
        'sigma' : '../lib/sigma/sigma.min',
        'sigma.json':  '../lib/sigma/plugins/sigma.parsers.json.min',
        'd3':'../lib/d3/d3',
        'visualization':'../lib/neo4j/visualization'

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

        'sigma': {
            exports: 'sigma'
        },
        'sigma.json': {
            deps: ['sigma'],
            exports: 'sigma'
        },

        komapping: {
            deps: ['knockout'],
            exports: 'komapping'
        },

        d3: {
            exports: 'd3'
        }

    }
});

define(['durandal/system', 'durandal/app', 'durandal/viewLocator','bootstrap','jquery'],  function (system, app, viewLocator) {
    //>>excludeStart("build", true);
    system.debug(true);
    //>>excludeEnd("build");

    app.title = 'String-db benchmark';

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