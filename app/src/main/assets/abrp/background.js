function onError(error) {
  port.postMessage(error);
}

// Establish connection with app
let port = browser.runtime.connectNative("browser");
port.onMessage.addListener(response => {
  try {
    let code = null;
    if(response.zoomIn) {
      code = 'document.getElementById("zoomin").click()';
    } else if(response.zoomOut) {
      code = 'document.getElementById("zoomout").click()';
    } else if (response.toggleNight) {
      code = 'document.getElementById("toolbar-nightbutton").click()';
    } else if(response.setCss) {
      browser.tabs.query({}).then((tabs) => {
        try {
          for (let tab of tabs) {
            browser.tabs.insertCSS(tab.id, {file: "abrptransmitter.css"});
          }
        } catch (e) {
          onError(e);
        }
      }).catch(onError);
    } else if(response.removeCss) {
      browser.tabs.query({}).then((tabs) => {
        try {
          for (let tab of tabs) {
            browser.tabs.removeCSS(tab.id, {file: "abrptransmitter.css"});
          }
        } catch (e) {
          onError(e);
        }
      }).catch(onError);
    } else if(response.lat && response.lon) {
      browser.tabs.query({}).then((tabs) => {
        try {
          for (let tab of tabs) {
            browser.tabs.sendMessage(tab.id, {"tlm": response});
          }
        } catch (e) {
          onError(e);
        }
      }).catch(onError);
    }
    if(code != null) {
      browser.tabs.query({}).then((tabs) => {
        try {
          for (let tab of tabs) {
            browser.tabs.executeScript(tab.id, {code: code});
          }
        } catch (e) {
          onError(e);
        }
      }).catch(onError);
    }
  } catch (e) {
    onError(e);
  }
});
