package com.muni.examples.aibrilcall;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by Developer on 2017-08-15.
 */
public class CallingService extends Service {

    final BackThread thread = new BackThread();//STT 스레드

    protected View rootView;

    String call_number;
    WindowManager.LayoutParams params;
    private WindowManager windowManager;

    Intent i; //STT 인텐트
    SpeechRecognizer mRecognizer; //구글 STT SpeechRecognizer
    TextView tv; //STT결과를 보여주는 TextView
    String dataResult="";
    String startTime="";
    static String opponentNumber = "010-7742-3267";
    static CallData callData = new CallData();


    @Override
    public IBinder onBind(Intent intent) {

        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%
        int height = (int) (display.getHeight() * 0.7); //Display 사이즈의 90%

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT);



        params.gravity = Gravity.LEFT | Gravity.TOP;


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.activity_stt, null);

        tv = (TextView) rootView.findViewById(R.id.tv);

        windowManager.addView(rootView, params);


        tv.setTextSize(20);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.rgb(255, 255, 255));
        tv.setMovementMethod(new ScrollingMovementMethod());

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        // nowDate 변수에 값을 저장한다.
        startTime = sdfNow.format(date);

        tv.setText("");
        tv.setText("안재현씨 오늘 괜찮았어요.아 네 시간있으면 말씀드릴게요.음 괜찮긴 했는데 재미없어더라고요.아 영화가 별로 좋지는 않으셨구나." +
                "네 영화가 생각했던 것 보다 별로더라고요.네 감사해요 오늘 영화보고 밥먹고그런게 생각보다 피곤해서 일찍 잘 것 같아요.");
        //STT 설정 코드
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");


        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener); //STT 리스너

        thread.setDaemon(true);
        thread.start();

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        super.onStartCommand(intent, flags, startId);

/*        if(mRecognizer != null){
            mRecognizer.destroy();
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(i);
        }else{
            mRecognizer.startListening(i);
        }*/


        return  START_STICKY;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();




        if(windowManager != null) {
            if (rootView != null) {
                windowManager.removeView(rootView);
                rootView = null;
            }
        }
        mRecognizer.destroy();
        super.onDestroy();

    }




    private PhoneStateListener phoneStateListener = new PhoneStateListener()
    {
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if(state==TelephonyManager.CALL_STATE_IDLE) { //통화가 끝나면
                dataResult= (String) tv.getText().toString();

                new HttpAsyncTask().execute("http://34.223.211.250:3000/analyze");

                stopSelf();
            }
        };
    };
    private RecognitionListener listener = new RecognitionListener() {

        @Override
        public void onRmsChanged(float rmsdB) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onResults(Bundle results) {
            Log.d("***","onResults");
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            System.out.print("@@@@@@@@@@@@@@@ 마이크 입력" + rs[0]);
            tv.setText(tv.getText() + rs[0]);
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(int error) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub

        }
    };

    class BackThread extends Thread{
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                // 메인에서 생성된 Handler 객체의 sendEmpryMessage 를 통해 Message 전달
                handler.sendEmptyMessage(0);

                try {
                    thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("Thread is dead!!!");
                } finally {

                }
            }

        } // end run()

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                mRecognizer.startListening(i); // stt 시작
            }
        }
    };


    public static String POST(String url, CallData callData){
        InputStream is = null;
        String result = "";
        try {

            // 1. create HttpClient
            //HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            //HttpPost httpPost = new HttpPost(url);
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("number", callData.getNumber());
            jsonObject.accumulate("name", callData.getName());
            jsonObject.accumulate("data", callData.getData());
            jsonObject.accumulate("opponentNumber", callData.getOpponentNumber());
            jsonObject.accumulate("time", callData.getTime());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);
            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("utf-8"));
            os.flush();
            ///응답받는 코드
            int responseCode=httpCon.getResponseCode();
            if(responseCode==200){
                Log.d("*******응답", "받음!");
            }
            ///
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if(is != null){
                    result = convertInputStreamToString(is);
                    Log.d("***********result!",result);
                }
                else
                    result = "Did not work!";
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                httpCon.disconnect();
            }


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            System.out.println("@@@@@@@@@@@@@@@@@doInBackground : Service");

            // 현재 자신의 전화번호 불러오기
            TelephonyManager telManager = (TelephonyManager) getSystemService( Context.TELEPHONY_SERVICE);
            String phoneNum = telManager.getLine1Number();
            if(phoneNum.startsWith("+82")){
                phoneNum = phoneNum.replace("+82", "0");
            }
            //callData 세팅
            callData.setNumber(phoneNum);
            callData.setName("주혜");
            System.out.println("@@@@@@@@@@@@@@@@@dataResult : " + dataResult);
            System.out.println("@@@@@@@@@@@@@@@@@opponentNumber : " + opponentNumber);
            callData.setData(dataResult);
            callData.setOpponentNumber(opponentNumber);
            callData.setTime(startTime);
            Log.d("*******sent data",phoneNum+", 주혜, "+dataResult+","+opponentNumber+","+startTime);
            return POST(urls[0],callData);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            thread.interrupted(); //스레드 죽어라!
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();

            startResultActivity();


        }
    }
    void startResultActivity(){
        Intent intent = new Intent(
                this,
                CallSelectTabActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        Log.d("***********", "ResultActivity 시작");
        //this.finish();
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}