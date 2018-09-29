package com.example.administrator.wrongtry.activities.data;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

public class MyData extends RealmObject implements RealmModel {
    private float Glu;
    private float Na;
    private float K;
    private float pH;
    private float time;
    private float record;

    public MyData() {

    }

    public float getNa() {
        return Na;
    }

    public void setNa(float na) {
        Na = na;
    }

    public float getK() {
        return K;
    }

    public void setK(float k) {
        K = k;
    }

    public float getpH() {
        return pH;
    }

    public void setpH(float pH) {
        this.pH = pH;
    }

    public MyData(float time, float glu, float Na, float K, float pH, float record) {
        this.Glu = glu;
        this.Na = Na;
        this.K = K;
        this.pH = pH;
        this.time = time;
        this.record = record;

    }

    public float getRecord() {
        return record;
    }

    public void setRecord(float record) {
        this.record = record;
    }

    public float getGlu() {
        return Glu;
    }

    public void setGlu(float glu) {
        Glu = glu;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
