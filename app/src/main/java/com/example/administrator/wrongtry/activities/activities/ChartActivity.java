package com.example.administrator.wrongtry.activities.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
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

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.administrator.wrongtry.activities.activities.MainActivity.realm;
import static com.example.administrator.wrongtry.activities.utils.DateUtil.getDateToString;

public class ChartActivity extends AppCompatActivity {
    private LineChart mChart0;
    private LineChart mChart1;
    private LineChart mChart2;
    private LineChart mChart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        long record = intent.getLongExtra("time",0);// 只有这个record是不同的
        Log.d("record", String.valueOf(record));

        mChart0 = (LineChart) findViewById(R.id.chart0);// 自定义图表
        mChart1 = (LineChart) findViewById(R.id.chart1);
        mChart2 = (LineChart) findViewById(R.id.chart2);
        mChart3 = (LineChart) findViewById(R.id.chart3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(getDateToString(record,"yyyy/MM/dd"));// 设置标题
        Glide.with(this).load(R.drawable.liangzi).into(fruitImageView);// 使用Glide加载图片

        RealmResults<MyData> MyResult = realm.where(MyData.class).findAll();

        initChartView0(MyResult);
        initChartView1(MyResult);
        initChartView2(MyResult);
        initChartView3(MyResult);

        ArrayList<Entry> yVals0 = new ArrayList<Entry>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();

        if(MyResult.size() <= 0){
            return ;
        }
        int k = 0;
        for (int i = 0; i < MyResult.size(); i++){
            //yVals.add(new Entry(i, (float) (Math.random() * 10000f)));
            if(MyResult.get(i).getRecord() == record){
                k++;

                yVals0.add(new Entry((k-1), MyResult.get(i).getGlu()));
                yVals1.add(new Entry((k-1), MyResult.get(i).getNa()));
                yVals2.add(new Entry((k-1), MyResult.get(i).getK()));
                yVals3.add(new Entry((k-1), MyResult.get(i).getpH()));
            }
        }

        addDataSet0(yVals0, "Glu");// 画图
        Legend l0 = mChart0.getLegend();
        l0.setForm(Legend.LegendForm.LINE);
        l0.setXEntrySpace(40);

        addDataSet1(yVals1, "Na");// 画图
        Legend l1 = mChart1.getLegend();
        l1.setForm(Legend.LegendForm.LINE);
        l1.setXEntrySpace(40);

        addDataSet2(yVals2, "Ka");// 画图
        Legend l2 = mChart2.getLegend();
        l2.setForm(Legend.LegendForm.LINE);
        l2.setXEntrySpace(40);

        addDataSet3(yVals3, "pH");// 画图
        Legend l3 = mChart3.getLegend();
        l3.setForm(Legend.LegendForm.LINE);
        l3.setXEntrySpace(40);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// 处理HomeAsUp按钮的事件
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();// 调用finish方法关闭当前的活动
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void initChartView0(final RealmResults<MyData> MyResult) {

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
        mChart0.setScaleXEnabled(true);// ADD in 20180403

        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart0.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(true);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(MyResult.size());    //强制有多少个刻度
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

        mChart0.animateY(1000, Easing.EasingOption.Linear);// ADD in 20180403
        mChart0.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart0.setData(new LineData());
    }

    private void addDataSet0(ArrayList<Entry> entryList, String dataSetName) {

        LineData data = mChart0.getData();

        if (data != null) {
            int count = data.getDataSetCount();
            LineDataSet set = new LineDataSet(entryList, dataSetName);
            set.setLineWidth(1.5f);
            //set.setCircleRadius(3.5f); // 注释于20180403
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#FF3030");
            set.setColor(color);
            set.setDrawCircleHole(false);
            set.setDrawCircles(false);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setDrawValues(false);    //节点不显示具体数值
            set.setValueTextColor(color);
            set.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线

//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart0.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart0.setVisibleXRangeMaximum(40);
            mChart0.invalidate();
        }
    }

    public void initChartView1(final RealmResults<MyData> MyResult) {

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
        mChart1.setScaleXEnabled(true);// ADD in 20180403

        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(true);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(MyResult.size());    //强制有多少个刻度
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

        mChart1.animateY(1000, Easing.EasingOption.Linear);// ADD in 20180403
        mChart1.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart1.setData(new LineData());
    }

    private void addDataSet1(ArrayList<Entry> entryList, String dataSetName) {

        LineData data = mChart1.getData();

        if (data != null) {
            int count = data.getDataSetCount();
            LineDataSet set = new LineDataSet(entryList, dataSetName);
            set.setLineWidth(1.5f);
            set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#5abdfc");
            set.setColor(color);
            set.setDrawCircleHole(false);
            set.setDrawCircles(false);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setDrawValues(false);    //节点不显示具体数值
            set.setValueTextColor(color);
            set.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线


//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart1.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart1.setVisibleXRangeMaximum(40);
            mChart1.invalidate();
        }

    }

    public void initChartView2(final RealmResults<MyData> MyResult) {

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
        mChart2.setScaleXEnabled(true);// ADD in 20180403

        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(true);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(MyResult.size());    //强制有多少个刻度
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

        mChart2.animateY(1000, Easing.EasingOption.Linear);// ADD in 20180403
        mChart2.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart2.setData(new LineData());
    }

    private void addDataSet2(ArrayList<Entry> entryList, String dataSetName) {

        LineData data = mChart2.getData();

        if (data != null) {
            int count = data.getDataSetCount();
            LineDataSet set = new LineDataSet(entryList, dataSetName);
            set.setLineWidth(1.5f);
            set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#33cc33");
            set.setColor(color);
            set.setDrawCircleHole(false);
            set.setDrawCircles(false);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setDrawValues(false);    //节点不显示具体数值
            set.setValueTextColor(color);
            set.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线


//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart2.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart2.setVisibleXRangeMaximum(40);
            mChart2.invalidate();
        }

    }

    public void initChartView3(final RealmResults<MyData> MyResult) {

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
        mChart3.setScaleXEnabled(true);// ADD in 20180403

        //控制轴上的坐标绘制在什么地方 上边下边左边右边
        XAxis xAxis = mChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);    //x轴是在上边显示还是显示在下边
        xAxis.enableGridDashedLine(10f, 10f, 0f);    //背景用虚线表格来绘制  给整成虚线
        xAxis.setAxisMinimum(0f);//设置轴的最小值。这样设置将不会根据提供的数据自动计算。
        xAxis.setGranularityEnabled(true);    //粒度
        xAxis.setGranularity(1f);    //缩放的时候有用，比如放大的时候，我不想把横轴的月份再细分

        // 此函数用于改变X轴的标签名称
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mMonths[(int) value % mMonths.length];
                //return String.valueOf(value);
                return getDateToString((long) MyResult.get((int) value).getTime(),"HH:mm:ss");
            }

            //@Override
            public int getDecimalDigits() {
                return 0;
            }
        });
//        xAxis.setAxisLineWidth(0f);    //设置坐标轴那条线的宽度
        xAxis.setDrawAxisLine(false);    //是否显示坐标轴那条轴
        xAxis.setDrawLabels(true);    //是不是显示轴上的刻度
        //xAxis.setLabelCount(mMonths.length);    //强制有多少个刻度
        xAxis.setLabelCount(MyResult.size());    //强制有多少个刻度
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

        mChart3.animateY(1000, Easing.EasingOption.Linear);// ADD in 20180403
        mChart3.animateX(1000, Easing.EasingOption.Linear);

        //一个chart中包含一个Data对象，一个Data对象包含多个DataSet对象，
        // 每个DataSet是对应一条线上的所有点(相对于折线图来说)
        mChart3.setData(new LineData());
    }

    private void addDataSet3(ArrayList<Entry> entryList, String dataSetName) {

        LineData data = mChart3.getData();

        if (data != null) {
            int count = data.getDataSetCount();
            LineDataSet set = new LineDataSet(entryList, dataSetName);
            set.setLineWidth(1.5f);
            set.setCircleRadius(3.5f);
            //int color = mColors[count % mColors.length];
            int color = Color.parseColor("#FFD700");
            set.setColor(color);
            set.setDrawCircleHole(false);
            set.setDrawCircles(false);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setDrawValues(false);    //节点不显示具体数值
            set.setValueTextColor(color);
            set.enableDashedHighlightLine(10f, 5f, 0f);    //选中某个点的时候高亮显示只是线
            set.setDrawFilled(true);     //填充折线图折线和坐标轴之间
            set.setFillColor(color);    //填充可以设置渐变填充一个Drawable，或者仅仅填充颜色
            set.setAxisDependency(YAxis.AxisDependency.RIGHT);    //如果使用的是右坐标轴必须设置这行
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 圆滑曲线


//            set.setDrawVerticalHighlightIndicator(false);//取消纵向辅助线
            set.setDrawHorizontalHighlightIndicator(false);//取消横向辅助线

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart3.notifyDataSetChanged();
            //这行代码必须放到这里，这里设置的是图表这个视窗能显示，x坐标轴，从最大值到最小值之间
            //多少段，好像这个库没有办法设置x轴中的间隔的大小
            mChart3.setVisibleXRangeMaximum(40);
            mChart3.invalidate();
        }

    }


}
