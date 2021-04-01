package g4rb4g3.at.abrptransmitter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import g4rb4g3.at.abrptransmitter.ui.main.SectionsPagerAdapter;
import g4rb4g3.at.abrptransmitter.ui.main.SwitchableSwipeViewPager;

public class MainActivity extends AppCompatActivity {

  boolean mFullscreen = false;
  SectionsPagerAdapter mSectionsPageAdapter;
  TabLayout mTabs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mSectionsPageAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    SwitchableSwipeViewPager viewPager = findViewById(R.id.view_pager);
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
}