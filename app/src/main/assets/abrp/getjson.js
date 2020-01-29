var abrptransmitterTlm = null;
var getJSON = $.getJSON;

$.getJSON = function (e, t, n) {
  console.log(e);
  if (!e.includes("get_sharing_telemetry") && !e.includes("get_vehicle_telemetry") && !e.includes("get_user_telemetry")) {
    return getJSON.apply(this, arguments);
  }

  var deferred = $.Deferred();
  try {
    getJSON.apply(this, arguments).done(function (data) {
      if (data.status == "ok") {
        var tlm_age = (new Date()).getTime() / 1000 - data.result.utc;
        if (tlm_age > 10 && abrptransmitterTlm != null) { // TODO: find good value here, ABRP assumes > 90s is bad but that would cause slow UI updates. We update each 5s, so at least two tlm sets should come in here
          Object.assign(data.result, abrptransmitterTlm); // this will overwrite the remote result with local data
        }
      }
      deferred.resolve(data)
    }).fail(function () {
      if (abrptransmitterTlm == null) {
        deferred.resolve({ "status": "error" });
      } else {
        deferred.resolve({ "status": "ok", "result": abrptransmitterTlm });
      }
    });
  } catch (e) {
    deferred.reject(e);
  }
  return deferred.promise();
}
