package com.muni.examples.aibrilcall;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class CallList extends AppCompatActivity {
    public static MyAdapter adapter;
    private ListView listView;
    public static ArrayList<CallDto> list;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public boolean lock() {
        return isRunning.compareAndSet(false, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_call_list);
        getCallList(); // 디바이스 안에 있는 오디오 파일 리스트를 조회하여 LIst를 만듭니다.

        listView = (ListView)findViewById(R.id.listview);
        adapter = new MyAdapter(this,list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CallList.this, CallActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("playlist", list);
                startActivity(intent);


                String findFileName = list.get(position).getTitle() + ".flac";
                Log.d("File", "findFileName" + findFileName);

                //if (lock()) {
                new CallAudilUpload().execute(getFolderFilePath(getFolderPath(), findFileName));
                // }


            }
        });
    }


    public  void getCallList(){
        list = new ArrayList<>();
        //가져오고 싶은 컬럼 명을 나열합니다. 오디오의 아이디, 제목 정보를 가져옵니다.
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        String where = MediaStore.Audio.Media.DATA + " like ?";
        String whereArgs[] = {getFolderPath()+"%"};

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, where, whereArgs, null);

        while(cursor.moveToNext()){
            CallDto callDto = new CallDto();
            callDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            callDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            list.add(callDto);

            Log.d("File", "list file list: " + list.toString());
        }
        cursor.close();
    }


    public String getFolderPath() {

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "AudioRecorder");

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/");
    }

    public String getFolderFilePath(String directoryName, String findFileName){

        String path = directoryName + findFileName;
        Log.d("File", "Path: " + path);

        return  path;
    }

    public ArrayList<String> getFolderlist(){
        ArrayList<String> folderList = new ArrayList<String>();
        ContentResolver resolver = getContentResolver();
        String[] folderColumn = {
                "distinct replace("+MediaStore.Audio.Media.DATA+", "+ MediaStore.Audio.Media.DISPLAY_NAME+", '')"
        };
        Cursor folderListCursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, folderColumn, null, null, null);

        if(folderListCursor != null && folderListCursor.getCount() > 0) {
            folderListCursor.moveToFirst();
            while(!folderListCursor.isAfterLast()) {
                folderList.add(folderListCursor.getString(0));
                folderListCursor.moveToNext();
            }
        }
        Log.d("File", "folderList : "+folderList);

        return folderList;
    }



}

