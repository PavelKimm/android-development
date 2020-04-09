package ru.tpu.courses.lab2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

class Record implements Parcelable {
    private Integer id;
    private String title;
    private Float rate;

    Record(Integer id, String title, Float rate) {
        this.id = id;
        this.title = title;
        this.rate = rate;
    }

    private Record(Parcel in) {
        id = in.readInt();
        title = in.readString();
        rate = in.readFloat();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeFloat(rate);
    }

    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    Integer getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    Float getRate() {
        return rate;
    }

    static Comparator<Record> rateComparator = new Comparator<Record>() {
        @Override
        public int compare(Record r1, Record r2) {
            return (r2.getRate().compareTo(r1.getRate()));
        }
    };
}
