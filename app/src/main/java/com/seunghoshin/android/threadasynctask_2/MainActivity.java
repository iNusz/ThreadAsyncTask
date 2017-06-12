package com.seunghoshin.android.threadasynctask_2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SET_DONE = 1;
    TextView textView;
    ProgressDialog progress;

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
        // 화면에 진행상태를 표시
        // 프로그래스 다이얼로그 정의
        // 여기서 this 대신에 getBaseContext()를 못쓰는 이유는 context에는 context만 들어가있다
        // this에는 캐스팅을 통해서 context에서 하위로 다운캐스팅이 된다 . 최상위 context를 넘겨주면 하위로 다시 캐스팅이 되지 않는다(context 리소스만 가지고있다)
        // 다이얼로그는 Theme를 쓰는데 context뿐만이 아니라 activity에 style요소를 가지고 있기 때문에 this를 써야한다

        progress = new ProgressDialog(this);
        progress.setTitle("진행중...");
        progress.setMessage("진행중 표시되는 메세지입니다");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }


    private void setDone() {
        textView.setText("Done!!!");
        // 프로그래스 창을 해제
        progress.dismiss();
    }

    private void run() {
        // 프로그래스 창을 호출
        progress.show();
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