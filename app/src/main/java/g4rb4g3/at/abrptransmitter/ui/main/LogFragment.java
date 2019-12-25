package g4rb4g3.at.abrptransmitter.ui.main;

import android.os.Bundle;
import android.os.FileObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.MainApplication;
import g4rb4g3.at.abrptransmitter.R;

public class LogFragment extends Fragment {
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

    File logFile = ((MainApplication) getActivity().getApplication()).getCurrentLogFile();
    showLog(logFile);

    final String parent = logFile.getParent();
    mFileObserver = new FileObserver(parent, FileObserver.MODIFY) {
      @Override
      public void onEvent(int event, @Nullable String path) {
        if (path == null || !path.endsWith(".log")) {
          return;
        }
        File file = new File(parent + "/" + path);
        showLog(file);
      }
    };
    return view;
  }
  
  @Override
  public void onResume() {
    super.onResume();

    mFileObserver.startWatching();
  }

  @Override
  public void onPause() {
    super.onPause();

    mFileObserver.stopWatching();
  }

  private void showLog(File file) {
    try {
      final StringBuilder sb = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        sb.insert(0, line + '\n');
      }
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mTvLog.setText(sb.toString());
        }
      });
    } catch (IOException e) {
      sLog.error("", e);
    }
  }
}
