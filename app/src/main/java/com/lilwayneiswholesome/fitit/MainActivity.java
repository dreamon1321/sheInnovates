package com.lilwayneiswholesome.fitit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.api.services.vision.v1.Vision;
import com.lilwayneiswholesome.fitit.util.VisionHelper;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onSelectPhotoButtonClicked);
    }

    public void onSelectPhotoButtonClicked(View view) {
        int rid = getResources()
                .getIdentifier("test_chair","raw", getPackageName());


        Observable.fromCallable(() -> {
            String result = null;
            InputStream is = getResources().openRawResource(rid);
            byte[] bytes = IOUtils.toByteArray(is);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();

            result = VisionHelper.doVision(byteArray);
            return result;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    Log.e(TAG, "result: " + result);
                }, error -> {
            Log.e(TAG, "Error: " + error);
        });

    }


}
