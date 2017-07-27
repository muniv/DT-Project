package com.muni.examples.aibrilcall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static PopupMenu.OnMenuItemClickListener onMenuItemClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);//splash다음에 원래의 테마로 돌립니다

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        onMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() { // 팝업메뉴아이템 클릭리스너 생성
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch( item.getItemId() ){//눌러진 MenuItem의 Item Id를 얻어와 식별
                    case R.id.load_call: //통화 불러오기
                        //Toast.makeText(MainActivity.this, "통화 불러오기", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(
                                getApplicationContext(), // 현재 화면의 제어권자
                                CallList.class); // 다음 넘어갈 클래스 지정
                        startActivity(intent); // 다음 화면으로 넘어간다

                        break;
                    case R.id.exit: //앱 종료
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                        break;
                }
                return false;
            }
        };

        //플로팅버튼
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp(view); //플로팅 버튼을 클릭하면 팝업메뉴가 나타나게 합니다.
            }
        });
    }

    public final void popUp(View view) {
        PopupMenu popup = new PopupMenu(this, view); // 인자 (context, 팝업메뉴 연결 anchor 뷰)
        getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu()); // 메뉴아이템 건져서 메뉴 inflate
        popup.setOnMenuItemClickListener(onMenuItemClickListener); // onCreate에서 생성한 리스너를 팝업메뉴에 셋팅
        // popup.show(view); //Popup Menu 보이기
        popup.show(); //Popup Menu 보이기
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
