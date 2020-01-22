package g4rb4g3.at.abrptransmitter;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import g4rb4g3.at.abrptransmitter.ui.main.AbrpGeckViewFragment;
import g4rb4g3.at.abrptransmitter.ui.main.SectionsPagerAdapter;

import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_DOWN;
import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_PRESS;
import static com.lge.ivi.view.IviKeyEvent.KEYCODE_TUNE_UP;
import static g4rb4g3.at.abrptransmitter.ui.main.SectionsPagerAdapter.TAB_TITLES;

public class MainActivity extends AppCompatActivity {

  boolean mFullscreen = false;
  SectionsPagerAdapter mSectionsPageAdapter;
  TabLayout mTabs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mSectionsPageAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(mSectionsPageAdapter);
    mTabs = findViewById(R.id.tabs);
    mTabs.setupWithViewPager(viewPager);

    TextClock textClock = findViewById(R.id.textclock);

    Button btnToggleFullscreen = findViewById(R.id.btn_toggle_fullscreen);
    btnToggleFullscreen.setOnClickListener(v -> {
      if (!mFullscreen) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        btnToggleFullscreen.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_fullscreen_exit), null, null, null);
        textClock.setVisibility(View.VISIBLE);
      } else {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        btnToggleFullscreen.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_fullscreen), null, null, null);
        textClock.setVisibility(View.GONE);
      }
      mFullscreen = !mFullscreen;
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mFullscreen) {
      int visibility = getWindow().getDecorView().getVisibility();
      if ((visibility & View.SYSTEM_UI_FLAG_VISIBLE) == 0) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
      }
    }
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    switch (event.getKeyCode()) {
      case KEYCODE_TUNE_UP:
      case KEYCODE_TUNE_DOWN:
      case KEYCODE_TUNE_PRESS:
        return notifyFragment(event.getKeyCode(), event.getAction());
      default:
        return super.dispatchKeyEvent(event);
    }
  }

  private boolean notifyFragment(int keycode, int action) {
    int position = mTabs.getSelectedTabPosition();
    if (TAB_TITLES[position] == R.string.tab_abrp_geckoview) {
      ((AbrpGeckViewFragment) mSectionsPageAdapter.getFragment(position)).onKeyEvent(keycode, action);
      return true;
    }
    return false;
  }
}