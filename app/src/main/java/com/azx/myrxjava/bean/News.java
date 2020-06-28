package com.azx.myrxjava.bean;

public class News {

    public News(long mTimestamp, String mNewContent, String mAuthor) {
        this.mTimestamp = mTimestamp;
        this.mNewContent = mNewContent;
        this.mAuthor = mAuthor;
    }

    public long mTimestamp;
    public String mNewContent;

    @Override
    public String toString() {
        return "News{" +
                "mTimestamp=" + mTimestamp +
                ", mNewContent='" + mNewContent + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                '}';
    }

    public String mAuthor;
}
