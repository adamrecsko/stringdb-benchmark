define(['plugins/router', 'durandal/app'], function (router, app) {
    "use strict";
    return {
        router: router,
        search: function() {

        },
        activate: function () {
            router.map([
                { route: '', title:'Home', moduleId: 'viewmodels/welcome', nav: true },
                { route: 'benchmarks', title:'MySQL', moduleId: 'viewmodels/mysql', nav: true },
                { route: 'graph', title:'Graph', moduleId: 'viewmodels/graph', nav: true },
                { route: 'bench/:name', title:'Benchmark', moduleId: 'viewmodels/bench', nav: true },

            ]).buildNavigationModel();

            return router.activate();
        }
    };
});