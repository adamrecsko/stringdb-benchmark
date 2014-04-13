define(['plugins/router', 'durandal/app'], function (router, app) {
    "use strict";
    return {
        router: router,
        search: function() {

        },
        activate: function () {
            router.map([
                { route: '', title:'Home', moduleId: 'viewmodels/welcome', nav: true },
                { route: 'mysql', title:'Home', moduleId: 'viewmodels/mysql', nav: true },
                { route: 'neo4j', title:'Home', moduleId: 'viewmodels/neo4j', nav: true }
            ]).buildNavigationModel();

            return router.activate();
        }
    };
});