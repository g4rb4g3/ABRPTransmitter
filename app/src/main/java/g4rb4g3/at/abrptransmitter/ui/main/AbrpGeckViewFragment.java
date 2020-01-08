package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mozilla.geckoview.AllowOrDeny;
import org.mozilla.geckoview.GeckoResult;
import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import g4rb4g3.at.abrptransmitter.R;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL;
import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL_NOMAP;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NAME;
import static g4rb4g3.at.abrptransmitter.Constants.PREFERENCES_NOMAP;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AbrpGeckViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AbrpGeckViewFragment extends Fragment {
  GeckoView mGeckoView;
  GeckoSession mGeckoSession;
  GeckoRuntime mGeckoRuntime;

  public AbrpGeckViewFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment AbrpGeckViewFragment.
   */
  public static AbrpGeckViewFragment newInstance() {
    AbrpGeckViewFragment fragment = new AbrpGeckViewFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_abrp_geck_view, container, false);

    if (mGeckoView == null) {
      mGeckoView = view.findViewById(R.id.gv_abrp);
      mGeckoSession = new GeckoSession();
      mGeckoRuntime = GeckoRuntime.create(getContext());

      mGeckoSession.open(mGeckoRuntime);
      mGeckoView.setSession(mGeckoSession);

      boolean nomap = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getBoolean(PREFERENCES_NOMAP, false);
      String url = ABETTERROUTEPLANNER_URL;
      if (nomap) {
        url += ABETTERROUTEPLANNER_URL_NOMAP;
      }
      mGeckoSession.loadUri(url);

      mGeckoSession.setNavigationDelegate(new GeckoSession.NavigationDelegate() {
        @Nullable
        @Override
        public GeckoResult<AllowOrDeny> onLoadRequest(@NonNull GeckoSession session, @NonNull LoadRequest request) {
          if (request.uri.startsWith("geo:")) {
            String[] parts = request.uri.replace("geo:", "").replaceFirst("\\?.*\\(", ",").replace(")", "").split(",");
            String lat = parts[0];
            String lon = parts[1];
            String name;
            try {
              name = URLDecoder.decode(parts[2], "UTF-8");
            } catch (UnsupportedEncodingException e) {
              name = "";
            }

            Intent intent = new Intent();
            intent.setAction("com.hkmc.intent.action.ACTION_ROUTE_SEARCH");
            intent.putExtra("com.hkmc.navi.EXTRA_LATITUDE", lat);
            intent.putExtra("com.hkmc.navi.EXTRA_LONGITUDE", lon);
            intent.putExtra("com.hkmc.navi.EXTRA_KEYWORD", name);
            getContext().sendBroadcast(intent);
          }
          return null;
        }
      });
    }
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }
}
