$(document).ready(function(){
	
  var IMG_SRC = "./img/football-player.gif";
  var ANIMATION_SPEED = 2250;
  var MAX_VALUE = 100;
  
  var $sea = $("#red_sea");
  var height = 50;
  var title = $("title")[0];
  var entering = 0;
  var foo=new Sound("./sounds/grunt.mp3",100,false);
  var crowd=new Sound("./sounds/crowd.mp3",100,false);
  var cheer=new Sound("./sounds/cheer.mp3",100,false);
  var starting = 0;
 
  var animateIn = function(){
    if(height + entering >= MAX_VALUE){
      return;
    }
    if (height + entering == MAX_VALUE - 1 && starting == 0) {
      crowd.start();
      starting = 1;
    }
    entering++;
    starting = 0;
    var $img = $("<img src='" + IMG_SRC + "' style='z-index: 3;' />");
    $("#background").prepend($img);
    $img.css("top", (Math.random() * (window.innerHeight - $img.height())) + "px");
    $img.animate({left: (window.innerWidth / 2) + "px",
                   top: (window.innerHeight / 2) + "px"},
      ANIMATION_SPEED,
      "linear", 
      function(){
        $(this).remove();
        height += 1;
        title.text = "Occupied: " + height + "%";
        $sea.css("height", (height) + "%");
        // added this transition again for a more liquid "feel", but feel free to remove it
        $sea.css('transition', 'height 1s ease');
        foo.start();
        entering--;
      });
  };


  var animateOut = function(){
    if(height <= 0){
      return;
    }
    if (height + entering == MAX_VALUE) {
      crowd.stop();
    }
    // WARNING: If you uncomment this, do not hold down or spam a key press.  Things can get loud quickly.
    cheer.start();
    height -= 1;
    title.text = "Occupied: " + height + "%";
    $sea.css("height", (height) + "%");
    // added this transition again for a more liquid "feel", but feel free to remove it
    $sea.css('transition', 'height 1s ease');

    var $img = $("<img src='" + IMG_SRC + "' style='z-index: 3;' />");
    $("#background").prepend($img);

    $img.css({left: (window.innerWidth / 2) + "px",
                   top: (window.innerHeight / 2) + "px"});
    
    $img.animate({left: (window.innerWidth - $img.width()) + "px",
                   top: (Math.random() * (window.innerHeight - $img.height())) + "px"},
      ANIMATION_SPEED,
      "linear", 
      function(){
        $(this).remove();
      });
  };
  
  setInterval(function(){
	    $.ajax({
	    	  type: "GET",
	      	  url: "./src/WebClassServlet",
	      	  data: { CurrentCapacity: height},
	      	  success: function(result){
	      		  automatePeople(result);
	      	  }
	      }); 
  }, 5000);
  
  
  var automatePeople = function(people){
	  var newPeople = parseInt(people);
	  if(newPeople > 0){
		  for(var i =0; i < newPeople; i++){
			  animateIn();
		  }
	  }
	  else{
		  newPeople *= -1;
		  for(var i =0; i < newPeople; i++){
			  animateOut();
		  }
	  }
  };
  
  
  $("body").on("click", function(){
    animateIn();    
  });

  $("body").on("keypress", function(){
    animateOut();
  });
});