package com.example.administrator.wrongtry.activities.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.wrongtry.R;
import com.example.administrator.wrongtry.activities.adapters.RecyclerViewAdapter;
import com.example.administrator.wrongtry.activities.data.Item;
import com.example.administrator.wrongtry.activities.data.MyData;
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
import java.util.List;

import io.realm.RealmResults;

import static com.example.administrator.wrongtry.activities.activities.MainActivity.CurrentTime;
import static com.example.administrator.wrongtry.activities.activities.MainActivity.PerformClickFlag;
import static com.example.administrator.wrongtry.activities.activities.MainActivity.realm;
import static com.example.administrator.wrongtry.activities.utils.DateUtil.getDateToString;

/**
 * A simple {@link Fragment} subclass.
 */
//public class VersionFragment extends Fragment implements View.OnClickListener{
public class VersionFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Item> itemlist;
    private RecyclerViewAdapter adapter;
    //private LineChart mChart;
    //private static Button updateButton;
    public VersionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_version, container, false);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerView);
        initDate();
        adapter = new RecyclerViewAdapter(itemlist,getActivity());
        assert recyclerView != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //PerformClickFlag = false;
        PerformClickFlag = true;

        return layout;
    }

    private void initDate(){
        itemlist = new ArrayList<>();
        RealmResults<MyData> MyResult = realm.where(MyData.class).findAll();
        Log.d("VersionFragment","ReadDataBase");
        for (int i = 0; i < MyResult.size(); i++){
            if(itemlist.size() == 0){
                itemlist.add(new Item(getDateToString((long) MyResult.get(i).getTime(),"yyyy/MM/dd"),
                        getDateToString((long) MyResult.get(i).getTime(),"hh:mm:ss"), (long) MyResult.get(i).getTime()));
            }
            else if(itemlist.size() > 0){

                if(MyResult.get(i).getTime() != MyResult.get(i-1).getTime()){
                    itemlist.add(new Item(getDateToString((long) MyResult.get(i).getTime(),"yyyy/MM/dd"),
                            getDateToString((long) MyResult.get(i).getTime(),"hh:mm:ss"), (long) MyResult.get(i).getTime()));
                }
            }
        }
    }
}
