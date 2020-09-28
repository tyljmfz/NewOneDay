package com.example.newoneday.music.model;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;

import java.io.File;

public class App extends Application {

    private HttpProxyCacheServer proxyCacheServer;

    public static HttpProxyCacheServer getProxy(Context context){
        App app = (App) context.getApplicationContext();

//        mediaPlayer.setDataSource(context,Uri.parse(proxyUrl));
        return app.proxyCacheServer == null ? (app.proxyCacheServer = app.newProxy()) : app.proxyCacheServer;
    }

    private HttpProxyCacheServer newProxy(){
        return new HttpProxyCacheServer(this);
    }

//    缓存的命名规则
    public class MyFileNameGenerator implements FileNameGenerator {
        public String generate(String url){
            Uri uri = Uri.parse(url);
            String audioId = uri.getQueryParameter("guid");
            return audioId +".mp3";
        }

    }

    public static File getAudioCacheDir(Context context){
        return new File(context.getExternalFilesDir("music"),"audio-cache");
    }


}
