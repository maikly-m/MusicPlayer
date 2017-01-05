package com.example.mrh.musicplayer.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MR.H on 2016/12/3 0003.
 */

public class MusicInfo implements Parcelable {
    private String _ID;
    private String DISPLAY_NAME;
    private String TITLE;
    private String DURATION;
    private String ARTIST;
    private String ALBUM;
    private String YEAR;
    private String MIME_TYPE;
    private String SIZE;
    private String DATA;
    private String IMAGE;
    private String LYRIC;

    public MusicInfo (String LYRIC, String _ID, String DISPLAY_NAME, String TITLE, String
            DURATION, String ARTIST, String ALBUM, String YEAR, String MIME_TYPE, String SIZE,
                      String DATA, String IMAGE) {
        this.LYRIC = LYRIC;
        this._ID = _ID;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.TITLE = TITLE;
        this.DURATION = DURATION;
        this.ARTIST = ARTIST;
        this.ALBUM = ALBUM;
        this.YEAR = YEAR;
        this.MIME_TYPE = MIME_TYPE;
        this.SIZE = SIZE;
        this.DATA = DATA;
        this.IMAGE = IMAGE;
    }
    public MusicInfo () {

    }

    public String get_ID () {
        return _ID;
    }

    public void set_ID (String _ID) {
        this._ID = _ID;
    }

    public String getDISPLAY_NAME () {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME (String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getTITLE () {
        return TITLE;
    }

    public void setTITLE (String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDURATION () {
        return DURATION;
    }

    public void setDURATION (String DURATION) {
        this.DURATION = DURATION;
    }

    public String getARTIST () {
        return ARTIST;
    }

    public void setARTIST (String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String getALBUM () {
        return ALBUM;
    }

    public void setALBUM (String ALBUM) {
        this.ALBUM = ALBUM;
    }

    public String getYEAR () {
        return YEAR;
    }

    public void setYEAR (String YEAR) {
        this.YEAR = YEAR;
    }

    public String getMIME_TYPE () {
        return MIME_TYPE;
    }

    public void setMIME_TYPE (String MIME_TYPE) {
        this.MIME_TYPE = MIME_TYPE;
    }

    public String getSIZE () {
        return SIZE;
    }

    public void setSIZE (String SIZE) {
        this.SIZE = SIZE;
    }

    public String getDATA () {
        return DATA;
    }

    public void setDATA (String DATA) {
        this.DATA = DATA;
    }

    public String getIMAGE () {

        return IMAGE;
    }

    public void setIMAGE (String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getLYRIC () {
        return LYRIC;
    }

    public void setLYRIC (String LYRIC) {
        this.LYRIC = LYRIC;
    }

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(this._ID);
        dest.writeString(this.DISPLAY_NAME);
        dest.writeString(this.TITLE);
        dest.writeString(this.DURATION);
        dest.writeString(this.ARTIST);
        dest.writeString(this.ALBUM);
        dest.writeString(this.YEAR);
        dest.writeString(this.MIME_TYPE);
        dest.writeString(this.SIZE);
        dest.writeString(this.DATA);
        dest.writeString(this.IMAGE);
        dest.writeString(this.LYRIC);
    }

    protected MusicInfo (Parcel in) {
        this._ID = in.readString();
        this.DISPLAY_NAME = in.readString();
        this.TITLE = in.readString();
        this.DURATION = in.readString();
        this.ARTIST = in.readString();
        this.ALBUM = in.readString();
        this.YEAR = in.readString();
        this.MIME_TYPE = in.readString();
        this.SIZE = in.readString();
        this.DATA = in.readString();
        this.IMAGE = in.readString();
        this.LYRIC = in.readString();
    }

    public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel (Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray (int size) {
            return new MusicInfo[size];
        }
    };
}
