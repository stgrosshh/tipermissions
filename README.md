# OBSOLETE and ABANDONED 
## Use new of API of Ti SDK 5.4.0 or later instead

## NOTE (Implementation hints)

After merging of PR #7778 the Appcelerator guys unfortunately decided
to strip down the functionality of the implementation.

So there is no way to support this module with the current API anymore :-(
Sorry for that!

The official API is now part of Ti.Android and can be used as follows:

```javascript

  // check permission

  var storagePermission = "android.permission.READ_EXTERNAL_STORAGE";
  var hasStoragePermission = Ti.Android.hasPermission(storagePermission);

  // request permission

  Ti.Android.requestPermissions(permissionsToRequest, function(e) {
      if (e.success) {
          Ti.API.info("SUCCESS");
      } else {
          Ti.API.info("ERROR: " + e.error);
      }
  });


```

### Breaking changes:
* no checking of of parameters any longer, so you will get no hint in case of typos in permissions
* you wil get the denied permissions as a comma separated string in the error property instead of having
  clean permissions and denied properties in the result
* you cannot provide a request code for correlation and processing in a central handler anymore, but a
  fixed request code is used internally

---

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

boolean tipermissions.requestPermission(String permission,fun callback(e), (optional) int8 requestCode)

### Requesting multiple permissions

boolean tipermissions.requestPermissions(String[] permission,fun callback(e), (optional) int8 requestCode )


## Usage

First initialize the module with require as above.

Call the requestPermission method, which is defined as follows:

  boolean requestPermission("permission",function(result){ do something })

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

upcoming new pull request (still in development) will provide also:
 permissions[] requested permissions
 denied[] denied permissions

# NOTE: requires integration of Pull Request #7778 ! 
https://github.com/appcelerator/titanium_mobile/pull/7778

## Author

Stefan Gross https://github.com/stgrosshh

## License

MIT License