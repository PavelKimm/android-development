package ru.tpu.courses.lab4;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "groups")
public class Group extends ListItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name = "group_name")
    public String groupName;

    public Group(@NonNull String groupName) {
        this.groupName = groupName;
    }

    protected Group(Parcel in) {
        id = in.readInt();
        groupName = Objects.requireNonNull(in.readString());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(groupName);
        dest.writeInt(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return groupName.equals(group.groupName);
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return groupName;
    }

    @Override
    public int getType() {
        return TYPE_GROUP;
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(groupName);
    }
}
