package com.xl.projectno.model;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by moon.zhong on 2015/3/2.
 */
public class FormImage {

    private String user ;

    private String mFileName ;

    private String mValue ;

    private String mMime ;

    private Bitmap mBitmap ;

    public FormImage(String user,String mFileName,Bitmap mBitmap) {
        this.user = user;
        this.mFileName=mFileName;
        this.mBitmap = mBitmap;
    }

    public String getUser() {
//        return mName;
        return user ;
    }

    public String getFileName() {
        return user+".png";
    }

    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        mBitmap.compress(Bitmap.CompressFormat.JPEG,80,bos) ;
        return bos.toByteArray();
    }

    public String getMime() {
        return "image/png";
    }
}
