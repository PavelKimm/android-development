package ru.tpu.courses.lab4;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentGroup extends ListItem implements Parcelable {

    public String studentName;
    String groupName;

    public StudentGroup(String studentName, String groupName) {
        this.studentName = studentName;
        this.groupName = groupName;
    }

    private StudentGroup(Parcel in) {
        studentName = in.readString();
        groupName = in.readString();
    }

    @Override
    public int getType() {
        return TYPE_STUDENT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentGroup> CREATOR = new Creator<StudentGroup>() {
        @Override
        public StudentGroup createFromParcel(Parcel in) {
            return new StudentGroup(in);
        }

        @Override
        public StudentGroup[] newArray(int size) {
            return new StudentGroup[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(studentName);
        dest.writeString(groupName);
    }
}
