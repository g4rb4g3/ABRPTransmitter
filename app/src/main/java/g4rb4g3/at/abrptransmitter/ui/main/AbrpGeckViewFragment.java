package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.mozilla.geckoview.GeckoRuntime;
import org.mozilla.geckoview.GeckoSession;
import org.mozilla.geckoview.GeckoView;

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;

import static g4rb4g3.at.abrptransmitter.Constants.ABETTERROUTEPLANNER_URL;

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
      mGeckoSession.loadUri(ABETTERROUTEPLANNER_URL);
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
