var abrptransmitterTlm = null;
var getJSON = $.getJSON;

$.getJSON = function (e, t, n) {
  console.log(e);
  if (e.indexOf("get_sharing_telemetry") == -1 && e.indexOf("get_vehicle_telemetry") == -1 && e.indexOf("get_user_telemetry") == -1) {
    return getJSON.apply(this, arguments);
  }

  var deferred = $.Deferred();
  try {
    if(abrptransmitterTlm == null) {
      deferred.resolve({"status": "error"});
    } else {
      deferred.resolve({"status": "ok", "result": abrptransmitterTlm});
    }
  } catch (e) {
    deferred.reject(e);
  }
  return deferred.promise();
}