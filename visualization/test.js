d3.csv('average_delays_2015.csv')
  .row(function(d) {
    // for each row of the data, create an object with these properties...
    return {
          	percentageDelay: +d.PercentageDelay     
    };
  })
  .get(function(csv) {
   		var print = d3.percentageDelay;
   		console.log(print);
      // lets say you want to log all the temperatures to the console
      //csv.forEach(function(d,i) {
        //var theTime = d3.time.format('%I:%M %p')(d.time);
        //console.log('The temperature at', theTime, 'was', d.temperature, 'degrees.');
      //});
    
  });