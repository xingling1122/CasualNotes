package com.xl.projectno.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/16.
 */
public class ImageCompress {

    public ImageCompress(){
    }


    public static Bitmap decodeBitmapResourceAndResize(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFileAndResize(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap decodeStreamAndResize(InputStream isBitmap, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBitmap, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(isBitmap, null, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }



    public Bitmap compressImage(String pathName, int sWidth, int sHeight, int finalSize){
        Bitmap bitmap = decodeBitmapFileAndResize(pathName, sWidth, sHeight);
        return compressScale(bitmap, finalSize);
    }


    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);

//        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset(); // 重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;// 每次都减少10
//        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }


    public Bitmap compressTo256(Bitmap image){
        return compressScale(image, 256f);
    }


    public Bitmap compressTo512(Bitmap image){
        return compressScale(image, 512f);
    }


    public Bitmap compressScale(Bitmap image, float reqWH) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();


        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        int scale = 1;
        if (w > h && w > reqWH) {
            scale = (int) (newOpts.outWidth / reqWH);
        } else if (w < h && h > reqWH) {
            scale = (int) (newOpts.outHeight / reqWH);
        }
        if (scale <= 0)
            scale = 1;
        newOpts.inSampleSize = scale;

        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return compressImage(bitmap);
    }

    public static Bitmap scale(Bitmap bitmap, double  scale_width, double scale_height) {
        int primaryWidth = bitmap.getWidth();               //原图片宽
        int primaryHeight = bitmap.getHeight();              //原图片高
        double scaleWidth=1;
        double scaleHeight=1; //高宽比例

        scaleWidth = scaleWidth * scale_width;  //缩放到原来的*倍
        scaleHeight = scaleHeight * scale_height;

        Matrix matrix = new Matrix();   //矩阵，用于图片比例缩放
        matrix.postScale((float)scaleWidth, (float)scaleHeight);    //设置高宽比例（三维矩阵）

        //缩放后的BitMap
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, primaryWidth, primaryHeight, matrix, true);

        //重新设置BitMap
        return newBmp;

    }

}
