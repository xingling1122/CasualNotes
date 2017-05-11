package com.xl.projectno.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by Administrator on 11-8-0008.
 */
public class DrawCircle {
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(x>y?y:x,
                x>y?y:x, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, x>y?y:x, x>y?y:x);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle((x>y?y:x) / 2, (x>y?y:x) / 2, x>y?y/2:x/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public static Bitmap getRoundeBitmapWithWhite(Bitmap bitmap) {
        int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth()
                : bitmap.getHeight();
        int num = 14;
        int sizebig = size + num;
        Bitmap output = Bitmap.createBitmap(sizebig, sizebig, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = Color.parseColor("#FFFFFF");
        final Paint paint = new Paint();
        final float roundPx = sizebig / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        RadialGradient gradient = new RadialGradient(roundPx, roundPx, roundPx,
                new int[] { Color.WHITE, Color.WHITE,
                        Color.parseColor("#AAAAAAAA") }, new float[] { 0.f,
                0.97f, 1.0f }, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);
        return output;
    }

}
