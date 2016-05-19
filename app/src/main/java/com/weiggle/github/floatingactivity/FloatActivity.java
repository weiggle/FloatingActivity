package com.weiggle.github.floatingactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FloatActivity extends AppCompatActivity {

    private TextView mTextView;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

        String data = getIntent().getStringExtra("data");
        mTextView = (TextView) findViewById(R.id.txt);
        mTextView.setText(data);

        mListView = (ListView) findViewById(R.id.lv);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mListView.getLayoutParams();
        params.height =(int)(getResources().getDisplayMetrics().heightPixels*2/5);

        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fillDatas());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String resultData = mAdapter.getItem(position);
                Intent intent = new Intent(FloatActivity.this,MainActivity.class);
                intent.putExtra("result",resultData);
                setResult(RESULT_OK,intent);
                onBackPressed();
            }
        });

    }


    private List<String> fillDatas(){
        List<String> list = new ArrayList<>();
        for(int i = 0;i< 50;i++){
            list.add("this is item "+i);
        }
        return list;
    }
}
