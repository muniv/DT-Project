package com.muni.examples.aibrilcall;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {
    CallData callData = STTActivity.callData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        new ResultActivity.HttpAsyncTask().execute("http://34.223.211.250:3000/phone/"+callData.getNumber()+"/"+callData.getOpponentNumber()+"/"+callData.getTime());
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private String Content;
        private String Error = null;

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
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("emotion");

                    // 각각의 JSON Node를 처리/

                    int lengthJsonArr = jsonMainNode.length();//"registered_sites 로 시작하는 서브노드배열의 갯수를 구함

                    // listview.setAdapter(adapter) ;
                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        /****** JSON node에서 데이터를 얻어옴***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* 노드 밸류에서 값을 얻어옴**********/
                        String sadness       = jsonChildNode.optString("sadness").toString();
                        String joy     = jsonChildNode.optString("joy").toString();
                        String fear     = jsonChildNode.optString("fear").toString();
                        String disgust     = jsonChildNode.optString("disgust").toString();
                        String anger     = jsonChildNode.optString("anger").toString();

                        Log.d("*************sadness",sadness);
                        Log.d("*************joy",joy);
                        Log.d("*************fear",fear);
                        Log.d("*************disgust",disgust);
                        Log.d("*************anger",anger);

                    }
                    /****************** JSON Data parsing 완료 *************/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
