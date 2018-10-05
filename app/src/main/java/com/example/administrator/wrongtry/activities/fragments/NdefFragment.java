/*
 ****************************************************************************
 * Copyright(c) 2014 NXP Semiconductors                                     *
 * All rights are reserved.                                                 *
 *                                                                          *
 * Software that is described herein is for illustrative purposes only.     *
 * This software is supplied "AS IS" without any warranties of any kind,    *
 * and NXP Semiconductors disclaims any and all warranties, express or      *
 * implied, including all implied warranties of merchantability,            *
 * fitness for a particular purpose and non-infringement of intellectual    *
 * property rights.  NXP Semiconductors assumes no responsibility           *
 * or liability for the use of the software, conveys no license or          *
 * rights under any patent, copyright, mask work right, or any other        *
 * intellectual property rights in or to any products. NXP Semiconductors   *
 * reserves the right to make changes in the software without notification. *
 * NXP Semiconductors also makes no representation or warranty that such    *
 * application will be suitable for the specified use without further       *
 * testing or modification.                                                 *
 *                                                                          *
 * Permission to use, copy, modify, and distribute this software and its    *
 * documentation is hereby granted, under NXP Semiconductors' relevant      *
 * copyrights in the software, without fee, provided that it is used in     *
 * conjunction with NXP Semiconductor products(UCODE I2C, NTAG I2C).        *
 * This  copyright, permission, and disclaimer notice must appear in all    *
 * copies of this code.                                                     *
 ****************************************************************************
 */
package com.example.administrator.wrongtry.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.wrongtry.R;
import com.example.administrator.wrongtry.activities.activities.MainActivity;
import com.example.administrator.wrongtry.activities.activities.SramTestActivity;
import com.example.administrator.wrongtry.activities.data.MyData;

import java.util.ArrayList;

import static com.example.administrator.wrongtry.activities.activities.MainActivity.CurrentTime;
import static com.example.administrator.wrongtry.activities.activities.MainActivity.PerformClickFlag;
import static com.example.administrator.wrongtry.activities.activities.MainActivity.RecordCurrentTime;
import static com.example.administrator.wrongtry.activities.activities.MainActivity.StartReadDataFlag;
import static com.example.administrator.wrongtry.activities.activities.MainActivity.realm;

public class NdefFragment extends Fragment implements OnClickListener{

	private static TextView ndefTextone,ndefTexttwo,ndefTextthree,ndefTextfour;
	private static Button readNdefButton,correctionButton;
    private static ArrayList<MyData> DataList;
    public static Chronometer timer;
    public static float CurrentGlu;
    public static float CurrentNa;
    public static float CurrentK;
    public static float CurrentH;
    public ArrayList<Float> WrongGlucose;
    public ArrayList<Float> WrongNa;
    public ArrayList<Float> WrongK;
    public ArrayList<Float> WrongpH;
    public int WrongCount;
    public boolean timerFlag;// Not sure to use static or not


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.fragment_ndef, container, false);

		ndefTextone = layout.findViewById(R.id.ndefTextone);
		ndefTexttwo = layout.findViewById(R.id.ndefTexttwo);
		ndefTextthree = layout.findViewById(R.id.ndefTextthree);
		ndefTextfour = layout.findViewById(R.id.ndefTextfour);
		readNdefButton = layout.findViewById(R.id.readNdefButton);
		correctionButton = layout.findViewById(R.id.correctionButton);
        timer = layout.findViewById(R.id.timer);
		readNdefButton.setOnClickListener(this);
		correctionButton.setOnClickListener(this);
        // Operations for database
		PerformClickFlag = true;// initial this flag as true
        RecordCurrentTime = true;// initial this flag as true



        //Plot();
        new TimeThread().start();
        DataList = new ArrayList<MyData>();

        AddWrongData();
        WrongCount = 0;
        timerFlag = true;

		return layout;
	}



    public void onClick(View v) {
		switch (v.getId()) {
		case R.id.readNdefButton:
			// Reset the values of the view
			MainActivity.demo.NDEFReadFinish();
			ndefTextone.setVisibility(View.VISIBLE);
			ndefTexttwo.setVisibility(View.VISIBLE);
			ndefTextthree.setVisibility(View.VISIBLE);
			ndefTextfour.setVisibility(View.VISIBLE);
			// Read content
			if (MainActivity.demo.isReady()) {
				MainActivity.demo.finishAllTasks();
                // Go into NTAG_I2C_Demo
				MainActivity.launchNdefDemo(MainActivity.getAuthStatus(), MainActivity.getPassword());
			}
			break;

        case R.id.correctionButton:
            Intent intent = new Intent(getActivity(),SramTestActivity.class);
            startActivity(intent);
            break;

		default:
			break;
		}
	}

	public static void resetNdefDemo() {
		/*if (writeChosen == true) {
            setAnswer("Tap tag to write NDEF content");
        } else {
            setAnswer("Tap tag to read NDEF content");
        }*/
		/*setNdefMessage("");
		setNdefType("");
		setDatarate("");
		setNdefType("");*/
	}
	
	public static void setDatarate(String datarate) {
		//ndefDataRateCallback.setText(datarate);
	}

	public static void setndefwrongflag() {
		if(PerformClickFlag == true){
			readNdefButton.performClick();// again
		}
	}

	public static void setNdefType(String type) {
		//ndefTypeText.setText(type);
	}

	public static void setNdefMessage(String answer) {
		/*Log.d("MyAnswer",answer);
		String[] strarray = answer.split("\\.",4);
		strarray[3] = answer.substring(15,19);
		Log.d("MyAnswer",strarray[3]);
        if(RecordCurrentTime == true){
            RecordCurrentTime = false;
            CurrentTime = System.currentTimeMillis();
        }
        CurrentGlu = Parse(strarray[0]);
        Log.d("MyFuckingTest", "This is the ADD DATA CurrentTime"+String.valueOf(CurrentTime));
        DataList.add(new MyData(System.currentTimeMillis(), Parse(strarray[0]),Parse(strarray[1]),Parse(strarray[2]),Parse(strarray[3]),CurrentTime));
		if(DataList.size() > 0){
			Log.d("DataBase","beginTransaction");
			realm.beginTransaction();
			for(int j = 0 ; j < DataList.size() ; j++)
				realm.copyToRealm(DataList.get(j));{
			}
			realm.commitTransaction();
			Log.d("DataBase","commitTransaction");
		}
		ndefTextone.setText(" "+Parse(strarray[0])/1000);
		ndefTexttwo.setText(" "+Parse(strarray[1])/1000);
		ndefTextthree.setText(" "+Parse(strarray[2])/1000);
		ndefTextfour.setText(" "+Parse(strarray[3])/1000);*/
	}

	/*public void Plot() {
        DataList.add(new MyData(System.currentTimeMillis(), (float) (Math.random() * 10000f),
                (float) (Math.random() * 10000f),(float) (Math.random() * 10000f),
                (float) (Math.random() * 10000f),CurrentTime));
        if(DataList.size() > 0) {
            Log.d("DataBase","beginTransaction");
            realm.beginTransaction();
            for(int j = 0 ; j < DataList.size() ; j++)
                realm.copyToRealm(DataList.get(j));{
            }
            realm.commitTransaction();
            Log.d("DataBase","commitTransaction");
        }

        RealmResults<MyData> MyResult = realm.where(MyData.class).findAll();
        initChartView0(MyResult);
        ArrayList<Entry> yVals0 = new ArrayList<Entry>();
        if(MyResult.size() <= 0){
            Log.d("ArrayListZero", String.valueOf(MyResult.size()));
            return ;
        }
        int k = 0 ;
        for (int i = 0; i < MyResult.size(); i++){
            //yVals.add(new Entry(i, (float) (Math.random() * 10000f)));
            Log.d("MyFuckingTest", "This is the PLOT IF CurrentTime"+String.valueOf(CurrentTime));
            if(MyResult.get(i).getRecord() == CurrentTime){
                k++;
                Log.d("ArrayListZero", "Enter ADD DATA IN FOR  NOW");
                yVals0.add(new Entry((k-1), MyResult.get(i).getGlu()));
            }
        }
        *//*if(yVals0.size() > 0){
            addDataSet0(yVals0, "Glu");
            Legend l0 = mChart0.getLegend();
            l0.setForm(Legend.LegendForm.LINE);
            l0.setXEntrySpace(40);
        }*//*
    }*/


	public static float Parse(String string){

		float wan,qian,bai,shi,ge,temp;

		temp = 0;// init

		try {
			temp = Integer.parseInt(string);
		}catch (NumberFormatException e){
			e.printStackTrace();
		}

		// Copy from MSP430 Project
		float receivedata = (float) ((temp/40.96)*250);

		wan=receivedata/10000;
		receivedata=receivedata%10000;
		qian=receivedata/1000;
		receivedata=receivedata%1000;
		bai=receivedata/100;
		receivedata=receivedata%100;
		shi=receivedata/10;
		receivedata=receivedata%10;
		ge=receivedata;

		//return (float) (wan+qian*0.1+bai*0.01+shi*0.001+ge*0.0001);
        return temp;
	}

    private class TimeThread extends Thread {
        @Override
        public void run() {
            do{
                try {
                    Thread.sleep(2000);
                    Message msg = new Message();
                    msg.what = 1;
					msg.what = 2;
                    mHandler.sendMessage(msg);// T=0.5s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    // refresh
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                //  Plot();
                    break;
				case 2:
				    if(StartReadDataFlag == true) {
                        doWrongThing();
                    }
					break;
                default:
                    break;

            }
        }
    };

    private void doWrongThing() {

        if(timerFlag == true) {
            timerFlag = false;
            timer.setBase(SystemClock.elapsedRealtime());
            int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
            timer.setFormat("0"+String.valueOf(hour)+":%s");
            timer.start();
        }
        Log.d("doWrongThing","doWrongThing");
        if(WrongCount <= 38) {
            WrongCount++;
            if (RecordCurrentTime == true) {
                RecordCurrentTime = false;
                CurrentTime = System.currentTimeMillis();
            }
            CurrentGlu = WrongGlucose.get(WrongCount);
            CurrentH = WrongpH.get(WrongCount);
            CurrentNa = WrongNa.get(WrongCount);
            CurrentK = WrongK.get(WrongCount);
            DataList.add(new MyData(System.currentTimeMillis(), WrongGlucose.get(WrongCount), WrongNa.get(WrongCount),
                    WrongK.get(WrongCount), WrongpH.get(WrongCount), CurrentTime));
            if(DataList.size() > 0){
                Log.d("DataBase","beginTransaction");
                realm.beginTransaction();
                for(int j = 0 ; j < DataList.size() ; j++)
                    realm.copyToRealm(DataList.get(j));{
                }
                realm.commitTransaction();
                Log.d("DataBase","commitTransaction");
            }

            ndefTextone.setText(" "+WrongGlucose.get(WrongCount));
            ndefTexttwo.setText(" "+WrongpH.get(WrongCount));
            ndefTextthree.setText(" "+WrongNa.get(WrongCount));
            ndefTextfour.setText(" "+WrongK.get(WrongCount));
        }
        else
        {
            WrongCount = 0;
        }
    }

    private void AddWrongData() {
        WrongGlucose = new ArrayList<>();
        WrongNa = new ArrayList<>();
        WrongK = new ArrayList<>();
        WrongpH = new ArrayList<>();
        WrongGlucose.add(820f);
        WrongGlucose.add(608.8f);
        WrongGlucose.add(464.4f);
        WrongGlucose.add(364.4f);
        WrongGlucose.add(293.84f);
        WrongGlucose.add(241.84f);
        WrongGlucose.add(206.24f);
        WrongGlucose.add(179.48f);
        WrongGlucose.add(158.48f);
        WrongGlucose.add(143.16f);
        WrongGlucose.add(132.12f);
        WrongGlucose.add(122.96f);
        WrongGlucose.add(116.24f);
        WrongGlucose.add(110.8f);
        WrongGlucose.add(106.36f);
        WrongGlucose.add(103.16f);
        WrongGlucose.add(100.2f);
        WrongGlucose.add(97.12f);
        WrongGlucose.add(95.56f);
        WrongGlucose.add(94.28f);
        WrongGlucose.add(92.04f);
        WrongGlucose.add(91.16f);
        WrongGlucose.add(90.08f);
        WrongGlucose.add(88.84f);
        WrongGlucose.add(87.84f);
        WrongGlucose.add(87.32f);
        WrongGlucose.add(85.64f);
        WrongGlucose.add(85.88f);
        WrongGlucose.add(85.12f);
        WrongGlucose.add(84.36f);
        WrongGlucose.add(83.88f);
        WrongGlucose.add(83.12f);
        WrongGlucose.add(82.6f);
        WrongGlucose.add(82.32f);
        WrongGlucose.add(81.68f);
        WrongGlucose.add(81.12f);
        WrongGlucose.add(80.68f);
        WrongGlucose.add(79.92f);
        WrongGlucose.add(79.8f);
        WrongGlucose.add(79.52f);
        WrongNa.add(46f);
        WrongNa.add(46.3f);
        WrongNa.add(46.6f);
        WrongNa.add(47.2f);
        WrongNa.add(47.2f);
        WrongNa.add(47.5f);
        WrongNa.add(47.5f);
        WrongNa.add(48.2f);
        WrongNa.add(48.2f);
        WrongNa.add(48.8f);
        WrongNa.add(49.1f);
        WrongNa.add(49.1f);
        WrongNa.add(49.1f);
        WrongNa.add(49.4f);
        WrongNa.add(49.7f);
        WrongNa.add(49.7f);
        WrongNa.add(50.1f);
        WrongNa.add(50.4f);
        WrongNa.add(50.4f);
        WrongNa.add(50.7f);
        WrongNa.add(51f);
        WrongNa.add(51f);
        WrongNa.add(51f);
        WrongNa.add(48.8f);
        WrongNa.add(47.8f);
        WrongNa.add(47.8f);
        WrongNa.add(47.8f);
        WrongNa.add(47.8f);
        WrongNa.add(48.5f);
        WrongNa.add(48.8f);
        WrongNa.add(48.5f);
        WrongNa.add(49.1f);
        WrongNa.add(49.4f);
        WrongNa.add(49.7f);
        WrongNa.add(50.1f);
        WrongNa.add(50.4f);
        WrongNa.add(50.7f);
        WrongNa.add(51f);
        WrongNa.add(51f);
        WrongNa.add(51f);
        WrongK.add(6.4f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.2f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.2f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.2f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.2f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.2f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.2f);
        WrongK.add(6.4f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongK.add(6.3f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
        WrongpH.add(6.7f);
    }
}
