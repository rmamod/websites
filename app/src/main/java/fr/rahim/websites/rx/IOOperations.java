package fr.rahim.websites.rx;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;


public class IOOperations {

    public static Observable<ArrayList<String>> read() {

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url("https://dl.dropboxusercontent.com/u/2532281/listing.txt").build();


        return Observable.create(new Observable.OnSubscribe<ArrayList<String>>() {
            @Override
            public void call(Subscriber<? super ArrayList<String>> subscriber) {
                InputStream in;
                BufferedReader reader = null;
                try {
                    ArrayList<String> sites = new ArrayList<>();
                    Response response = client.newCall(request).execute();
                    in = response.body().byteStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sites.add(line);
                    }
                    subscriber.onNext(sites);
                    subscriber.onCompleted();
                } catch (IOException e){
                    subscriber.onError(e);
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException ignored){}
                }
            }
        });
    }
}
