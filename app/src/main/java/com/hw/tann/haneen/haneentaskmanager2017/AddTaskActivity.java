package com.hw.tann.haneen.haneentaskmanager2017;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class AddTaskActivity extends AppCompatActivity {

    private ImageButton ibtnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ibtnMap = (ImageButton) findViewById(R.id.ibtnMap);

    }
}
