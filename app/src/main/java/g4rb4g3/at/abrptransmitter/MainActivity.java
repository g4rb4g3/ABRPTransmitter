package g4rb4g3.at.abrptransmitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  public static final String PREFERENCES_NAME = "preferences";
  public static final String PREFERENCES_MAIL = "abrp_mail";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onResume() {
    super.onResume();

    final SharedPreferences sp = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    final TextView tvMail = findViewById(R.id.tv_abrp_mail);
    tvMail.setText(sp.getString(PREFERENCES_MAIL, ""));

    Button btSave = findViewById(R.id.save);
    btSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SharedPreferences.Editor sped = sp.edit();
        sped.putString(PREFERENCES_MAIL, tvMail.getText().toString());
        sped.commit();
      }
    });
  }
}
