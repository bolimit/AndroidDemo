package com.marco.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author yangbo
 */

public class Config implements Parcelable {
    public static final Creator<Config> CREATOR = new Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
    private int num;

    public Config() {
    }

    protected Config(Parcel in) {
        this.num = in.readInt();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.num);
    }
}
