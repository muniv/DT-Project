package com.muni.examples.aibrilcall;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.muni.examples.aibrilcall.custom.MyMarkerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_page_one.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_page_one#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_page_one extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private BarChart mChart1, mChart2;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private TextView tvText;


    public fragment_page_one() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_page_one.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_page_one newInstance(String param1, String param2) {
        fragment_page_one fragment = new fragment_page_one();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = inflater.inflate(R.layout.activity_barchart_noseekbar, container, false);

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");


        tvText = (TextView) view.findViewById(R.id.tvText);

        mChart1 = (BarChart) view.findViewById(R.id.chart1);
        mChart1.setOnChartValueSelectedListener(this);
        mChart1.getDescription().setEnabled(false);

        mChart2 = (BarChart) view.findViewById(R.id.chart2);
        mChart2.setOnChartValueSelectedListener(this);
        mChart2.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn


        // scaling can now only be done on x- and y-axis separately
        mChart1.setPinchZoom(false);
        mChart1.setDrawBarShadow(false);
        mChart1.setDrawGridBackground(false);

        mChart2.setPinchZoom(false);
        mChart2.setDrawBarShadow(false);
        mChart2.setDrawGridBackground(false);



        final String[] views = {"긍정", "부정"};

        XAxis xAxis = mChart1.getXAxis();
        xAxis.setLabelCount(2, false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10);
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        //xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(views));
        xAxis.setDrawAxisLine(false);


        mChart1.getAxisLeft().setDrawGridLines(false);

        MyMarkerView mv = new MyMarkerView(this.getContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart1); // For bounds control
        mChart1.setMarker(mv); // Set the marker to the chart

        XAxis xAxis1 = mChart2.getXAxis();
        xAxis1.setLabelCount(2, false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawGridLines(false);
        xAxis1.setTextSize(10);
        xAxis1.setTypeface(mTfLight);
        xAxis1.setGranularity(1f);
        //xAxis1.setCenterAxisLabels(true);
        xAxis1.setValueFormatter(new IndexAxisValueFormatter(views));
        xAxis1.setDrawAxisLine(false);

        mChart2.getAxisLeft().setDrawGridLines(false);

        MyMarkerView mv1 = new MyMarkerView(this.getContext(), R.layout.custom_marker_view);
        mv1.setChartView(mChart2); // For bounds control
        mChart2.setMarker(mv1); // Set the marker to the chart



        YAxis leftAxis = mChart1.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);


        YAxis rightAxis = mChart1.getAxisRight();
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMaximum(100f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);


        YAxis leftAxis1 = mChart2.getAxisLeft();
        leftAxis1.setTypeface(mTfLight);
        leftAxis1.setLabelCount(5, false);
        leftAxis1.setSpaceTop(15f);
        leftAxis1.setAxisMaximum(100f);
        leftAxis1.setAxisMinimum(0f);
        leftAxis1.setDrawAxisLine(false);
        leftAxis1.setDrawLabels(false);
        leftAxis1.setDrawGridLines(false);

        YAxis rightAxis1 = mChart2.getAxisRight();
        rightAxis1.setTypeface(mTfLight);
        rightAxis1.setLabelCount(5, false);
        rightAxis1.setSpaceTop(15f);
        rightAxis1.setAxisMaximum(100f);
        rightAxis1.setAxisMinimum(0f);
        rightAxis1.setDrawAxisLine(false);
        rightAxis1.setDrawLabels(false);
        rightAxis1.setDrawGridLines(false);




/*        Legend l = mChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(mTfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        final String[] views = {"나", "상대방"};

        XAxis xAxis = mChart1.getXAxis();
        xAxis.setTextSize(10);
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(views));

        YAxis leftAxis = mChart1.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)*/

/*
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);*/




        //mChart1.getAxisRight().setEnabled(false);

        // add a nice and smooth animation
        mChart1.animateY(2500);

        mChart1.getLegend().setEnabled(false);

        mChart2.animateY(2500);

        mChart2.getLegend().setEnabled(false);

        setData(2);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
    /*        case R.id.actionToggleValues: {

                for (IDataSet set : mChart1.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart1.invalidate();

                for (IDataSet set : mChart2.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart2.invalidate();
                break;
            }*/
          /*  case R.id.actionToggleHighlight: {

                if(mChart1.getData() != null) {
                    mChart1.getData().setHighlightEnabled(!mChart1.getData().isHighlightEnabled());
                    mChart1.invalidate();
                }

                if(mChart2.getData() != null) {
                    mChart2.getData().setHighlightEnabled(!mChart2.getData().isHighlightEnabled());
                    mChart2.invalidate();
                }
                break;
            }*/
          /*  case R.id.actionTogglePinch: {
                if (mChart1.isPinchZoomEnabled())
                    mChart1.setPinchZoom(false);
                else
                    mChart1.setPinchZoom(true);

                mChart1.invalidate();

                if (mChart2.isPinchZoomEnabled())
                    mChart2.setPinchZoom(false);
                else
                    mChart2.setPinchZoom(true);

                mChart2.invalidate();
                break;
            }*/
           /* case R.id.actionToggleAutoScaleMinMax: {
                mChart1.setAutoScaleMinMaxEnabled(!mChart1.isAutoScaleMinMaxEnabled());
                mChart1.notifyDataSetChanged();

                mChart2.setAutoScaleMinMaxEnabled(!mChart2.isAutoScaleMinMaxEnabled());
                mChart2.notifyDataSetChanged();
                break;
            }*/
        /*    case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : mChart1.getData().getDataSets())
                    ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                mChart1.invalidate();

                for (IBarDataSet set : mChart2.getData().getDataSets())
                    ((BarDataSet)set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                mChart2.invalidate();
                break;
            }*/
            case R.id.animateX: {
                mChart1.animateX(3000);

                mChart2.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart1.animateY(3000);

                mChart2.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                mChart1.animateXY(3000, 3000);

                mChart2.animateXY(3000, 3000);
                break;
            }
            case R.id.actionSave: {
                if (mChart1.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();

                if (mChart2.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }
        }
        return true;
    }

    private void setData(int cnt) {


        int[] values = {40, 60, 80, 20};
        double[] values1 = {0.42135, 0.58423};
        double[] values2 = {0.78423, 0.22135};

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

        for (int i = 0; i < cnt; i++) {
            //float mult = (mSeekBarY.getProgress() + 1);
            float val1 = (float) Math.round((values1[i] * 100));
            float val2 = (float) Math.round((values2[i] * 100));
            yVals1.add(new BarEntry(i, val1));
            yVals2.add(new BarEntry(i, val2));
        }

        BarDataSet set1, set2;

        if (mChart1.getData() != null &&
                mChart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart1.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart1.getData().notifyDataChanged();
            mChart1.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            //set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets1 = new ArrayList<IBarDataSet>();
            dataSets1.add(set1);

            BarData data1 = new BarData(dataSets1);
            mChart1.setData(data1);
            mChart1.setFitBars(true);
        }

        mChart1.invalidate();

        if (mChart2.getData() != null &&
                mChart2.getData().getDataSetCount() > 0) {
            set2 = (BarDataSet)mChart2.getData().getDataSetByIndex(0);
            set2.setValues(yVals2);
            mChart2.getData().notifyDataChanged();
            mChart2.notifyDataSetChanged();
        } else {
            set2 = new BarDataSet(yVals2, "Data Set");
            set2.setColors(ColorTemplate.VORDIPLOM_COLORS);
            //set2.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets2 = new ArrayList<IBarDataSet>();
            dataSets2.add(set2);

            BarData data2 = new BarData(dataSets2);
            mChart2.setData(data2);
            mChart2.setFitBars(true);
        }

        mChart2.invalidate();





/*        float groupSpace = 0.1f;
        float barSpace = 0.05f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"


        int[] values = {40, 60, 80, 20};
        double[] values1 = {0.42135, 0.58423, 0.78423, 0.22135};

        if(values1[0] > values1[2]){
            tvText.setText("권영호님이 박주혜님보다 긍정요소가 많았습니다.");
        }else if(values1[0] < values1[2]) {
            tvText.setText("박주혜님이 권영호님보다 긍정요소가 많았습니다.");
        }else if(values1[0] == values1[2]) {
            tvText.setText("박주혜님과 권영호님, 모두 긍정적입니다.");
        }

        int groupCount = 2;
        int startYear = 0;
        int endYear = startYear + groupCount;


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();

*//*        float randomMultiplier = mSeekBarY.getProgress() * 100000f;*//*

        for (int i = 0; i < cnt; i++) {
            yVals1.add(new BarEntry(i, (float)Math.round(values1[i] * 100)));
            yVals2.add(new BarEntry(i, (float)Math.round(values1[i + 2] * 100)));
            yVals3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
        }

        BarDataSet set1, set2, set3, set4;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet) mChart.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "긍정");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "부정");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(yVals3, "Company C");
            set3.setColor(Color.rgb(242, 247, 158));
            set4 = new BarDataSet(yVals4, "Company D");
            set4.setColor(Color.rgb(255, 102, 0));

            BarData data = new BarData(set1, set2);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(mTfLight);

            mChart.setData(data);
        }

        // specify the width each bar should have
        mChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mChart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.getXAxis().setAxisMaximum(startYear + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mChart.groupBars(startYear, groupSpace, barSpace);
        mChart.invalidate();*/
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
