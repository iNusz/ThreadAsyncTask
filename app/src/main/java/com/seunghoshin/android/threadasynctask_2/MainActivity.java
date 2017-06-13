package com.seunghoshin.android.threadasynctask_2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
                runAsync();
            }
        });
        // 화면에 진행상태를 표시
        // 프로그래스 다이얼로그 정의
        // 여기서 this 대신에 getBaseContext()를 못쓰는 이유는 context에는 context만 들어가있다
        // this에는 캐스팅을 통해서 context에서 하위로 다운캐스팅이 된다 . 최상위 context를 넘겨주면 하위로 다시 캐스팅이 되지 않는다(context 리소스만 가지고있다)
        // 다이얼로그는 Theme를 쓰는데 context뿐만이 아니라 activity에 style요소를 가지고 있기 때문에 this를 써야한다


    }


    private void setDone() {
        // todo 이렇게 되면 setDone메소드가 구지 있어야 되는 생각이 .. 차라리 onPostExecute에 넣어주면 어떨까..  -> 넣어줘도된다
        //textView.setText("Done!!!");
        // 프로그래스 창을 해제
        // progress.dismiss();
    }

    // 사용할때는 Task가 한정적일때 (1~10초)에서 끝난다거나 , 10가지일만 처리하고 끝난다거나 , 루틴이 정해져있을때 쓴다. (채팅같은건 당연히안씀)
    // 주로 네트워크 통해서 데이터 가져오거나 목록을 뿌려줄때 사용한다
    private void runAsync() {

        new AsyncTask<String, Integer, Float>() {

            // 제네릭 타입   1 - doInBackground 의 인자
            //            2 - onProgressUpdate 의 인자
            //            3 - doInBackground 의 리턴타입

            ProgressDialog progress;


            // doInBackground 가 호출되기 전에 먼저 호출된다   , 메인쓰레드에서 실행
            @Override
            protected void onPreExecute() {
                progress = new ProgressDialog(MainActivity.this);
                progress.setTitle("진행중...");
                progress.setMessage("진행중 표시되는 메세지입니다");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
            }

            // 서브쓰레드에서 실행
            @Override
            // String ... params  <-- ...은 크기를 정의되지않은 params 배열이라고 생각하면 됨
            protected Float doInBackground(String... params) { // Thread에서 run과 같은 역활을 하는데 데이터를 처리하는 역활을 해줌 ...
                // 10초 후에 Done 이 뜨게끔 한다 .
                try {
                    for(int i =0; i<10; i++) {
                        // onProgressUpdate 를 주기적으로 업데이트 해준다.
                        publishProgress(i*10);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 날씨를 예를 들경우 위의 서브쓰레드에서 맑음이라는 데이터를 가져온다음에 return으로 맑음의 결과값을 onPostExecute(메인)으로 넘겨줄수 있다
                return 1004.4f; // 이때 onPostExecute 가 실행된다  , 여기에 넘길 값을 쓰면 된다
            }


            // doInBackground 가 처리되고 나서 호출된다   , 메인쓰레드에서 실행
            @Override
            protected void onPostExecute(Float aVoid) {  // 위의 서브스레드의 처리가 끝나고 나서 MainThread로 값을 넘겨줄수 있다는 얘기임
                // 결과값을 메인 UI에 세팅하는 로직을 여기에 작성한다
                textView.setText("Done!!!");
                progress.dismiss();
            }

            // 주기적으로 doInBackground 에서 호출이 가능한 함수 , 진행률을 나타낼수 있다   todo onProgressUpdate = 메인쓰레드 ? 서브쓰레드 ?  ->서브쓰레드
            @Override
            protected void onProgressUpdate(Integer... values) {
                // todo values[0]의 동작원리 , values를 넣으면 왜 주소값이 나오는지 .. -> 배열이 주소값을 참조하기 때문에 나오고 위에 publishProgress 메소드 참고
                progress.setMessage("진행율 = " +values[0]+ "%");
            }


            //Thread에서 start과 같은 역활을 한다   , execute에서는 메인쓰레드에서 -> 서브쓰레드 (doInBackground) 로 값을 넘겨준다 , 서로값을넘겨줄수 있다
        }.execute("안녕", "하세요");  // doInBackground 를 실행(그전에 pre가실행) , 이렇게 출력해주면 순서대로 배열처럼 꺼내 쓸 수 있다(같은타입만가능)
    }


//    일반 쓰레드

//    private void runThread() {
//        // 프로그래스 창을 호출
//        progress.show();
//        CustomThread thread = new CustomThread(handler);
//        thread.start();
//    }
}


class CustomThread extends Thread {
    Handler handler;

    public CustomThread(Handler handler) {
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