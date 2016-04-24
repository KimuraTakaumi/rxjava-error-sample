package com.hatenablog.techium.rxjava.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.exceptions.Exceptions;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkRxJavaOnError();
    }

    private void checkRxJavaOnError() {
        String[] urls = {"https://www.google.co.jp/?gws_rd=ssl",
                "http://localhost/",
                "http://techium.hatenablog.com/"};

        Observable
                .from(urls)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<String, Boolean>() {

                    @Override
                    public Boolean call(String s) {
                        Log.d("techium", s);
                        String result = null;
                        Request request = new Request.Builder()
                                .url(s)
                                .get()
                                .build();

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            result = response.body().string();
                        } catch (IOException e) {
                            throw Exceptions.propagate(e);
                        }

                        return true;
                    }
                }).subscribe(new Observer<Boolean>() {

            @Override
            public void onCompleted() {
                Log.d("techium", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("techium", "onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean b) {
                Log.d("techium", "onNext " + b);
            }
        });
    }

}
