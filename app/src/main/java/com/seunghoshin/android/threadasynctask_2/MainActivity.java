package com.seunghoshin.android.threadasynctask_2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SET_DONE = 1;
    TextView textView;

    // thread에서 호출하기 위한 handler
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_DONE:
                    setDone();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });
    }

    private void setDone() {
        textView.setText("Done!!!");
    }

    private void run() {
        CustomThread thread = new CustomThread(handler);
        thread.start();
    }
}


class CustomThread extends Thread {
    Handler handler;

    public CustomThread(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        // 10초 후에 Done 이 뜨게끔 한다 .
        try {
            Thread.sleep(10000);
            // Main UI 에 현재 Thread 가 접근할 수 없으므로
            // handler 를 통해 호출해준다
            handler.sendEmptyMessage(MainActivity.SET_DONE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}