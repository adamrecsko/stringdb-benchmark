define(['morris','jquery','knockout','benchmark'],function(Morris,$,ko,Benchmark) {
    var ctor = function () {

        var self = this;

        this.proteinCount = ko.observable();
        this.benchmarks = ko.observableArray();

        this.activate = function(){
            $.getJSON("mysqlcount").then(function(data){
                self.proteinCount(data.value);
            });
        };
        this.attached = function(){

                $.getJSON("mysql").then(function(dataIn){

                    ko.utils.arrayForEach(dataIn,function(data){
                        self.benchmarks.push(new Benchmark(data));
                    });
                    Morris.Donut({
                        element: 'speedchart_mysql',
                        data:dataIn,
                        formatter: function (y, data) { return  y+"ms" }
                    });
            });

        }
    };
    return ctor;
});