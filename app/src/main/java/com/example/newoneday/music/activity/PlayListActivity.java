package com.example.newoneday.music.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.newoneday.R;
import com.example.newoneday.music.adapter.SongAdapter;
import com.example.newoneday.music.data.Song;
import com.example.newoneday.music.model.App;
import com.example.newoneday.music.model.NetworkStatesReceiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PlayListActivity extends AppCompatActivity{

    public static List<Song> songList = new ArrayList<Song>();
    private SeekBar playerBar;
    private TextView curretTime;
    private TextView totalTime;
    public static boolean checkPlayingState = false;
    public static boolean canPlay = false;
    public HashMap<String,Integer> AudioPositionMap=new HashMap<>();
    public  boolean ifAppear = true;
    public  boolean tag = true;
    private Timer timer = new Timer();

    public static boolean ifSelectedCancel =  false;

    public static AlertDialog.Builder builder;
    public static List<String> info = new ArrayList<>();
    //    public HashMap<String, String> info = new HashMap<>();
    public static List<Integer> value =  new ArrayList<>();
//    public static List<Boolean> likeOrNot = new ArrayList<>();
//    public static List<Integer> AlbumPic =  new ArrayList<>();
    public static HashMap<Integer,String> AudioUrlList = new HashMap<>();
    public static HashMap<Integer,Song> list = new HashMap<>();
    public static Integer itemCount = 0;
    public ImageView playerImageView;
    public MediaPlayer mediaPlayer = new MediaPlayer();
    public HttpProxyCacheServer proxy;
    public String proxyUrl;
    public NetworkStatesReceiver networkStatesReceiver;
    private TextView songInfo;
    private ImageView lastSong;
    private ImageView nextSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerBar = findViewById(R.id.player_seekbar_music_playlist_activity);
        playerImageView = findViewById(R.id.player_play_imageview__music_playlist_activity);
        curretTime = findViewById(R.id.curret_time_music_playlist_activity);
        totalTime = findViewById(R.id.total_time_music_playlist_activity);
        songInfo = findViewById(R.id.song_info_music_playlist_activity);
        playerImageView.setOnClickListener(new PlaySongs());
        playerBar.setOnSeekBarChangeListener(new seekBarChanged());
        lastSong = findViewById(R.id.last_song_imageview__music_playlist_activity);
        nextSong = findViewById(R.id.next_song_imageview__music_playlist_activity);
        lastSong.setOnClickListener(new toLastSong());
        nextSong.setOnClickListener(new toNextSong());
        initSongs();


//        initMediaPlayer();
        final SongAdapter adapter = new SongAdapter(PlayListActivity.this, R.layout.listview_music_playlist_song_item, songList);
        ListView listView = (ListView) findViewById(R.id.playlist_listview_music_playlist_activity);
        listView.setAdapter(adapter);
        itemCount = listView.getAdapter().getCount();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                initMediaPlayer();

                Song song =  list.get(position);
                SongAdapter.keyVaule = position;
                curretTime.setText("00:00");
                mediaPlayer.seekTo(0);
                if(proxy == null){
                    proxy = new HttpProxyCacheServer(getApplicationContext());
                }

                if(SongAdapter.keyVaule != null){
                    proxyUrl = proxy.getProxyUrl(getValue(AudioUrlList,SongAdapter.keyVaule));

                }else{
                    proxyUrl = null;
                }


                initMediaPlayer();
            }
        });

        if(proxy == null){
            proxy = new HttpProxyCacheServer(getApplicationContext());
        }

        if(SongAdapter.keyVaule != null){
            proxyUrl = proxy.getProxyUrl(getValue(AudioUrlList,SongAdapter.keyVaule));

        }else{
            proxyUrl = null;
        }


        initMediaPlayer();

        timer.schedule(timerTask,0,1000);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("tag", "播放完毕");
                playerImageView.setImageResource(R.drawable.pause);
//                        phonograph.setPlaying(true);
            }
        });


    }

    class toLastSong implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if(SongAdapter.keyVaule != null && SongAdapter.keyVaule != 0){
                SongAdapter.keyVaule--;
                playerImageView.setImageResource(R.drawable.pause);
                playerBar.setProgress(0);
                String cTime = timeFormat(0);
                curretTime.setText(cTime);
                initMediaPlayer();
            }else if(SongAdapter.keyVaule != null && SongAdapter.keyVaule == 0){
                Toast.makeText(getApplicationContext(),"当前歌曲为第一首",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"您尚未选中任何歌曲",Toast.LENGTH_SHORT).show();
            }
        }
    };

    class toNextSong implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(SongAdapter.keyVaule != null && (SongAdapter.keyVaule + 1) < PlayListActivity.itemCount){
                Log.d("testNumber", PlayListActivity.itemCount+"");
                SongAdapter.keyVaule++;
                playerImageView.setImageResource(R.drawable.pause);
                playerBar.setProgress(0);
                String cTime = timeFormat(0);
                curretTime.setText(cTime);

                initMediaPlayer();
            }else if(SongAdapter.keyVaule != null && (SongAdapter.keyVaule + 1) == PlayListActivity.itemCount){
                Toast.makeText(getApplicationContext(),"当前歌曲已是最后一首",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"您尚未选中任何歌曲",Toast.LENGTH_SHORT).show();
            }
        }
    };

    class PlaySongs implements View.OnClickListener{
        @Override
        public void onClick(View v){
            int progress=getCurrentProgress();

            if(progress < 100){

                if(SongAdapter.keyVaule != null){
                    if(isPlaying()){
                        pause();
                        playerImageView.setImageResource(R.drawable.pause);
                    }else{
                        if(NetworkStatesReceiver.ifSelectedCancel){
                            Toast.makeText(PlayListActivity.this, "网络提示 当前无wifi连接，是否使用移动数据继续播放？", Toast.LENGTH_SHORT).show();
//                            NetworkStatesReceiver.setValueOfDialog("网络提示","当前无wifi连接，是否使用移动数据继续播放？",NetworkStatesReceiver.ifSelectedCancel);
                        }else{
                            playerImageView.setImageResource(R.drawable.playing);
                            mediaPlayer.start();
                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"您当前没有选中歌曲",Toast.LENGTH_SHORT).show();
                }
            }else{
                stop();
                Log.d("TestPause","Pause");
                playerImageView.setImageResource(R.drawable.pause);
            }
        }
    };

    public static void setValueOfDialog(String title, String message, boolean ifAppear){

        PlayListActivity.builder.setIcon(R.drawable.dialog_icon);
        PlayListActivity.builder.setTitle(title);
        PlayListActivity.builder.setMessage(message);
        PlayListActivity.builder.setView(R.layout.custom_dialog);


        //监听下方button点击事件
        if(ifAppear){
            PlayListActivity.builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    canPlay = true;
                    ifSelectedCancel = false;
                }
            });

            PlayListActivity.builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    canPlay = false;
                    ifSelectedCancel = true;
                }
            });

            PlayListActivity.builder.setCancelable(true);
            AlertDialog dialog=PlayListActivity.builder.create();
            dialog.show();
        }
    }


    public void pause(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
            playerImageView.setImageResource(R.drawable.pause);
        }
        savePlayPosition(getValue(AudioUrlList,SongAdapter.keyVaule),getCurrentProgress());
    }

    public void savePlayPosition(String audioUrl, int currentProgress) {

        //用hashMap来存储一对URL和其对应的播放进度,形成对应关系

        if(currentProgress<0){
            return;
        }
        AudioPositionMap.put(audioUrl,currentProgress);
    }

    public void seekTo(int position){
        if(mediaPlayer!=null){
            mediaPlayer.seekTo(position);
        }
    }

    public boolean isPlaying(){
        if(mediaPlayer == null){
            checkPlayingState = false;
        }else{
            checkPlayingState = mediaPlayer.isPlaying();
        }
        return checkPlayingState;
    }

    public void stop(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            playerImageView.setImageResource(R.drawable.pause);
            mediaPlayer=null;

        }
    }

    private void initMediaPlayer(){
        try{
            if(mediaPlayer==null){
                mediaPlayer=new MediaPlayer();
            }
            songInfo.setText(PlayListActivity.info.get(SongAdapter.keyVaule));
            if(SongAdapter.keyVaule != 0){

                mediaPlayer.reset();
                proxyUrl = proxy.getProxyUrl(getValue(AudioUrlList,SongAdapter.keyVaule));
                mediaPlayer.setDataSource(proxyUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.start();
            }else if(SongAdapter.keyVaule == 0){

                mediaPlayer.reset();
                proxyUrl = proxy.getProxyUrl(getValue(AudioUrlList,SongAdapter.keyVaule));
                mediaPlayer.setDataSource(proxyUrl);
                mediaPlayer.prepareAsync();
                mediaPlayer.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getValue(HashMap<Integer,String> map, Integer value){
        buildAudioList();
        String keyUrl = null;

        keyUrl = map.get(value);
        return keyUrl;
    }




    public int getCurrentProgress(){
        int currentProgress;
        if(playerBar==null){
            currentProgress=-1;
        }else{
            currentProgress=playerBar.getProgress();
        }
        return currentProgress;
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try{
                if(mediaPlayer == null){
                    return;
                }else if(mediaPlayer.isPlaying() && playerBar.isPressed() == false){
                    handleProgress.sendEmptyMessage(0);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    Handler handleProgress = new Handler(){
        public void handleMessage(Message msg){
            if(mediaPlayer != null){
                int position = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if(duration>0){
                    long pos = (playerBar.getMax() * (long) position) / duration;
                    playerBar.setProgress((int) pos);

                    String cTime = timeFormat(position);

                    String tTime = timeFormat(duration);
                    if(position == duration-1000){
                        Log.d("TestTime","get");
                        playerImageView.setImageResource(R.drawable.pause);
                    }
                    totalTime.setText(tTime);
                    curretTime.setText(cTime);
                }
            }
        }
    };

    class seekBarChanged implements SeekBar.OnSeekBarChangeListener{

        public int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int fprogress, boolean b) {
            if(mediaPlayer == null){
                return;
            }
            this.progress= fprogress * mediaPlayer.getDuration() / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            seekTo(progress);

        }
    }
    public String timeFormat(int milli){


        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return (mm+":"+ss) ;
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //    获取当前进程名
    private String getProcessName(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
                if (proInfo.pid == android.os.Process.myPid()) {
                    if (proInfo.processName != null) {
                        return proInfo.processName;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private HttpProxyCacheServer getProxy() {
        return App.getProxy(getApplicationContext());
    }

    public void initSongs() {
        if(itemCount == 0) {
            Song s0 = new Song("白日梦 -", "林海", 0);
            songList.add(s0);
            value.add(s0.getSongId());
            info.add(s0.getSongName() + s0.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s0.getSongId(), s0);

            Song s1 = new Song("Summer -", "久石让", 1);
            songList.add(s1);
            value.add(s1.getSongId());
            info.add(s1.getSongName() + s1.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s1.getSongId(), s1);

            Song s2 = new Song("conc. n. 1 G-dur gavotte -", "Francois", 2);
            songList.add(s2);
            value.add(s2.getSongId());
            info.add(s2.getSongName() + s2.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s2.getSongId(), s2);

            Song s3 = new Song("Nature's Path -", "Dan Gibson", 3);
            songList.add(s3);
            value.add(s3.getSongId());
            info.add(s3.getSongName() + s3.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s3.getSongId(), s3);

            Song s4 = new Song("Blue Dragon (piano & guitar ver) -", "Sawano Hiroyuki", 4);
            songList.add(s4);
            value.add(s4.getSongId());
            info.add(s4.getSongName() + s4.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s4.getSongId(), s4);

            Song s5 = new Song("Krone -", "Sawano Hiroyuki", 5);
            songList.add(s5);
            value.add(s5.getSongId());
            info.add(s5.getSongName() + s5.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s5.getSongId(), s5);

            Song s6 = new Song("Between Worlds -", "Roger Subirana", 6);
            songList.add(s6);
            value.add(s6.getSongId());
            info.add(s6.getSongName() + s6.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s6.getSongId(), s6);

            Song s7 = new Song("只要平凡（电影《我不是药神》主题曲） -","张杰/张碧晨", 7);
            songList.add(s7);
            value.add(s7.getSongId());
            info.add(s7.getSongName() + s7.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s7.getSongId(), s7);

            Song s8 = new Song("可能否（30秒铃声） -","木小雅", 8);
            songList.add(s8);
            value.add(s8.getSongId());
            info.add(s8.getSongName() + s8.getSongSinger());
//        添加HashMap<songId,Song>
            list.put(s8.getSongId(), s8);

            Song s9 = new Song("喜欢你 (原唱: Beyond)  -","邓紫棋",9);
            songList.add(s9);
            value.add(s9.getSongId());
            info.add(s9.getSongName()+s9.getSongSinger());
            list.put(s9.getSongId(),s9);
        }

    }

    public void buildAudioList(){

        AudioUrlList.put(0,"http://other.web.rc01.sycdn.kuwo.cn/resource/n3/35/24/1696413445.mp3");
        AudioUrlList.put(1,"http://other.web.ra01.sycdn.kuwo.cn/resource/n2/128/7/8/1186605667.mp3");
        AudioUrlList.put(2,"http://other.web.rz01.sycdn.kuwo.cn/resource/n3/91/63/2781880125.mp3");
        AudioUrlList.put(3,"http://other.web.rs03.sycdn.kuwo.cn/resource/n2/25/69/3107065490.mp3");
        AudioUrlList.put(4, "http://other.web.rc01.sycdn.kuwo.cn/resource/n3/85/64/422509277.mp3");
        AudioUrlList.put(5, "http://other.web.rh01.sycdn.kuwo.cn/resource/n1/43/38/2896969580.mp3");
        AudioUrlList.put(6, "http://other.web.rz01.sycdn.kuwo.cn/resource/n1/59/56/1216614326.mp3");

        AudioUrlList.put(7,"http://nf01.sycdn.kuwo.cn/resource/n1/25/35/401360027.mp3");
        AudioUrlList.put(8,"http://nf01.sycdn.kuwo.cn/resource/n2/59/7/2076410040.mp3");
        AudioUrlList.put(9,"http://re01.sycdn.kuwo.cn/resource/n2/69/33/1625071345.mp3");
//        AudioUrlList.put(4, "http://other.web.rc01.sycdn.kuwo.cn/resource/n3/85/64/422509277.mp3");
//        AudioUrlList.put(4, "http://other.web.rc01.sycdn.kuwo.cn/resource/n3/85/64/422509277.mp3");


    }

}
