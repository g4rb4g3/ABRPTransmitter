package g4rb4g3.at.abrptransmitter.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

import javax.net.ssl.HttpsURLConnection;

import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.TLSSocketFactory;

import static g4rb4g3.at.abrptransmitter.Constants.ABRPTRANSMITTER_APK_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.ABRPTRANSMITTER_RELEASE_URL;

public class AbrpTransmitterReleaseLoader extends AsyncTask<String, String, String> {

  private static final Logger sLog = LoggerFactory.getLogger(AbrpTransmitterReleaseLoader.class.getSimpleName());

  private Context mContext;
  private IAbrpTransmitterReleaseLoader mIAbrpTransmitterReleaseLoader;

  public AbrpTransmitterReleaseLoader(Context context, IAbrpTransmitterReleaseLoader delegate) {
    mContext = context;
    mIAbrpTransmitterReleaseLoader = delegate;
  }

  @Override
  protected String doInBackground(String... params) {
    HttpsURLConnection connection = null;
    BufferedReader reader = null;
    String error = "";

    try {
      URL url = new URL(params[0]);
      connection = (HttpsURLConnection) url.openConnection();
      connection.setSSLSocketFactory(new TLSSocketFactory());
      if (params[0].equals(ABRPTRANSMITTER_RELEASE_URL)) {
        mIAbrpTransmitterReleaseLoader.setProgressDialogMessage(R.string.please_wait_loading_releaes);
        connection.connect();
        InputStream stream = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line;

        while ((line = reader.readLine()) != null) {
          buffer.append(line);

        }
        return buffer.toString();
      } else {
        mIAbrpTransmitterReleaseLoader.setProgressDialogMessage(R.string.please_wait_downloading);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

          InputStream stream = connection.getInputStream();

          //File outDir = mContext.getExternalFilesDir(null);
          File outFile = new File("/sdcard", ABRPTRANSMITTER_APK_NAME);
          FileOutputStream fileOutputStream = new FileOutputStream(outFile);
          byte[] buffer = new byte[1024];
          int bufferLength;

          while ((bufferLength = stream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, bufferLength);
          }
          fileOutputStream.close();

          return outFile.getAbsolutePath();
        }
      }
    } catch (IOException e) {
      sLog.error(e.getMessage(), e);
      error = e.getLocalizedMessage();
    } catch (NoSuchAlgorithmException e) {
      sLog.error(e.getMessage(), e);
      error = e.getLocalizedMessage();
    } catch (KeyManagementException e) {
      sLog.error(e.getMessage(), e);
      error = e.getLocalizedMessage();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        sLog.error(e.getMessage(), e);
      }
    }
    return error;
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    if (result.startsWith("[") && result.endsWith("]")) {
      try {
        JSONArray jsonArray = new JSONArray(result);
        LinkedHashMap<String, String> releases = new LinkedHashMap<>(jsonArray.length() + 1, 1.0f);
        releases.put(mContext.getString(R.string.choose_release), "");
        String[] spinnerContent = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jsonObject = jsonArray.getJSONObject(i);
          String name = jsonObject.getString("name");
          String url = jsonObject.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
          releases.put(name, url);
          spinnerContent[i] = name;
        }
        mIAbrpTransmitterReleaseLoader.releasesLoadComplete(releases);
      } catch (JSONException e) {
        sLog.error("error getting github releases", e);
      }
    } else if(result.endsWith(ABRPTRANSMITTER_APK_NAME)) {
      mIAbrpTransmitterReleaseLoader.downloadComplete(result);
    } else {
      mIAbrpTransmitterReleaseLoader.loadFailed(result);
    }
  }

  public interface IAbrpTransmitterReleaseLoader {
    void releasesLoadComplete(LinkedHashMap<String, String> result);

    void downloadComplete(String result);

    void setProgressDialogMessage(int id);

    void loadFailed(String msg);
  }
}