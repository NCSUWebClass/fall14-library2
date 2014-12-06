$(document).ready(function(){
	var canvas = $("#animated_sea")[0];
	  var ctx = canvas.getContext("2d");
	  var MAX_HEIGHT = 3;
	  
	  ctx.fillStyle = "#CC0000";
	  
	  var heights = new Array(Math.floor(canvas.width / 2));
	  
	  
	  var dampen = function(x){
	    x = x * Math.PI / 180;
	    return MAX_HEIGHT /* Math.exp(-x / 4)*/ * Math.sin(x * 6);
	  };
	  
	  for(var i = 0; i < heights.length; i++){
	    heights[i] = dampen(-i);
	  }
	  
	  var render = function( level ){
	    ctx.clearRect(0, 0, canvas.width, canvas.height);
	    ctx.beginPath();
	    
	    ctx.moveTo(canvas.width, canvas.height);
	    ctx.lineTo(0, canvas.height);
	    var dx = Math.round(canvas.width / heights.length);
	    var i = 0;
	    for(; i < heights.length - 1; i++){
	      ctx.lineTo(dx * i, canvas.height - (level + heights[i]));
	    }
	    ctx.lineTo(canvas.width, canvas.height - (level + heights[i]));
	    ctx.closePath();
	    ctx.fill();
	  };
	  
	    var ctr = 0;
	  var animate = function(fromLeft){
	    setInterval(function(){
	      if(fromLeft){
	        for(var i = heights.length - 1; i > 0; i--){
	          heights[i] = heights[i - 1];
	        }
	        heights[0] = dampen(ctr);
	        ctr += 1;
	      } else {
	        for(var i = heights.length - 1; i >= 0; i--){
	          heights[i - 1] = heights[i];
	        }
	        ctr += 1;
	        heights[heights.length - 1] = dampen(ctr);
	      }
	      render(MAX_HEIGHT);
	    }, 23);
	  };
	  animate(true);

});