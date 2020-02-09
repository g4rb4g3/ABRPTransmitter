package g4rb4g3.at.abrptransmitter.asynctasks;

import android.os.AsyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LogFileLoader extends AsyncTask<String, String, String> {
  private static final Logger sLog = LoggerFactory.getLogger(LogFileLoader.class.getSimpleName());
  private ILogFileLoader mDelegate;

  public LogFileLoader(ILogFileLoader delegate) {
    mDelegate = delegate;
  }

  @Override
  protected String doInBackground(String... strings) {
    try {
      return readFromLast(new File(strings[0]), 100);
    } catch (IOException e) {
      sLog.error("error reading log file", e);
    }
    return null;
  }

  @Override
  protected void onPostExecute(final String s) {
    super.onPostExecute(s);
    mDelegate.updateText(s);
  }

  private String readFromLast(File file, int lines) throws IOException {
    int readLines = 0;
    StringBuilder builder = new StringBuilder();
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
      long fileLength = file.length() - 1;
      randomAccessFile.seek(fileLength);
      for (long pointer = fileLength; pointer >= 0; pointer--) {
        randomAccessFile.seek(pointer);
        char c = (char) randomAccessFile.read();
        if (c == '\n') {
          readLines++;
          if (readLines == lines)
            break;
        }
        builder.insert(0, c);
      }
      return builder.toString();
    } finally {
      if (randomAccessFile != null) {
        randomAccessFile.close();
      }
    }
  }

  public interface ILogFileLoader {
    void updateText(String s);
  }
}
