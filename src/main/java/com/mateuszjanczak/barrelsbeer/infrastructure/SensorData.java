package com.mateuszjanczak.barrelsbeer.infrastructure;

public class SensorData {
    public int cid;
    public Data data;
    public int code;

    public SensorData() {
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "cid=" + cid +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}

class Data {
    public String value;

    public Data() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Data{" +
                "value='" + value + '\'' +
                '}';
    }
}