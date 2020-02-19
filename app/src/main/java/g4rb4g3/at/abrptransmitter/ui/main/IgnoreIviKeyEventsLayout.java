package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.lge.ivi.view.IviKeyEvent;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class IgnoreIviKeyEventsLayout extends ConstraintLayout {

  public IgnoreIviKeyEventsLayout(Context context) {
    super(context);
  }

  public IgnoreIviKeyEventsLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public IgnoreIviKeyEventsLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    switch (event.getKeyCode()) {
      case IviKeyEvent.KEYCODE_TUNE_PRESS:
      case IviKeyEvent.KEYCODE_TUNE_UP:
      case IviKeyEvent.KEYCODE_TUNE_DOWN:
      case IviKeyEvent.KEYCODE_TRACK_SEEK_UP:
      case IviKeyEvent.KEYCODE_TRACK_SEEK_DOWN:
      case IviKeyEvent.KEYCODE_SEEK_UP:
      case IviKeyEvent.KEYCODE_SEEK_DOWN:
        return false;
      default:
        return super.dispatchKeyEvent(event);
    }
  }
}
