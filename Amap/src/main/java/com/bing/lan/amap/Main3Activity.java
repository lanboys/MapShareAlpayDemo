package com.bing.lan.amap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class Main3Activity extends AppCompatActivity implements TextWatcher, AdapterView.OnItemClickListener {

    private static final String TAG = "MainAty";
    private EditText edit;
    private ListView lv;
    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
    }

    private void initView() {
        edit = (EditText) findViewById(R.id.search_edit);
        lv = (ListView) findViewById(R.id.search_list);
        edit.addTextChangedListener(this);
        mAdapter = new SearchAdapter(this);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);

        View viewById = findViewById(R.id.fab);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String trim = edit.getText().toString().trim();

                InputTask.getInstance(Main3Activity.this, mAdapter  ).onSearch(trim.toString(), "");
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {

        AddressBean item = (AddressBean) mAdapter.getItem(position);

        //Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();

        Intent date = new Intent();
        date.putExtra("addressInfo", item);

        setResult(0, date);

        finish();
        //Item点击事件处理
    }
}