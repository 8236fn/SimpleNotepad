package com.blogspot.tiaotone.simplenotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private ListView listview;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> dataList;
    private Button addNote;
    private TextView tv_content;
    private NoteDateBaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RefreshNotesList();
    }

    private void RefreshNotesList() {
        int size = dataList.size();
        if(size>0){
            dataList.removeAll(dataList);
            simpleAdapter.notifyDataSetChanged();
        }
        Cursor cursor = db.query("note",null,null,null,null,null,null);
        startManagingCursor(cursor);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", name);
            map.put("tv_date", date);
            dataList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,dataList, R.layout.item_view, new String[]{"tv_content","tv_date"},new int[]{R.id.tv_content,R.id.tv_date});
        listview.setAdapter(simpleAdapter);
    }

    private void initView(){
        tv_content = (TextView)findViewById(R.id.tv_content);
        listview = (ListView)findViewById(R.id.listview);
        dataList = new ArrayList<Map<String,Object>>();
        addNote = (Button) findViewById(R.id.btn_editnote);
        dbHelper = new NoteDateBaseHelper(this);
        db = dbHelper.getReadableDatabase();

        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("info","");
                bundle.putInt("enter_state", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("刪除記事");
        builder.setMessage("確定刪除嗎？");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = listview.getItemAtPosition(position) + "";
                String content1 = content.substring(content.indexOf("=")+1,content.indexOf(","));
                db.delete("note", "content = ?", new String[]{content1});
                RefreshNotesList();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}
