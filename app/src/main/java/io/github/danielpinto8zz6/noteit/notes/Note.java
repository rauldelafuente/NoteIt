package io.github.danielpinto8zz6.noteit.notes;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Note implements Parcelable {
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CONTENT = "content";
    private static final String COL_CREATE_DATE = "create_date";
    private static final String COL_NOTIFY_DATE = "notify_date";
    private static final String COL_EDITED_DATE = "notify_date";
    private static final String COL_COLOR = "color";
    private static final String COL_STATUS = "status";
    private static final String COL_IMAGE = "image";
    private static final String COL_TYPE = "type";

    private Integer id;
    private String title;
    private String content;

    private String create_date;
    private String notify_date;

    private String edited_date;
    private String color;
    private Integer status = 0;
    private String image;

    private Integer type = 0;

    public Note() {
    }

    public Note(Integer id, String title, String content, String create_date, String notify_date, String edited_date, String color, Integer status, String image, Integer type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.create_date = create_date;
        this.notify_date = notify_date;
        this.edited_date = edited_date;
        this.color = color;
        this.status = status;
        this.image = image;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getNotify_date() {
        return notify_date;
    }

    public void setNotify_date(String notify_date) {
        this.notify_date = notify_date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEdited_date() {
        return edited_date;
    }

    public void setEdited_date(String edited_date) {
        this.edited_date = edited_date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putInt(COL_ID, this.id);
        b.putString(COL_TITLE, this.title);
        b.putString(COL_CONTENT, this.content);
        b.putString(COL_CREATE_DATE, this.create_date);
        b.putString(COL_NOTIFY_DATE, this.notify_date);
        b.putString(COL_EDITED_DATE, this.notify_date);
        b.putString(COL_COLOR, this.color);
        b.putInt(COL_STATUS, this.status);
        b.putString(COL_IMAGE, this.image);
        b.putInt(COL_TYPE, this.type);
        return b;
    }

    public Note(Bundle b) {
        if (b != null) {
            this.id = b.getInt(COL_ID);
            this.title = b.getString(COL_TITLE);
            this.content = b.getString(COL_CONTENT);
            this.create_date = b.getString(COL_CREATE_DATE);
            this.notify_date = b.getString(COL_NOTIFY_DATE);
            this.edited_date = b.getString(COL_EDITED_DATE);
            this.color = b.getString(COL_COLOR);
            this.status = b.getInt(COL_STATUS);
            this.image = b.getString(COL_IMAGE);
            this.type = b.getInt(COL_TYPE);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                " mid=" + id +
                ", mtitle='" + title + '\'' +
                ", mcontent='" + content + '\'' +
                ", mcreate_date='" + create_date + '\'' +
                ", mnotify_date='" + notify_date + '\'' +
                ", medited_date='" + edited_date + '\'' +
                ", mcolor='" + color + '\'' +
                ", mstatus=" + status +
                ", mimage='" + image + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    protected Note(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        title = in.readString();
        content = in.readString();
        create_date = in.readString();
        notify_date = in.readString();
        edited_date = in.readString();
        color = in.readString();
        status = in.readByte() == 0x00 ? null : in.readInt();
        image = in.readString();
        type = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(create_date);
        dest.writeString(notify_date);
        dest.writeString(edited_date);
        dest.writeString(color);
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
        dest.writeString(image);
        if (type == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(type);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}