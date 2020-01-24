package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_DISABLE_TAB_SWIPE;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;

public class SwitchableSwipeViewPager extends ViewPager {
  private static SwitchableSwipeViewPager mInstance;
  private boolean mSwipeEnabled;

  public SwitchableSwipeViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    mInstance = this;
    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    mSwipeEnabled = !sharedPreferences.getBoolean(PREFERENCES_DISABLE_TAB_SWIPE, false);
  }

  public static SwitchableSwipeViewPager getInstance() {
    return mInstance;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return mSwipeEnabled && super.onTouchEvent(event);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    return mSwipeEnabled && super.onInterceptTouchEvent(event);
  }

  public void setPagingEnabled(boolean enabled) {
    this.mSwipeEnabled = enabled;
  }
}
