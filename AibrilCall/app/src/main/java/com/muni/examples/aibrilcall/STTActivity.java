package com.muni.examples.aibrilcall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class STTActivity extends AppCompatActivity {
    Intent i; //STT 인텐트
    SpeechRecognizer mRecognizer; //구글 STT SpeechRecognizer
    TextView tv; //STT결과를 보여주는 TextView
    final STTActivity.BackThread thread = new STTActivity.BackThread();//STT 스레드
    String dataResult="";
    String endTime="";
    static String opponentNumber;
    static CallData callData = new CallData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stt);


        tv=(TextView) findViewById(R.id.tv);
        tv.setText("");
        //tv.setText("여보세요. 구혜선씨 맞으십니까? 네 저 안재현입니다! 수근이가 구혜선씨를 소개해주어서 구혜선씨에게 연락하게 되었습니다! 수근이가 저를 잘 소개해줬으면 좋다! 재밌으시네요! 혹시 이번 주말에 시간되시면 만나서 밥도 먹고 영화도 보고 카페도 갑시다? 평일 좋다 혹시 저녁에 시간 됩니까? 집이 어디십니까? 오 저는 도서관 근처에 살고 있습니다! 그럼 혹시 토요일날 중간인 분수대에서 보실까요? 그곳에 먹을 곳도 많고 영화관도 있다! 알겠습니다. 혹시 먹는 것 중에 좋아하시는 것 있으십니까? 소고기 저도 좋아하는데 잘됐습니다! 그럼 저녁에 소고기 먹으러 가시는 것 좋으십니까? 혹시 영화는 어떤 것 좋아하십니까? 찾아보니까 이번 주 토요일에 괴물 개봉하더라고요. 괴물 영화 좋아하십니까? 네 에나멜 봅시다. 좋습니다 ");
        tv.setText(": 네! 안재현씨 맞으십니까? 네 저도 수근이한테 연락 받았습니다! 수근이가 안재현씨 싫다 합니다. 주말은 싫다. 평일에 보다. 아니 저는 점심만 가능합니다. 기차역 주변에 살고 있습니다! 그냥 기차역 쪽에서 봅시다 불필요하고 귀찮게 멀리 나가기 싫다. 소고기 괜찮습니다. 네! 가끔 맛없는 소고기 집이 있는데 제 집 주변으로 와주시니 맛있는데 알려드리겠습니다! 괴물 예고편을 보니까 시시해서 별로 재미없습니다. 에나멜 봅시다. 네! 그럼 평일에 뵙시다! 전화 끊겠습니다. ");
        //STT 설정 코드
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener); //STT 리스너



        // STT 스레드 생성하고 시작
        thread.setDaemon(true);
        thread.start();
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    private PhoneStateListener phoneStateListener = new PhoneStateListener()
    {
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if(state==TelephonyManager.CALL_STATE_IDLE){ //통화가 끝나면

                dataResult= (String) tv.getText();

                // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();
                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmm");
                // nowDate 변수에 값을 저장한다.
                endTime = sdfNow.format(date);

                new STTActivity.HttpAsyncTask().execute("http://34.223.211.250:3000/analyze");
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
            tv.setText(tv.getText()+rs[0]);
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

    } // end class BackThread
    // '메인스레드' 에서 Handler 객체를 생성한다.
    // Handler 객체를 생성한 스레드 만이 다른 스레드가 전송하는 Message나 Runnable 객체를
    // 수신할수 있다.
    // 아래 생성된 Handler 객체는 handlerMessage() 를 오버라이딩 하여
    // Message 를 수진합니다.
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
                Log.d("*******응답","받음!");
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


            // 현재 자신의 전화번호 불러오기
            TelephonyManager telManager = (TelephonyManager) getSystemService( Context.TELEPHONY_SERVICE);
            String phoneNum = telManager.getLine1Number();
            if(phoneNum.startsWith("+82")){
                phoneNum = phoneNum.replace("+82", "0");
            }
            //callData 세팅
            callData.setNumber(phoneNum);
            callData.setData(dataResult);
            callData.setOpponentNumber(opponentNumber);
            callData.setTime(endTime);
            Log.d("*******sent data",phoneNum+","+dataResult+","+opponentNumber+","+endTime);
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
                getApplicationContext(),
                CallSelectTabActivity.class);
        startActivity(intent);
        Log.d("***********","ResultActivity 시작");
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
