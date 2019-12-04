package g4rb4g3.at.abrptransmitter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

public class CompanionDataExchanger implements Runnable {
  public static final int MSG_TOKEN = 1;

  private static final int EXCHANGE_PORT = 6942;
  private volatile boolean stop = false;
  private ServerSocket mServerSocket;
  private Handler mCompMsgHandler;

  public CompanionDataExchanger(Handler handler) {
    mCompMsgHandler = handler;
  }

  @Override
  public void run() {
    try {
      mServerSocket = new ServerSocket(EXCHANGE_PORT);
      while (!stop) {
        Socket socket = mServerSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while ((line = in.readLine()) != null) {
          try {
            JSONObject jsonObject = new JSONObject(line);
            for(Iterator<String> iterator = jsonObject.keys(); iterator.hasNext();) {
              String key = iterator.next();
              switch(key) {
                case ABetterRoutePlanner.ABETTERROUTEPLANNER_URL_TOKEN:
                  Message msg = new Message();
                  msg.what = MSG_TOKEN;
                  msg.obj = jsonObject.getString(key);
                  mCompMsgHandler.sendMessage(msg);
                  break;
                default:
                  Log.d(MainActivity.TAG, "unknown json key: " + key + " with value: " + jsonObject.get(key));
                  break;
              }
            }
          } catch (JSONException e) {
            Log.d(MainActivity.TAG, "skipping non json message [" + line + "]", e);
          }
        }
        socket.close();
      }
      mServerSocket.close();
    } catch (IOException e) {
      Log.e(MainActivity.TAG, "CompanionDataExchanger.run()", e);
    }
  }

  public void stop() {
    stop = true;
    try {
      mServerSocket.close();
    } catch (IOException e) {
      Log.e(MainActivity.TAG, "CompanionDataExchanger.stop()", e);
    }
  }
}
