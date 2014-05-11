define(['knockout','visualization','jquery'],function(ko,visualization,$) {
    var ctor = function () {

          var self = this;

          this.attached= function(){
              $.getJSON("neo4j").done(function(data){

                  visualization.visualize("graphcontainer",2000,400,data);

              });
          }
    };
    return ctor;
});