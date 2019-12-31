package g4rb4g3.at.abrptransmitter.ui.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import g4rb4g3.at.abrptransmitter.R;
import g4rb4g3.at.abrptransmitter.service.AbrpConsumptionService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsumptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsumptionFragment extends Fragment implements View.OnClickListener, AbrpConsumptionService.IAbrpConsumptionService {

  private static final Logger sLog = LoggerFactory.getLogger(ConsumptionFragment.class.getSimpleName());
  private LineChart mLcConsumption;
  private ArrayList<Entry> mRealSocValues = new ArrayList<>();
  private AbrpConsumptionService mService;
  private boolean mBound;

  private ServiceConnection mServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      AbrpConsumptionService.AbrpConsumptionBinder binder = (AbrpConsumptionService.AbrpConsumptionBinder) service;
      mService = binder.getService();
      mBound = true;
      mService.addListener(ConsumptionFragment.this);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBound = false;
    }
  };

  public ConsumptionFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @return A new instance of fragment ConsumptionFragment.
   */
  public static ConsumptionFragment newInstance() {
    ConsumptionFragment fragment = new ConsumptionFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_consumption, container, false);
    mLcConsumption = view.findViewById(R.id.lc_consumption);
    setupChart();

    Button btnLoad = view.findViewById(R.id.btn_load);
    btnLoad.setOnClickListener(this);
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

  @Override
  public void onResume() {
    super.onResume();

    Intent intent = new Intent(getContext(), AbrpConsumptionService.class);
    getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onPause() {
    super.onPause();

    if (mBound) {
      mService.removeListener(this);
      getActivity().unbindService(mServiceConnection);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_load:
        Context context = getContext();
        Intent intent = new Intent(context, AbrpConsumptionService.class);
        context.startService(intent);
        break;
    }
  }

  private void setupChart() {
    // no description text
    mLcConsumption.getDescription().setEnabled(false);

    // enable / disable grid background
    // chart.setDrawGridBackground(true);

    // enable touch gestures
    mLcConsumption.setTouchEnabled(false);

    // enable scaling and dragging
    mLcConsumption.setDragEnabled(false);
    mLcConsumption.setScaleEnabled(false);

    // if disabled, scaling can be done on x- and y-axis separately
    mLcConsumption.setPinchZoom(false);

    // chart.setBackgroundColor(Color.rgb(255, 255, 255));

    // set custom chart offsets (automatic offset calculation is hereby disabled)
    //mLcConsumption.setViewPortOffsets(0, 0, 0, 0);

    // get the legend (only possible after setting data)
    Legend l = mLcConsumption.getLegend();
    l.setEnabled(false);

    mLcConsumption.getAxisLeft().setAxisMaximum(100);
    mLcConsumption.getAxisLeft().setAxisMinimum(0);
    mLcConsumption.getAxisLeft().setEnabled(true);

    mLcConsumption.getAxisRight().setEnabled(false);

    mLcConsumption.getXAxis().setEnabled(true);

    mLcConsumption.setAutoScaleMinMaxEnabled(true);
  }

  public void setChartData(ArrayList<Entry> heightValues, ArrayList<Entry> estimatedSocValues, ArrayList<Entry> realSocValues) {
    mRealSocValues = realSocValues;

    LineDataSet altitude = new LineDataSet(heightValues, "Altitude");
    altitude.setAxisDependency(YAxis.AxisDependency.RIGHT);
    altitude.setColor(Color.GRAY);
    altitude.setLineWidth(1.5f);
    altitude.setFillAlpha(90);
    altitude.setFillColor(Color.GRAY);
    altitude.setDrawCircles(false);
    altitude.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    altitude.setDrawFilled(true);
    altitude.setDrawValues(false);

    LineDataSet estimatedSoc = new LineDataSet(estimatedSocValues, "Estimated");
    estimatedSoc.setAxisDependency(YAxis.AxisDependency.LEFT);
    estimatedSoc.setColor(Color.BLACK);
    estimatedSoc.setLineWidth(1.5f);
    estimatedSoc.setDrawCircles(false);
    estimatedSoc.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    estimatedSoc.setDrawValues(false);

    LineDataSet realSoc = new LineDataSet(mRealSocValues, "Real");
    realSoc.setAxisDependency(YAxis.AxisDependency.LEFT);
    realSoc.setColor(Color.GREEN);
    realSoc.setLineWidth(1.5f);
    realSoc.setDrawCircles(false);
    realSoc.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    realSoc.setDrawValues(false);

    LineData data = new LineData(altitude, estimatedSoc, realSoc);
    mLcConsumption.setData(data);
    mLcConsumption.invalidate();
  }

  @Override
  public void updateChartData(ArrayList<Entry> realSocValues) {
    mRealSocValues = realSocValues;
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mLcConsumption.invalidate();
      }
    });
  }


}
