package com.jph.simple;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.jph.takephoto.model.TImage;

import java.io.File;
import java.util.ArrayList;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.cboy.me
 * GitHub:https://github.com/crazycodeboy
 * Eamil:crazycodeboy@gmail.com
 */
public class ResultActivity extends Activity {
    ArrayList<TImage>images;
    private ImageView imageView1;
    private Button button;
    private TextView textView;
    private Bitmap bitmap;
    private int a=1;
    //训练数据路径，必须包含tesseract文件夹
    static final String TESSBASE_PATH = "/storage/emulated/0/Download/tesseract/";
    //识别语言英文
    static final String DEFAULT_LANGUAGE = "eng";
    //识别语言简体中文
    static final String CHINESE_LANGUAGE = "chi_sim";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);
        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);
        images= (ArrayList<TImage>) getIntent().getSerializableExtra("images");
        showImg();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.buildDrawingCache();
                bitmap = imageView1.getDrawingCache();

if(a==1)
{
    SimpleChineseOCR();
    a=0;
}else {
    EnglishOCR();
    a=1;
}


            }
        });

    }
    public void EnglishOCR(){
        //设置图片可以缓存
  //      imageView1.setDrawingCacheEnabled(true);
        //获取缓存的bitmap
   //     final Bitmap bmp = imageView1.getDrawingCache();
        final TessBaseAPI baseApi = new TessBaseAPI();
        //初始化OCR的训练数据路径与语言
        baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        //设置要识别的图片
        baseApi.setImage(bitmap);
  //      imageView1.setImageBitmap(bmp);
        textView.setText(baseApi.getUTF8Text());
        baseApi.clear();
        baseApi.end();
    }
    public void SimpleChineseOCR(){
        //设置图片可以缓存
//        imageView1.setDrawingCacheEnabled(true);
        //获取缓存的bitmap
    //    final Bitmap bmp = imageView1.getDrawingCache();
        final TessBaseAPI baseApi = new TessBaseAPI();
        //初始化OCR的训练数据路径与语言
        baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE);
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        //设置要识别的图片
        baseApi.setImage(bitmap);
    //    imageView1.setImageBitmap(bmp);
        textView.setText(baseApi.getUTF8Text());
        baseApi.clear();
        baseApi.end();
    }
    private void showImg() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llImages);
        for (int i = 0, j = images.size(); i < j - 1; i += 2) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
             imageView1 = (ImageView) view.findViewById(R.id.imgShow1);

            ImageView imageView2 = (ImageView) view.findViewById(R.id.imgShow2);
            Glide.with(this).load(new File(images.get(i).getCompressPath())).into(imageView1);
            Glide.with(this).load(new File(images.get(i + 1).getCompressPath())).into(imageView2);
             bitmap=((BitmapDrawable)imageView1.getDrawable()).getBitmap();
            linearLayout.addView(view);
        }
        if (images.size() % 2 == 1) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
            Glide.with(this).load(new File(images.get(images.size() - 1).getCompressPath())).into(imageView1);

            linearLayout.addView(view);
        }
        imageView1.getDrawable();
   //     bitmap=((BitmapDrawable)imageView1.getDrawable()).getBitmap();
    }
}
