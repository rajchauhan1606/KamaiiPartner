package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;
import com.kamaii.partner.DTO.EarningDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MyEarning extends Fragment {

    private View view;
    private BarChart mChart;
    private String TAG = MyEarning.class.getSimpleName();
    private EarningDTO earningDTO;
    private ArrayList<EarningDTO.ChartData> chartDataList;
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> paramsRequest = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private BaseActivity baseActivity;
    List<String> list = new ArrayList<String>();
    private CardView cvRequestPayment;
    String[] stringArray;
    private CustomTextViewBold tvOnlineTime, tvWalletEarning, tvOnlineEarning, tvCashEarning, tvWalletAmount, tvTotalEarning,todays_earning,bank_amount, tvJobDone, tvTotalJob, tvCompletePercentages, tvreferearning, tvtotalrefer;
    private DialogInterface dialog_book;
    Spinner spi_date;
    private ArrayList<String> spinnerarryalist = new ArrayList<>();
    String spinnervalue = "";
    CustomTextView total_order_txt, complete_job_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_earning, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_earnings));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.editImage.setVisibility(View.GONE);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());
        total_order_txt = view.findViewById(R.id.total_order_txt);
        complete_job_txt = view.findViewById(R.id.complete_job_txt);


        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {

        mChart = v.findViewById(R.id.chart1);
        tvOnlineEarning = v.findViewById(R.id.tvOnlineEarning);
        tvWalletEarning = v.findViewById(R.id.tvWalletEarning);
        tvCashEarning = v.findViewById(R.id.tvCashEarning);
        tvWalletAmount = v.findViewById(R.id.tvWalletAmount);
        tvTotalEarning = v.findViewById(R.id.tvTotalEarning);
        tvJobDone = v.findViewById(R.id.tvJobDone);
        bank_amount = v.findViewById(R.id.bank_amount);
        todays_earning = v.findViewById(R.id.todays_earning);
        tvTotalJob = v.findViewById(R.id.tvTotalJob);
        tvOnlineTime = v.findViewById(R.id.tvOnlineTime);
        tvCompletePercentages = v.findViewById(R.id.tvCompletePercentages);
        cvRequestPayment = v.findViewById(R.id.cvRequestPayment);
        tvreferearning = v.findViewById(R.id.tvreferearning);
        tvtotalrefer = v.findViewById(R.id.tvtotalrefer);
        spi_date = v.findViewById(R.id.spi_date);

        spinnerarryalist.add(0, "Today");
        spinnerarryalist.add(1, "Yesterday");
        spinnerarryalist.add(2, "Weekly");
        spinnerarryalist.add(3, "Month");
        spinnerarryalist.add(4, "Custom");
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerarryalist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_date.setAdapter(arrayAdapter);

        cvRequestPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {

                    try {
                        if (earningDTO.getWalletAmount() > 200) {
                            bookDailog();
                        } else {
                            ProjectUtils.showLong(getActivity(), "Amount Should Be Grater Than 200");
                        }
                    } catch (NullPointerException e) {
                        ProjectUtils.showLong(getActivity(), getResources().getString(R.string.insufficient_balance));
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
            }
        });

        spi_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnervalue = adapterView.getItemAtPosition(i).toString();
                // Toast.makeText(getActivity(),spinnervalue,Toast.LENGTH_SHORT).show();


                if (spinnervalue.equalsIgnoreCase("Custom")) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    spinnervalue = (i2 + "/" + (i1 + 1) + "/" + i);
                                    //Toast.makeText(getActivity(),spinnervalue,Toast.LENGTH_SHORT).show();
                                }
                            }, year, month, day);
                    dpd.show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getEarning();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
    }

    public void getEarning() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.MY_EARNING1_API, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e("MYEARNING", "" + response.toString());
                        earningDTO = new Gson().fromJson(response.getJSONObject("data").toString(), EarningDTO.class);
                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });

    }

    public void requestPayment() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.WALLET_REQUEST_API, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showLong(getActivity(), msg);
                    cvRequestPayment.setEnabled(false);
                    dialog_book.dismiss();
                } else {
                    cvRequestPayment.setEnabled(false);
                    ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void showData() {

        tvOnlineEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getOnlineEarning());
        tvWalletEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getWalletEarning());
        tvCashEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getCashEarning());
        tvWalletAmount.setText(earningDTO.getCurrency_symbol() + earningDTO.getWalletAmount());
        tvTotalEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getTotalEarning());
        tvreferearning.setText(earningDTO.getCurrency_symbol() + earningDTO.getReferral_earning());
        tvJobDone.setText(earningDTO.getJobDone());
        bank_amount.setText(earningDTO.getBankTransfer());
        todays_earning.setText(earningDTO.getTodaysEarning());
        tvOnlineTime.setText(earningDTO.getOnlineHours());

        if (earningDTO.getTotalJob().equalsIgnoreCase("1") || earningDTO.getTotalJob().equalsIgnoreCase("0")) {
            total_order_txt.setText(getResources().getString(R.string.total_job));
        } else {
            tvTotalJob.setText(getResources().getString(R.string.total_jobs));
        }

        if (earningDTO.getJobDone().equalsIgnoreCase("0") || earningDTO.getJobDone().equalsIgnoreCase("1")) {
            complete_job_txt.setText(getResources().getString(R.string.job_done));
        } else {
            complete_job_txt.setText(getResources().getString(R.string.jobs_done));
        }
        tvTotalJob.setText(earningDTO.getTotalJob());
        tvtotalrefer.setText(earningDTO.getTot_ref());
        tvCompletePercentages.setText(earningDTO.getCompletePercentages() + " %");


        chartDataList = new ArrayList<>();
        chartDataList = earningDTO.getChartData();

        for (int i = 0; i < chartDataList.size(); i++) {

            list.add(chartDataList.get(i).getDay());
        }

        stringArray = list.toArray(new String[0]);


        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(60);

        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return stringArray[(int) value % stringArray.length];
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        setData(chartDataList);

    }


    private void setData(ArrayList<EarningDTO.ChartData> charts) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < charts.size(); i++) {

            yVals1.add(new BarEntry(i, Float.parseFloat(charts.get(i).getCount())));
        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, getResources().getString(R.string.earning_graph));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
            mChart.invalidate();
        }
    }

    public void bookDailog() {
        try {
            new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.payment))
                    .setMessage(getResources().getString(R.string.process_payment))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            requestPayment();

                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
