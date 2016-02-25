
// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});

var view = Ti.UI.createView();

var label = Ti.UI.createLabel({
		color:'#000000',
		text:String.format(L('welcome'),'Titanium'),
		height:'auto',
		width:'auto'
});
view.add(label);


var tipermissions = require('ti.permissions');  // will return same instance as other require calls

//Add behavior for UI
label.addEventListener('click', function(e) {
	  hasPerm = tipermissions.hasPermission("android.permission.CAMERA");
      Ti.API.log("error","Critical Permission " + hasPerm);

      tipermissions.requestPermission("android.permission.CAMERA",123,function(e) {
                 Ti.API.log("error","In callback " + e.success + " " + e.code + " " + e.requestCode);
              });

	});

win.add(view);
win.open();



