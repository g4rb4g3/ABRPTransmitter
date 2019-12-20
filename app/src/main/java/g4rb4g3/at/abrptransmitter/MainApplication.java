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

    sLog.info(getString(R.string.app_name) + " started");
  }

  public File getCurrentLogFile() {
    return new File(mFileHandler.getCurrentFileName());
  }
}
