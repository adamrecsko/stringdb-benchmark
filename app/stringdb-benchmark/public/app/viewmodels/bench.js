define(['morris','jquery','knockout','benchmark'],function(Morris,$,ko,Benchmark) {
    var ctor = function () {

        var self = this;


        this.name = ko.observable()
        self.benchmarks = ko.observableArray();
        this.count = ko.observable(30);

        this.reload = function(){

           $("#speedchart_mysql").empty();
           self.benchmarks.removeAll();


           var requests = [];
           ko.utils.arrayForEach(ko.utils.range(0, self.count()), function(i){
               var request =   // $.getJSON("bench/"+self.name());
                   $.ajax({url:"bench/"+self.name(),dataType:"json"});

               request.success(function(dataIn){

                   ko.utils.arrayForEach(dataIn,function(data){
                       data.index = i;
                       self.benchmarks.push(new Benchmark(data));

                   });
               });

              requests.push(request);
           });


            $.when.apply($,requests).done(function(){
                var dataIn = $.map(self.benchmarks(),function(benc){
                    return {
                        value: benc.value(),
                        index:benc.index(),
                        time:benc.time()

                    };
                });
                Morris.Bar({
                    element: 'speedchart_mysql',
                    data: dataIn,
                    xkey: 'index',
                    ykeys: ['value'],
                    labels: ['time(ms)']
                });


            });






        };

        this.activate = function(name){
            this.name(name);
        };
        this.attached = function(){

            self.reload();

        }
    };
    return ctor;
});