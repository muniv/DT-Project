package com.muni.examples.aibrilcall;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CallActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<CallDto> list;
    private MediaPlayer mediaPlayer;
    private TextView title;
    private ImageView previous,play,pause,next;
    private SeekBar seekBar;
    private Button select;
    boolean isPlaying = true;
    private ContentResolver res;
    private ProgressUpdate progressUpdate;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Intent intent = getIntent();
        mediaPlayer = new MediaPlayer();
        title = (TextView)findViewById(R.id.title);
        seekBar = (SeekBar)findViewById(R.id.seekbar);


        position = intent.getIntExtra("position",0);
        list = (ArrayList<CallDto>) intent.getSerializableExtra("playlist");
        res = getContentResolver();

        previous = (ImageView)findViewById(R.id.pre);
        play = (ImageView)findViewById(R.id.play);
        pause = (ImageView)findViewById(R.id.pause);
        next = (ImageView)findViewById(R.id.next);
        select = (Button)findViewById(R.id.select);

        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        next.setOnClickListener(this);
        select.setOnClickListener(this);

        playCall(list.get(position));
        progressUpdate = new ProgressUpdate();
        progressUpdate.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                if(seekBar.getProgress()>0 && play.getVisibility()== View.GONE){
                    mediaPlayer.start();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(position+1<list.size()) {
                    position++;
                    playCall(list.get(position));
                }
            }
        });
    }

    public void playCall(CallDto callDto) {

        try {
            seekBar.setProgress(0);
            title.setText(callDto.getTitle());
            Uri callURI = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, ""+ callDto.getId());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, callURI);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            if(mediaPlayer.isPlaying()){
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }else{
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            Log.e("SimplePlayer", e.getMessage());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                    mediaPlayer.start();

                break;
            case R.id.pause:
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                break;
            case R.id.pre:
                if(position-1>=0 ){
                    position--;
                    playCall(list.get(position));
                    seekBar.setProgress(0);
                }
                break;
            case R.id.next:
                if(position+1<list.size()){
                    position++;
                    playCall(list.get(position));
                    seekBar.setProgress(0);
                }

                break;
            case R.id.select:
                String serverURL = "http://192.168.1.100/sample.php";
                String sampleData= (String) getText(R.string.app_name);
                SendOperation send = new SendOperation();
                send.setSelected_sites_id(sampleData);
                send.execute(serverURL);

                Intent intent = new Intent(
                        getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
        }
    }


    class ProgressUpdate extends Thread {
        @Override
        public void run() {
            while(isPlaying){
                try {
                    Thread.sleep(500);
                    if(mediaPlayer!=null){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (Exception e) {
                    Log.e("ProgressUpdate",e.getMessage());
                }

            }
        }
    }

    //
    private class SendOperation  extends AsyncTask<String, Void, Void> {

        // 초기화 필요
        //이 클래스에서는 HttpClient 가 아닌 123라인에서 HttpURLConnection 객체(오브젝트)를 사용합니다.
        //private final HttpClient Client = new DefaultHttpClient();//아파치서버클래스에서 변수를 전역으로 사용하기 위해서 final 선언하면서 오브젝트 생성
        private String Error = null;
        private ProgressDialog Dialog= new ProgressDialog(CallActivity.this);//현재  클래스에 Dialog 변수선언
        String send_data="";
        String sample_data="";
        public void setSelected_sites_id(String str){
            sample_data=str;
        }

        protected void onPreExecute() {
            Dialog.setMessage("잠시만 기다려 주세요..");
            Dialog.show();

            try{
                // 요청 파라미터 설정

                //selected_sites+=selected_sites_id;
                send_data +="&" + URLEncoder.encode("selected_sites", "UTF-8") + "="+sample_data;
                //selected_sites +="&" + URLEncoder.encode("data", "UTF-8") + "= data";

            } catch (UnsupportedEncodingException e) {
                // 에러 표시
                e.printStackTrace();
            }

        }
        // onPreExecute 메소드 이후 호출
        protected Void doInBackground(String... urls) {//클릭이벤트 실행시 파라미터값 urls= LongOperation().execute(serverURL);
            BufferedReader reader=null;
            // 데이터 전송
            try
            {
                // Lab code here....
                //Data를 보낼 URL =
                URL url=new URL(urls[0]);
                //POST data 요청사항을 OutputStreamWriter 를 이용하여 전송
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());

                wr.write(send_data);//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                wr.flush();//OutputStreamWriter 버퍼 메모리 비우기
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {
                    reader.close();
                }
                catch(Exception ex) {}
            }
            /*****************************************************/
            return null;
        }
        //doInBackground POST전송 후 자동실행 코드
        protected void onPostExecute(Void unused) {
            Dialog.dismiss(); //"잠시만 기다려 주세요.. 다이알로그 메세지 창 종료
            if (Error != null) {
            } else {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
