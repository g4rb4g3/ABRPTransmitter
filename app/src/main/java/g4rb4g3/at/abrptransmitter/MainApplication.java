package g4rb4g3.at.abrptransmitter;

import android.app.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;

public class MainApplication extends Application {
  private static final Logger sLog = LoggerFactory.getLogger(MainApplication.class.getSimpleName());
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
