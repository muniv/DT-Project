package com.muni.examples.aibrilcall;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends Activity {
    CallData callData = STTActivity.callData;
    private TextView tvPrintContents; // 내용이 출력되는 TextView.
    private String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        new JsonLoadingTask().execute();
        //new ResultActivity.HttpAsyncTask().execute("http://34.223.211.250:3000/phone/"+callData.getNumber()+"/"+callData.getOpponentNumber()+"/"+callData.getTime());
        //new ResultActivity.HttpAsyncTask().execute("http://34.223.211.250:3000/phone/01077422367/01075079691/20170814111121");
    }
    private class JsonLoadingTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strs)
        {
            String var = getJsonText();
            return var;
        } // doInBackground : 백그라운드 작업을 진행한다.

        @Override
        protected void onPostExecute(String result)
        {
            tvPrintContents.setText(result);
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask

    public String getJsonText()
    {
        StringBuffer sb = new StringBuffer();

        try
        {
            //주어진 URL 문서의 내용을 문자열로 얻는다.
            //String jsonPage = getStringFromUrl("http://34.223.211.250:3000/phone/"+callData.getNumber()+"/"+callData.getOpponentNumber()+"/"+callData.getTime());
            String jsonPage = getStringFromUrl("http://34.223.211.250:3000/phone/01077422367/01075079691/20170814111121");

            JSONObject jsonObject = new JSONObject(jsonPage);
            JSONObject jObj = jsonObject.optJSONObject("me");
            String jUnderObj = jObj.getString("emotion");

            JSONObject emotion = new JSONObject(jUnderObj);
            String sadness = emotion.getString("sadness");
            String fear = emotion.getString("fear");
            String joy = emotion.getString("joy");
            String disgust = emotion.getString("disgust");
            String anger = emotion.getString("anger");

            System.out.println("!!!!!!!!!!!!!!!!!!!" + sadness);
            System.out.println("!!!!!!!!!!!!!!!!!!!" + fear);
            System.out.println("!!!!!!!!!!!!!!!!!!!" + joy);
            System.out.println("!!!!!!!!!!!!!!!!!!!" + disgust);
            System.out.println("!!!!!!!!!!!!!!!!!!!" + anger);


            //JSONArray jsonArray = jObj.getJSONArray("sadness");

            /*for(int i = 0; i < jsonArray.length(); i++)
            {
                jObj = jsonArray.getJSONObject(i);

                String var = jObj.toString();
            }*/
        }

        catch (Exception e)
        {
            // TODO: handle exception
        }

        return sb.toString();
    }//getJsonText()-----------

    public String getStringFromUrl(String pUrl)
    {
        BufferedReader bufreader=null;
        HttpURLConnection urlConnection = null;

        StringBuffer page=new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성

        try
        {
            URL url= new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream,"UTF-8"));
            String line = null;

            while((line = bufreader.readLine())!=null)
            {
                page.append(line);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        finally
        {
            try
            {
                bufreader.close();
                urlConnection.disconnect();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return page.toString();
    }
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
                    //jsonResponse = new JSONObject(Content);

                    JSONObject jsonObject = new JSONObject(Content);
                    JSONObject jObj = jsonObject.optJSONObject("me");
                    String jUnderObj = jObj.getString("emotion");

                    JSONObject emotion = new JSONObject(jUnderObj);
                    String sadness = emotion.getString("sadness");
                    String fear = emotion.getString("fear");
                    String joy = emotion.getString("joy");
                    String disgust = emotion.getString("disgust");
                    String anger = emotion.getString("anger");

                    System.out.println("!!!!!!!!!!!!!!!!!!!" + sadness);
                    System.out.println("!!!!!!!!!!!!!!!!!!!" + fear);
                    System.out.println("!!!!!!!!!!!!!!!!!!!" + joy);
                    System.out.println("!!!!!!!!!!!!!!!!!!!" + disgust);
                    System.out.println("!!!!!!!!!!!!!!!!!!!" + anger);
                    /****************** JSON Data parsing 완료 *************/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
