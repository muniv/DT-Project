package com.muni.examples.aibrilcall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview ;
        ListViewAdapter adapter;

        // 권한체크 시작
        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("Aibril Call을 사용하기 위해서 \n 하단 확인 버튼을 누르면 통화 권한 설정을 시작합니다.")
                .setDeniedMessage("권한을 설정하지 않을 경우 사용이 불가능하며, \n[설정] > [권한] 에서 권한 재설정이 가능합니다.")
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.RECORD_AUDIO)
                .check();

        // Adapter 생성
        adapter = new ListViewAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview_users);
        listview.setAdapter(adapter);

        String serverURL = "http://34.223.211.250:3000/users";
        new MainActivity.HttpAsyncTask().execute(serverURL);

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    PermissionListener permissionListener = new PermissionListener()
    {
        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };

    private PhoneStateListener phoneStateListener = new PhoneStateListener()
    {
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if(state==TelephonyManager.CALL_STATE_OFFHOOK){//통화시작하면 STTActivity 시작
                Intent stt_activity_intent = new Intent(
                        getApplicationContext(),
                        STTActivity.class);
                startActivity(stt_activity_intent);
            }
        };
    };

    //목록 가져오기
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private String Content;
        private String Error = null;
        ListView listview = (ListView) findViewById(R.id.listview_users);
        ListViewAdapter adapter= (ListViewAdapter) listview.getAdapter();
        @Override
        protected String doInBackground(String... urls) {
            try {

                URL urlCon = new URL(urls[0]);
                HttpURLConnection httpCon = (HttpURLConnection)urlCon.openConnection();

                // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
                httpCon.setDoInput(true);
                httpCon.setRequestMethod("GET");
                ///응답받는 코드
                int responseCode=httpCon.getResponseCode();
                BufferedReader br;
                if(responseCode==200){
                    br=new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while((line = br.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    br.close();

                    Content = response.toString();
                    Log.d("******결과값",Content);
                }else{
                    Log.d("*******응답코드","error!");
                }

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (Error != null) {
                //uiUpdate.setText("Output : "+Error);
            } else {
                // 서버 응답변수 reader 에서 내용을 라인단위 문자열 변수 Content 를 화면 UI uiUpdate=output객체에 뿌려줌.
                //uiUpdate.setText( Content );
                /****************** JSON Data 파싱 *************/
                JSONObject jsonResponse;
                try {
                    /****** 문자열 Content변수내용을 JSON Object로 생성 ********/
                    jsonResponse = new JSONObject(Content);

                    // JSONArray에서 항목 이름으로 결과 값 조회
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("users");

                    // 각각의 JSON Node를 처리/

                    int lengthJsonArr = jsonMainNode.length();//서브노드배열의 갯수를 구함

                    // listview.setAdapter(adapter) ;
                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** JSON node에서 데이터를 얻어옴***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* 노드 밸류에서 값을 얻어옴**********/
                        String name       = jsonChildNode.optString("name").toString();
                        String phone     = jsonChildNode.optString("phone").toString();

                        adapter.addItem(name,phone);
                        adapter.notifyDataSetChanged();


                    }
                    /****************** JSON Data parsing 완료 *************/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
