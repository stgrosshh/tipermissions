# tipermissions Module

## Description

Permission handling module to support Android 6.0 and above permission behaviour.

Provides means to check and request permissions.
Allows to invoke a callback function with the permission request result.
So the App can react accordingly, when a dangerous permission has not been granted
by the user.

## Accessing the tipermissions Module

To access this module from JavaScript, you would do the following:

    var tipermissions = require("ti.permissions");

The tipermissions variable is a reference to the Module object.

## Reference


### Checking for permission

boolean tipermissions.hasPermission(permission)

### Requesting a single permission

boolean tipermissions.requestPermission(String permission,int8 requestCode, fun callback(e))

### Requesting multiple permissions

boolean tipermissions.requestPermissions(String[] permission,int8 requestCode, fun callback(e))


## Usage

First initialize the module with require as above.

Call the requestPermission method, which is defined as follows:

  boolean requestPermission("permission",requestCode, function(result){ do something })

The permission paramter has to be a string with a permission as used in Manifest,
e.g. "android.permission.WRITE_EXTERNAL_STORAGE".
Permission parameter will be checked against available Android permissions.
The module will return false, in case of an invalid permission.

The requestCode parameter has to be a 8 Bit int and allows to correlation of the
callback with a certain request. 

The requestCode will be provided in the callback function's result parameter as
result.requestCode. So you can use a central callback which handles
all the requests if you like.

Other result properties are:
 success (boolean) 
 code
 message (in case of an error)
 permissions[] requested permissions

# NOTE: requires integration of Pull Request #7778 ! 
https://github.com/appcelerator/titanium_mobile/pull/7778

## Author

Stefan Gross https://github.com/stgrosshh

## License

MIT License