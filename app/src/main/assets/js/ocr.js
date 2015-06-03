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


window.app = new App();