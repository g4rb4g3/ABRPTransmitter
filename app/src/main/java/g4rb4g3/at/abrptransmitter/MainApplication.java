package g4rb4g3.at.abrptransmitter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LogLevel;
import pl.brightinventions.slf4android.LoggerConfiguration;

import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_LOG_LEVEL;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;

public class MainApplication extends Application {
  private FileLogHandlerConfiguration mFileHandler;

  @Override
  public void onCreate() {
    super.onCreate();
    setLogLevel();

    mFileHandler = LoggerConfiguration.fileLogHandler(this);
    LoggerConfiguration.configuration().addHandlerToRootLogger(mFileHandler);
  }

  public File getCurrentLogFile() {
    return new File(mFileHandler.getCurrentFileName());
  }

  public void setLogLevel() {
    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    String logLevel = sharedPreferences.getString(PREFERENCES_LOG_LEVEL, "error");
    switch (logLevel) {
      case "error":
        LoggerConfiguration.configuration().setRootLogLevel(LogLevel.ERROR);
        break;
      case "warn":
        LoggerConfiguration.configuration().setRootLogLevel(LogLevel.WARNING);
        break;
      case "info":
        LoggerConfiguration.configuration().setRootLogLevel(LogLevel.INFO);
        break;
      case "debug":
        LoggerConfiguration.configuration().setRootLogLevel(LogLevel.DEBUG);
        break;
    }
  }
}
