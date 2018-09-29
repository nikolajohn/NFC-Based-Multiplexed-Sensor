package com.example.administrator.wrongtry.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.wrongtry.R;
import com.example.administrator.wrongtry.activities.data.MyData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

import io.realm.RealmResults;

import static com.example.administrator.wrongtry.activities.fragments.NdefFragment.CurrentGlu;
import static com.example.administrator.wrongtry.activities.fragments.NdefFragment.CurrentH;
import static com.example.administrator.wrongtry.activities.fragments.NdefFragment.CurrentK;
import static com.example.administrator.wrongtry.activities.fragments.NdefFragment.CurrentNa;

public class RealTimeFragment extends Fragment {

    public LineChart mChart0;
    private LineData data0;
    private LineDataSet set0;

    public LineChart mChart1;
    private LineData data1;
    private LineDataSet set1;

    public LineChart mChart2;
    private LineData data2;
    private LineDataSet set2;

    public LineChart mChart3;
    private LineData data3;
    private LineDataSet set3;

    public RealTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater
                .inflate(R.layout.fragment_real_time, container, false);
        mChart0 = (LineChart) layout.findViewById(R.id.chart0);
        mChart1 = (LineChart) layout.findViewById(R.id.chart1);
        mChart2 = (LineChart) layout.findViewById(R.id.chart2);
        mChart3 = (LineChart) layout.findViewById(R.id.chart3);

        // 初始图
        initChartView0();
        ArrayList<Entry> yVals0 = new ArrayList<Entry>();
        yVals0.add(new Entry(0,0));
        if (yVals0.size()>0){
            addDataSet0(yVals0, "Glu");// 画图
            Legend l0 = mChart0.getLegend();
            l0.setForm(Legend.LegendForm.LINE);
            l0.setXEntrySpace(40);
        }

        initChartView1();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        yVals1.add(new Entry(0,0));
        if (yVals1.size()>0){
            addDataSet1(yVals1, "pH");// 画图
            Legend l1 = mChart1.getLegend();
            l1.setForm(Legend.LegendForm.LINE);
            l1.setXEntrySpace(40);
        }

        initChartView2();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        yVals2.add(new Entry(0,0));
        if (yVals2.size()>0){
            addDataSet2(yVals2, "Na");// 画图
            Legend l2 = mChart2.getLegend();
            l2.setForm(Legend.LegendForm.LINE);
            l2.setXEntrySpace(40);
        }

        initChartView3();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        yVals3.add(new Entry(0,0));
        if (yVals3.size()>0){
            addDataSet3(yVals3, "K");// 画图
            Legend l3 = mChart3.getLegend();
            l3.setForm(Legend.LegendForm.LINE);
            l3.setXEntrySpace(40);
        }

        new TimeThread().start();
        // Inflate the layout for this fragment
        return layout;
    }

    private class TimeThread extends Thread {
        @Override
        public void run() {
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔0.5秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Plot();
                    break;

                default:
                    break;

            }
        }
    };

    private void Plot() {
        addEntry0((int) CurrentGlu);
        addEntry1((int) CurrentH);
        addEntry2((int) CurrentNa);
        addEntry3((int) CurrentK);
    }

    public void initChartView0( ) {

        mChart0.setDrawGridBackground(false);
        mChart0.setDescription(null);    //右下角说明文字
        mChart0.setDrawBorders(true);    //四周是不是有边框
        mChart0.setBorderWidth(0.5f);
        mChart0.setBorderColor(Color.parseColor("#b3b3b3"));    //边框颜色，默认黑色？
//        mChart.setVisibleXRangeMaximum(4);

        // enable touch gestures
        mChart0.setTouchEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        //禁止x轴y轴同时进行缩放
        mChart0.setPinchZoom(false);
        // enable scaling and dragging
        mChart0.setDragEnabled(true);
        mChart0.setScaleEnabled(true);
        mChart0.setScaleXEnabled(true);


        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart0.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        //xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分
        xAxis.setEnabled(false);

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                //return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
                return null;
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(false);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(100);    //强制有多少个刻度
        xAxis.setTextColor(Color.parseColor("#b3b3b3"));


        //隐藏左侧坐标轴显示右侧坐标轴，并对右侧的轴进行配置
        mChart0.getAxisLeft().setEnabled(false);
        YAxis leftAxis = mChart0.getAxisRight();
        leftAxis.setEnabled(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawAxisLine(false);
        //坐标轴绘制在图表的内侧
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#b3b3b3"));
        //确实没看懂这个是干嘛用的，默认是10f
        //这个玩意好像有坐标轴enable的时候是不可用的
        leftAxis.setSpaceTop(50f);// 这里很重要

        mChart0.animateY(1000, Easing.EasingOption.Linear);
        mChart0.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart0.setData(new LineData());
    }

    public void initChartView1( ) {

        mChart1.setDrawGridBackground(false);
        mChart1.setDescription(null);    //右下角说明文字
        mChart1.setDrawBorders(true);    //四周是不是有边框
        mChart1.setBorderWidth(0.5f);
        mChart1.setBorderColor(Color.parseColor("#b3b3b3"));    //边框颜色，默认黑色？
//        mChart.setVisibleXRangeMaximum(4);

        // enable touch gestures
        mChart1.setTouchEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        //禁止x轴y轴同时进行缩放
        mChart1.setPinchZoom(false);
        // enable scaling and dragging
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setScaleXEnabled(true);


        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        //xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分
        xAxis.setEnabled(false);

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                //return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
                return null;
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(false);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(100);    //强制有多少个刻度
        xAxis.setTextColor(Color.parseColor("#b3b3b3"));


        //隐藏左侧坐标轴显示右侧坐标轴，并对右侧的轴进行配置
        mChart1.getAxisLeft().setEnabled(false);
        YAxis leftAxis = mChart1.getAxisRight();
        leftAxis.setEnabled(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawAxisLine(false);
        //坐标轴绘制在图表的内侧
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#b3b3b3"));
        //确实没看懂这个是干嘛用的，默认是10f
        //这个玩意好像有坐标轴enable的时候是不可用的
        leftAxis.setSpaceTop(50f);// 这里很重要

        mChart1.animateY(1000, Easing.EasingOption.Linear);
        mChart1.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart1.setData(new LineData());
    }

    public void initChartView2( ) {

        mChart2.setDrawGridBackground(false);
        mChart2.setDescription(null);    //右下角说明文字
        mChart2.setDrawBorders(true);    //四周是不是有边框
        mChart2.setBorderWidth(0.5f);
        mChart2.setBorderColor(Color.parseColor("#b3b3b3"));    //边框颜色，默认黑色？
//        mChart.setVisibleXRangeMaximum(4);

        // enable touch gestures
        mChart2.setTouchEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        //禁止x轴y轴同时进行缩放
        mChart2.setPinchZoom(false);
        // enable scaling and dragging
        mChart2.setDragEnabled(true);
        mChart2.setScaleEnabled(true);
        mChart2.setScaleXEnabled(true);


        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        //xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分
        xAxis.setEnabled(false);

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                //return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
                return null;
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(false);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(100);    //强制有多少个刻度
        xAxis.setTextColor(Color.parseColor("#b3b3b3"));


        //隐藏左侧坐标轴显示右侧坐标轴，并对右侧的轴进行配置
        mChart2.getAxisLeft().setEnabled(false);
        YAxis leftAxis = mChart2.getAxisRight();
        leftAxis.setEnabled(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawAxisLine(false);
        //坐标轴绘制在图表的内侧
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#b3b3b3"));
        //确实没看懂这个是干嘛用的，默认是10f
        //这个玩意好像有坐标轴enable的时候是不可用的
        leftAxis.setSpaceTop(50f);// 这里很重要

        mChart2.animateY(1000, Easing.EasingOption.Linear);
        mChart2.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart2.setData(new LineData());
    }

    public void initChartView3( ) {

        mChart3.setDrawGridBackground(false);
        mChart3.setDescription(null);    //右下角说明文字
        mChart3.setDrawBorders(true);    //四周是不是有边框
        mChart3.setBorderWidth(0.5f);
        mChart3.setBorderColor(Color.parseColor("#b3b3b3"));    //边框颜色，默认黑色？
//        mChart.setVisibleXRangeMaximum(4);

        // enable touch gestures
        mChart3.setTouchEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        //禁止x轴y轴同时进行缩放
        mChart3.setPinchZoom(false);
        // enable scaling and dragging
        mChart3.setDragEnabled(true);
        mChart3.setScaleEnabled(true);
        mChart3.setScaleXEnabled(true);


        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        //xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分
        xAxis.setEnabled(false);

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                //return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
                return null;
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(false);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(100);    //强制有多少个刻度
        xAxis.setTextColor(Color.parseColor("#b3b3b3"));


        //隐藏左侧坐标轴显示右侧坐标轴，并对右侧的轴进行配置
        mChart3.getAxisLeft().setEnabled(false);
        YAxis leftAxis = mChart3.getAxisRight();
        leftAxis.setEnabled(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawAxisLine(false);
        //坐标轴绘制在图表的内侧
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.parseColor("#b3b3b3"));
        //确实没看懂这个是干嘛用的，默认是10f
        //这个玩意好像有坐标轴enable的时候是不可用的
        leftAxis.setSpaceTop(50f);// 这里很重要

        mChart3.animateY(1000, Easing.EasingOption.Linear);
        mChart3.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart3.setData(new LineData());
    }

    private void addDataSet0(ArrayList<Entry> entryList, String dataSetName) {

        data0 = (LineData) mChart0.getData();

        if (data0 != null) {
            int count = data0.getDataSetCount();
            set0 = new LineDataSet(entryList, dataSetName);
            set0.setDrawCircleHole(false);
            set0.setDrawCircles(false);
            set0.setLineWidth(1.5f);
            //set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#FF3030");
            set0.setColor(color);
            set0.setCircleColor(color);
            set0.setHighLightColor(color);
            set0.setValueTextSize(10f);
            set0.setDrawValues(false);    //节点不显示具体数值
            set0.setValueTextColor(color);
            set0.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set0.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set0.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set0.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set0.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线

//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set0.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data0.addDataSet(set0);
            data0.notifyDataChanged();
            mChart0.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart0.setVisibleXRangeMaximum(100);// 修改了此处
            mChart0.invalidate();
        }
    }

    private void addDataSet1(ArrayList<Entry> entryList, String dataSetName) {

        data1 = (LineData) mChart1.getData();

        if (data1 != null) {
            int count = data1.getDataSetCount();
            set1 = new LineDataSet(entryList, dataSetName);
            set1.setDrawCircleHole(false);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.5f);
            //set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#5abdfc");
            set1.setColor(color);
            set1.setCircleColor(color);
            set1.setHighLightColor(color);
            set1.setValueTextSize(10f);
            set1.setDrawValues(false);    //节点不显示具体数值
            set1.setValueTextColor(color);
            set1.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set1.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set1.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线

//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set1.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data1.addDataSet(set1);
            data1.notifyDataChanged();
            mChart1.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart1.setVisibleXRangeMaximum(100);// 修改了此处
            mChart1.invalidate();
        }
    }

    private void addDataSet2(ArrayList<Entry> entryList, String dataSetName) {

        data2 = (LineData) mChart2.getData();

        if (data2 != null) {
            int count = data2.getDataSetCount();
            set2 = new LineDataSet(entryList, dataSetName);
            set2.setDrawCircleHole(false);
            set2.setDrawCircles(false);
            set2.setLineWidth(1.5f);
            //set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#33cc33");
            set2.setColor(color);
            set2.setCircleColor(color);
            set2.setHighLightColor(color);
            set2.setValueTextSize(10f);
            set2.setDrawValues(false);    //节点不显示具体数值
            set2.setValueTextColor(color);
            set2.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set2.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set2.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线

//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set2.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data2.addDataSet(set2);
            data2.notifyDataChanged();
            mChart2.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart2.setVisibleXRangeMaximum(100);// 修改了此处
            mChart2.invalidate();
        }
    }

    private void addDataSet3(ArrayList<Entry> entryList, String dataSetName) {

        data3 = (LineData) mChart3.getData();

        if (data3 != null) {
            int count = data3.getDataSetCount();
            set3 = new LineDataSet(entryList, dataSetName);
            set3.setDrawCircleHole(false);
            set3.setDrawCircles(false);
            set3.setLineWidth(1.5f);
            //set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#FFD700");
            set3.setColor(color);
            set3.setCircleColor(color);
            set3.setHighLightColor(color);
            set3.setValueTextSize(10f);
            set3.setDrawValues(false);    //节点不显示具体数值
            set3.setValueTextColor(color);
            set3.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set3.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set3.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set3.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set3.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线

//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set3.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data3.addDataSet(set3);
            data3.notifyDataChanged();
            mChart3.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart3.setVisibleXRangeMaximum(100);// 修改了此处
            mChart3.invalidate();
        }
    }

    public void addEntry0(int number){
        data0 = (LineData) mChart0.getData();

        if(data0 != null) {

            //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
            if (set0.getEntryCount() == 0) {
                data0.addDataSet(set0);
            }
            mChart0.setData(data0);

            Entry entry = new Entry(set0.getEntryCount(), number);
            data0.addEntry(entry, 0);
            //通知数据已经改变
            data0.notifyDataChanged();
            mChart0.notifyDataSetChanged();
            //设置在曲线图中显示的最大数量
            mChart0.setVisibleXRangeMaximum(10);
            //移到某个位置
            mChart0.moveViewToX(data0.getEntryCount() - 5);
        }
    }

    public void addEntry1(int number){
        data1 = (LineData) mChart1.getData();

        if(data1 != null) {

            //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
            if (set1.getEntryCount() == 0) {
                data1.addDataSet(set1);
            }
            mChart1.setData(data1);

            Entry entry = new Entry(set1.getEntryCount(), number);
            data1.addEntry(entry, 0);
            //通知数据已经改变
            data1.notifyDataChanged();
            mChart1.notifyDataSetChanged();
            //设置在曲线图中显示的最大数量
            mChart1.setVisibleXRangeMaximum(10);
            //移到某个位置
            mChart1.moveViewToX(data1.getEntryCount() - 5);
        }
    }

    public void addEntry2(int number){
        data2 = (LineData) mChart2.getData();

        if(data2 != null) {

            //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
            if (set2.getEntryCount() == 0) {
                data2.addDataSet(set2);
            }
            mChart2.setData(data2);

            Entry entry = new Entry(set2.getEntryCount(), number);
            data2.addEntry(entry, 0);
            //通知数据已经改变
            data2.notifyDataChanged();
            mChart2.notifyDataSetChanged();
            //设置在曲线图中显示的最大数量
            mChart2.setVisibleXRangeMaximum(10);
            //移到某个位置
            mChart2.moveViewToX(data2.getEntryCount() - 5);
        }
    }

    public void addEntry3(int number){
        data3 = (LineData) mChart3.getData();

        if(data3 != null) {

            //最开始的时候才添加 lineDataSet（一个lineDataSet 代表一条线）
            if (set3.getEntryCount() == 0) {
                data3.addDataSet(set3);
            }
            mChart3.setData(data3);

            Entry entry = new Entry(set3.getEntryCount(), number);
            data3.addEntry(entry, 0);
            //通知数据已经改变
            data3.notifyDataChanged();
            mChart3.notifyDataSetChanged();
            //设置在曲线图中显示的最大数量
            mChart3.setVisibleXRangeMaximum(10);
            //移到某个位置
            mChart3.moveViewToX(data3.getEntryCount() - 5);
        }
    }


}
