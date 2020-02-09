var tlm = null;

// Establish connection with app
let port = browser.runtime.connectNative("browser");
port.onMessage.addListener(response => {
  try {
    let code = '';
    if(response.zoomIn) {
      code = 'document.getElementById("zoomin").click()';
    } else if(response.zoomOut) {
      code = 'document.getElementById("zoomout").click()';
    } else if (response.toggleNight) {
      code = 'document.getElementById("toolbar-nightbutton").click()';
    } else if(response.setCss) {
      browser.tabs.insertCSS({file: "abrptransmitter.css"});
    } else if(response.removeCss) {
      browser.tabs.removeCSS({file: "abrptransmitter.css"});
    } else if(response.lat && response.lon) {
      tlm = response;
      browser.tabs.query({ currentWindow: true, active: true }).then(sendMessageToTabs).catch(onError);
    }
    if(code != '') {
      browser.tabs.executeScript({
        code: code
      });
    }
  } catch (e) {
    onError(e);
  }
});

function onError(error) {
  //console.log(error);
  port.postMessage(error);
}

function sendMessageToTabs(tabs) {
  try {
    for (let tab of tabs) {
      browser.tabs.sendMessage(tab.id, {"tlm": tlm});
    }
  } catch (e) {
    onError(e);
  }
}