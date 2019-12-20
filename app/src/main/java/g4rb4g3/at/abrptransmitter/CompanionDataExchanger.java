package g4rb4g3.at.abrptransmitter;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import g4rb4g3.at.abrptransmitter.service.AbrpTransmitterService;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_TOKEN;
import static g4rb4g3.at.abrptransmitter.Constants.COMPANION_EXCHANGE_PORT;
import static g4rb4g3.at.abrptransmitter.Constants.MSG_TOKEN;

public class CompanionDataExchanger implements Runnable {

  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterService.class.getSimpleName());

  private volatile boolean stop = false;
  private ServerSocket mServerSocket;
  private Handler mCompMsgHandler;

  public CompanionDataExchanger(Handler handler) {
    mCompMsgHandler = handler;
  }

  @Override
  public void run() {
    try {
      mServerSocket = new ServerSocket(COMPANION_EXCHANGE_PORT);
      while (!stop) {
        Socket socket = mServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while ((line = in.readLine()) != null) {
          try {
            JSONObject jsonObject = new JSONObject(line);
            for (Iterator<String> iterator = jsonObject.keys(); iterator.hasNext(); ) {
              String key = iterator.next();
              switch (key) {
                case ABETTERROUTEPLANNER_URL_TOKEN:
                  Message msg = new Message();
                  msg.what = MSG_TOKEN;
                  msg.obj = jsonObject.getString(key);
                  mCompMsgHandler.sendMessage(msg);
                  break;
                default:
                  sLog.debug("unknown json key: " + key + " with value: " + jsonObject.get(key));
                  break;
              }
            }
          } catch (JSONException e) {
            sLog.debug("skipping non json message [" + line + "]", e);
          }
        }
        socket.close();
      }
      mServerSocket.close();
    } catch (IOException e) {
      sLog.error("CompanionDataExchanger.run()", e);
    }
  }

  public void stop() {
    stop = true;
    try {
      mServerSocket.close();
    } catch (IOException e) {
      sLog.error("CompanionDataExchanger.stop()", e);
    }
  }
}
