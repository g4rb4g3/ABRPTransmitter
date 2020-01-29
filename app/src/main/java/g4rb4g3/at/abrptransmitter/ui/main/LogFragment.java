package g4rb4g3.at.abrptransmitter.ui.main;

import android.os.Bundle;
import android.os.FileObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.MainApplication;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.asynctasks.LogFileLoader;

public class LogFragment extends Fragment implements LogFileLoader.ILogFileLoader {
  private static final Logger sLog = LoggerFactory.getLogger(LogFragment.class.getSimpleName());
  private FileObserver mFileObserver = null;
  private TextView mTvLog;

  public LogFragment() {
    // Required empty public constructor
  }

  public static LogFragment newInstance() {
    LogFragment fragment = new LogFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_log, container, false);
    mTvLog = view.findViewById(R.id.tv_log);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    File logFile = ((MainApplication) getActivity().getApplication()).getCurrentLogFile();

    final String parent = logFile.getParent();
    mFileObserver = new FileObserver(parent, FileObserver.MODIFY) {
      @Override
      public void onEvent(int event, @Nullable String path) {
        if (path == null || !path.endsWith(".log")) {
          return;
        }
        new LogFileLoader(LogFragment.this).execute(parent + "/" + path);
      }
    };
  }

  @Override
  public void onResume() {
    super.onResume();
    mTvLog.setText(getString(R.string.please_wait_logs));
    File logFile = ((MainApplication) getActivity().getApplication()).getCurrentLogFile();
    new LogFileLoader(this).execute(logFile.getAbsolutePath());
  }

  @Override
  public void onPause() {
    super.onPause();

    mFileObserver.stopWatching();
  }

  @Override
  public void updateText(final String s) {
    if (s != null) {
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mTvLog.setText(s);
        }
      });
    }
    mFileObserver.startWatching();
  }
}
