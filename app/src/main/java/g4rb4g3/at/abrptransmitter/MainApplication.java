package g4rb4g3.at.abrptransmitter;

import android.app.Application;

import java.io.File;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;

public class MainApplication extends Application {
  private FileLogHandlerConfiguration mFileHandler;

  @Override
  public void onCreate() {
    super.onCreate();

    mFileHandler = LoggerConfiguration.fileLogHandler(this);
    LoggerConfiguration.configuration().addHandlerToRootLogger(mFileHandler);
  }

  public File getCurrentLogFile() {
    return new File(mFileHandler.getCurrentFileName());
  }
}
