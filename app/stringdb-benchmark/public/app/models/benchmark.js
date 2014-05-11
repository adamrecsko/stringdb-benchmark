define(['knockout','komapping'],function(ko,komapping) {
    return function Benchmark(data) {

        this.value = ko.observable();
        this.description = ko.observable();
        this.label = ko.observable();
        this.key = ko.observable();




        komapping.fromJS(data, {}, this);


    };
});