package com.github.tvbox.osc.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class IdNameAddressBean implements Serializable, Parcelable {
    private int id;
    private String name;
    private String address;

    public IdNameAddressBean() {
    }

    public IdNameAddressBean(String name, String address) {
        this.name = name;
        this.address = address;
    }

    protected IdNameAddressBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
    }

    public static final Creator<IdNameAddressBean> CREATOR = new Creator<IdNameAddressBean>() {
        @Override
        public IdNameAddressBean createFromParcel(Parcel in) {
            return new IdNameAddressBean(in);
        }

        @Override
        public IdNameAddressBean[] newArray(int size) {
            return new IdNameAddressBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResult() {
        return id + ", " + name + ", " + address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(address);
    }
}
