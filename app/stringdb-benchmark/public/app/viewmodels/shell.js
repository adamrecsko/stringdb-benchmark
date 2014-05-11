define(['plugins/router', 'durandal/app'], function (router, app) {
    "use strict";
    return {
        router: router,
        search: function() {

        },
        activate: function () {
            router.map([
                { route: '', title:'Home', moduleId: 'viewmodels/welcome', nav: true },
                { route: 'mysql', title:'MySQL', moduleId: 'viewmodels/mysql', nav: true },
                { route: 'neo4j', title:'Neo4J', moduleId: 'viewmodels/neo4j', nav: true },
                { route: 'graph', title:'Graph', moduleId: 'viewmodels/graph', nav: true }

            ]).buildNavigationModel();

            return router.activate();
        }
    };
});