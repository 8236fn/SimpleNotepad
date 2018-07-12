package com.blogspot.tiaotone.simplenotepad;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_date;
    private EditText et_content;
    private Button btn_ok;
    private Button btn_cancel;
    private NoteDateBaseHelper dbHelper;
    public int enter_state = 0;
    public String last_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
    }
    private void initView(){
        tv_date = (TextView)findViewById(R.id.tv_date);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        dbHelper = new NoteDateBaseHelper(this);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = sdf.format(date);
        tv_date.setText(dateString);

        Bundle myBundle = this.getIntent().getExtras();
        last_content = myBundle.getString("info");
        enter_state = myBundle.getInt("enter_state");
        et_content.setText(last_content);

        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_ok:
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String content = et_content.getText().toString();
                if (enter_state == 0) {
                    if (!content.equals("")) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String dateString = sdf.format(date);
                        ContentValues values = new ContentValues();
                        values.put("content", content);
                        values.put("date", dateString);
                        db.insert("note", null, values);
                        finish();
                    } else {
                        Toast.makeText(EditActivity.this, "輸入內容", Toast.LENGTH_LONG).show();
                    }

                } else {
                    ContentValues values = new ContentValues();
                    values.put("content", content);
                    db.update("note", values, "content=?", new String[]{last_content});
                    finish();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
