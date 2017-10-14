package org.soraworld.fourchess;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Welcome extends Activity {

    int queen = 0;
    NumberProgressBar progress_bar;
    private Thread load_thread = null;//声明load_thread线程
    final Handler load_handler = new Handler();
    final Runnable load_run = new Runnable() {
        @Override
        public void run() {
            progress_bar.setProgress(queen);
        }
    };
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progress_bar = findViewById(R.id.loading);
        image = findViewById(R.id.welback);
        setBackRand();
        Loading();//load_thread线程初始化
        load_thread.start();//启动load_thread线程
    }

    public void setBackRand() {
        int rand;
        rand = (int) (Math.random() * 7 + 1);
        switch (rand) {
            case 1:
                image.setImageResource(R.drawable.pic01);
                break;
            case 2:
                image.setImageResource(R.drawable.pic02);
                break;
            case 3:
                image.setImageResource(R.drawable.pic03);
                break;
            case 4:
                image.setImageResource(R.drawable.pic04);
                break;
            case 5:
                image.setImageResource(R.drawable.pic05);
                break;
            case 6:
                image.setImageResource(R.drawable.pic06);
                break;
            case 7:
                image.setImageResource(R.drawable.pic07);
                break;
        }
    }

    //load_thread线程初始化
    public void Loading() {
        load_thread = new Thread(() -> {
            WallpaperManager wpman = WallpaperManager.getInstance(Welcome.this);
            Drawable drw = wpman.getDrawable();
            Bitmap bmp = ((BitmapDrawable) drw).getBitmap();
            try {
                doBlur(bmp, 70);//高斯模糊半径
            } catch (IOException e) {
                e.printStackTrace();
            }
            ///////////////////////////////////////
            //跳转页面
            //////////////////////////////////////
            Intent intent = new Intent(Welcome.this, MyMenu.class);
            Welcome.this.startActivity(intent);
            Welcome.this.finish();
        });
    }

    //获取SD卡路径
    public static String getSDPath() {
        File SDdir = null;
        boolean sdExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdExist) {
            SDdir = Environment.getExternalStorageDirectory();
        }
        if (SDdir != null) {
            return SDdir.toString();
        } else
            return null;
    }

    //保存Bitmap
    public void saveBitmap(Bitmap bmp) throws IOException {
        String path = getSDPath() + "/Android/data/org.soraworld.fourchess/";
        File dirFile = new File(path, "missyou.png");
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirFile));
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
        queen = 100;
        load_handler.post(load_run);
    }

    //高斯模糊并保存到data目录
    public void doBlur(Bitmap bmp, int radius) throws IOException {

        Bitmap bitmap;
        bitmap = bmp.copy(bmp.getConfig(), true);
        if (radius < 1) {
            try {
                saveBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int times = 0, all = (w + h);

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int r_sum, g_sum, b_sum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {

            times++;
            queen = 99 * times / all;
            load_handler.post(load_run);
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = r_sum = g_sum = b_sum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                r_sum += sir[0] * rbs;
                g_sum += sir[1] * rbs;
                b_sum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[r_sum];
                g[yi] = dv[g_sum];
                b[yi] = dv[b_sum];

                r_sum -= routsum;
                g_sum -= goutsum;
                b_sum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                r_sum += rinsum;
                g_sum += ginsum;
                b_sum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {

            times++;
            queen = 99 * times / all;
            load_handler.post(load_run);

            rinsum = ginsum = binsum = routsum = goutsum = boutsum = r_sum = g_sum = b_sum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                r_sum += r[yi] * rbs;
                g_sum += g[yi] * rbs;
                b_sum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[r_sum] << 16)
                        | (dv[g_sum] << 8) | dv[b_sum];

                r_sum -= routsum;
                g_sum -= goutsum;
                b_sum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                r_sum += rinsum;
                g_sum += ginsum;
                b_sum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        saveBitmap(bitmap);
    }
}