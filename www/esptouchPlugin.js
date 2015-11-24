var exec    = require('cordova/exec'),
cordova = require('cordova');

module.exports = {
	smartConfig:function(apSsid,apBssid,apPassword,isSsidHiddenStr,taskResultCountStr,successCallback, errorCallback){
		exec(successCallback, errorCallback, "esptouchPlugin", "smartConfig", [apSsid,apBssid,apPassword,isSsidHiddenStr,taskResultCountStr]);
	},
	
	cancelConfig:function(successCallback, errorCallback){
		exec(successCallback, errorCallback, "esptouchPlugin", "cancelConfig", []);
	}
}