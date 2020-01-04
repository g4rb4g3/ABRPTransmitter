package g4rb4g3.at.abrptransmitter.asynctasks;

import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogFileLoader extends AsyncTask<String, String, String> {
  private static final Logger sLog = LoggerFactory.getLogger(LogFileLoader.class.getSimpleName());
  private ILogFileLoader mDelegate;

  public LogFileLoader(ILogFileLoader delegate) {
    mDelegate = delegate;
  }

  @Override
  protected String doInBackground(String... strings) {
    try {
      StringBuilder sb = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(strings[0])));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        sb.insert(0, line + '\n');
      }
      return sb.toString();
    } catch (IOException e) {
      sLog.error("", e);
    }
    return null;
  }

  @Override
  protected void onPostExecute(final String s) {
    super.onPostExecute(s);
    mDelegate.updateText(s);
  }

  public interface ILogFileLoader {
    void updateText(String s);
  }
}
