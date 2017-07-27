package com.muni.examples.aibrilcall;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class CallList extends AppCompatActivity {
    private ListView listView;
    public static ArrayList<CallDto> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        getCallList(); // 디바이스 안에 있는 오디오 파일 리스트를 조회하여 LIst를 만듭니다.
        listView = (ListView)findViewById(R.id.listview);
        MyAdapter adapter = new MyAdapter(this,list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CallList.this,CallActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("playlist",list);
                startActivity(intent);
            }
        });
    }


    public  void getCallList(){
        list = new ArrayList<>();
        //가져오고 싶은 컬럼 명을 나열합니다. 오디오의 아이디, 제목 정보를 가져옵니다.
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE
        };

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while(cursor.moveToNext()){
            CallDto callDto = new CallDto();
            callDto.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            callDto.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            list.add(callDto);
        }
        cursor.close();
    }



}

