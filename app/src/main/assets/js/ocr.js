function App(){
	this.resizeables = [];
	window.onresize = App.prototype.doResize;
}

App.prototype.doResize = function(){
	for(var x = 0; x < app.resizeables.length; x++){
			var options = app.resizeables[x];
			var elem = $("#"+options.id);
			var dim = App.prototype.getDim();
			var keys = Object.keys(options);
			for(var y = 0; y < keys.length; y++){
				var key = keys[y];
				var value = options[key];
				if(key === "id") continue; //skip id
				var on = value.on;
				var dimen;
				if(on === "width"){
					dimen = value.factor*dim[0]+"px";
				}
				else if(on === "height") {
					dimen = value.factor*dim[1]+"px";
				}
				elem.css(key,dimen);
				
			}
		}
};

App.prototype.resize = function(options){
	this.resizeables.push(options);
	App.prototype.doResize();
};

App.prototype.getDim = function(){
    var body = window.document.body;
    var screenHeight;
    var screenWidth;
    if (window.innerHeight) {
        screenHeight = window.innerHeight;
        screenWidth = window.innerWidth;
    }
    else if (body.parentElement.clientHeight) {
        screenHeight = body.parentElement.clientHeight;
        screenWidth = body.parentElement.clientWidth;
    }
    else if (body && body.clientHeight) {
        screenHeight = body.clientHeight;
        screenWidth = body.clientWidth;
    }
    return [screenWidth, screenHeight];  
};


App.prototype.loadContent = function(content){
	$("#container").html(content);
	console.log("load Content called");
}

App.prototype.animate = function(){
	$("#container").html("<div id='anim_one' class='anim_area'></div>"+
		"<div id='anim_two' class='anim_area'></div>"+
		"<div id='anim_three' class='anim_area'></div>");
	var red,blue,green;
	var rand = Math.random();
	if(rand > 0.9){
		red = 40;
		blue = 20;
		green = 10;
	}
	else if(rand > 0.6){
	    red = 20;
		blue = 40;
		green = 10;
	}
   else {
   		red = 20;
		blue = 10;
		green = 40;
   }
	animate.start("anim_one",red,100,0.9*app.getDim()[0],0,"red");//id,dx,delay,max,direction
	animate.start("anim_two",blue,100,0.9*app.getDim()[0],0,"blue");
	animate.start("anim_three",green,100,0.9*app.getDim()[0],0,"green");
	console.log("animate called");
}

window.app = new App();