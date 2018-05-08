d3.csv(
	"average_delays_2015.csv", function(data) {

		.row(function(d){
			return {
				airline:String(d.airline),percentage_delay:Number();
			}
		})

  		
		});