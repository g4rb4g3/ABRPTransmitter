package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.Context;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import g4rb4g3.at.abrptransmitter.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

  @StringRes
  public static final int[] TAB_TITLES = new int[]{R.string.tab_abrp_geckoview, R.string.tab_settings, R.string.tab_logs, R.string.tab_information};
  private final Context mContext;
  private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

  public SectionsPagerAdapter(Context context, FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return AbrpGeckViewFragment.newInstance();
      case 1:
        return SettingsFragment.newInstance();
      case 2:
        return LogFragment.newInstance();
      case 3:
        return InformationFragment.newInstance();
      default:
        throw new RuntimeException("unknown fragment called");
    }
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mContext.getResources().getString(TAB_TITLES[position]);
  }

  @Override
  public int getCount() {
    return TAB_TITLES.length;
  }

  @Override
  public Object instantiateItem(final ViewGroup container, final int position) {
    final Fragment fragment = (Fragment) super.instantiateItem(container, position);
    instantiatedFragments.put(position, new WeakReference<>(fragment));
    return fragment;
  }

  @Override
  public void destroyItem(final ViewGroup container, final int position, final Object object) {
    instantiatedFragments.remove(position);
    super.destroyItem(container, position, object);
  }

  @Nullable
  public Fragment getFragment(final int position) {
    final WeakReference<Fragment> wr = instantiatedFragments.get(position);
    if (wr != null) {
      return wr.get();
    } else {
      return null;
    }
  }
}