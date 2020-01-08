package g4rb4g3.at.abrptransmitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import g4rb4g3.at.abrptransmitter.service.CompanionExchangerService;
import g4rb4g3.at.abrptransmitter.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

    Button btnToggleFullscreen = findViewById(R.id.btn_toggle_fullscreen);
    btnToggleFullscreen.setOnClickListener(v -> {
      if (v.getTag() == null) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        v.setTag("");
        btnToggleFullscreen.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_fullscreen_exit), null, null, null);
      } else {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        v.setTag(null);
        btnToggleFullscreen.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_fullscreen), null, null, null);
      }
    });

    Intent intent = new Intent(this, CompanionExchangerService.class);
    startService(intent);
  }
}