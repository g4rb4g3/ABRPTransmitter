/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

// Establish connection with app
let port = browser.runtime.connectNative("browser");
port.onMessage.addListener(response => {
  try {
    let code = '';
    if(response.zoomIn) {
      code = 'document.getElementById("zoomin").click()';
    } else if(response.zoomOut) {
      code = 'document.getElementById("zoomout").click()';
    }
    if(code != '') {
      browser.tabs.executeScript({
        code: code
      });
    }
  } catch (e) {
    port.postMessage(e);
  }
});
port.postMessage("Hello from WebExtension!");