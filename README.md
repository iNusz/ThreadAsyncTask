# ThreadAsyncTask


## Handler

handler 을 이용해보자


```java
// 상수를 지정해줌으로써 case문을 원할하게 쓰게 만든다
public static final int SET_DONE = 1;

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
```


```java
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
```


<br/>



## ProgressDialog



ProgressDialog를 만들어 보자



<br/>




#### 프로그래스 다이얼로그를 정의한다.




setTitle , setMessage , setProgressStyle 는 필수요소이다.




```java
@Override
            public void onClick(View v) {
                run();
            }
        });
        // 화면에 진행상태를 표시
        // 여기서 this 대신에 getBaseContext()를 못쓰는 이유는 context에는 context만 들어가있다
        // this에는 캐스팅을 통해서 context에서 하위로 다운캐스팅이 된다 . 최상위 context를 넘겨주면 하위로 다시 캐스팅이 되지 않는다(context 리소스만 가지고있다)
        // 다이얼로그는 Theme를 쓰는데 context뿐만이 아니라 activity에 style요소를 가지고 있기 때문에 this를 써야한다
        progress = new ProgressDialog(this);
        progress.setTitle("진행중...");
        progress.setMessage("진행중 표시되는 메세지입니다");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
```



#### run()

run()에서 show()로 창을 호출해준다



```java
private void run() {
    progress.show();
}
```



#### setDone()



프로그래스가 끝나는부분에서 dismiss();로 창을 해제한다




```java
private void setDone() {
      progress.dismiss();
}
```



<br/>




## AsyncTask




AsyncTask는 Task가 한정적일때(1~10초 에서 끝난다거나), 10가지일만 처리하고 끝나거나, 루틴이 정해져있을때 사용한다(채팅앱x)



주로 네트워크를 통해서 데이터를 가져오거나 목록을 뿌려줄때 사용한다.




<br/>


#### 일반적인 형태


execute는 Thread에서 start과 같은 역활을 한다.

execute에서는 메인쓰레드에서 -> 서브쓰레드 (doInBackground) 로 값을 넘겨준다. 서로 값을 넘겨줄수 있다


이때 execute는 doInBackground 를 실행시켜주는데(그전에 onPreExecute가 실행) 괄호 안에 값을 ,로 구분해서 입력하면 배열처럼 순차적으로 출력이 되어서 값을 넘겨줄 수 있다


```java
private void runAsync() {

    new AsyncTask<String, Integer, Float>() {
            // 제네릭 타입   1 - doInBackground 의 인자
            //            2 - onProgressUpdate 의 인자
            //            3 - doInBackground 의 리턴타입


    }.execute("안녕", "하세요");
}
```




#### onPreExecute



doInBackground 가 호출되기 전에 먼저 호출된다. 메인쓰레드에서 실행된다.




```java
          @Override
          protected void onPreExecute() {
              progress = new ProgressDialog(MainActivity.this);
              progress.setTitle("진행중...");
              progress.setMessage("진행중 표시되는 메세지입니다");
              progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
              progress.show();
          }
```




#### doInBackground




Thread에서 run과 같은 역활을 하는데 데이터를 처리하는 역활을 해준다. 서브쓰레드에서 실행된다.



여기서 return값을 넘겨줄 경우에 onPostExecute에 값이 넘어가면서 실행이 되는데, 이를 활용하는 방법으로는




날씨를 예를 들경우 위의 서브쓰레드(onPreExecute)에서 "맑음"이라는 데이터를 가져온다음에 return으로 "맑음"의 결과값을 onPostExecute(메인)으로 넘겨줄수 있다.




```java
            @Override
            // String ... params  <-- ...은 크기를 정의되지않은 params 배열이라고 생각하면 됨
            protected Float doInBackground(String... params) {
                // 10초 후에 Done 이 뜨게끔 한다 .
                try {
                    for(int i =0; i<10; i++) {
                        // onProgressUpdate 를 주기적으로 업데이트 해주는 메소드
                        publishProgress(i*10);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1004.4f; // 이때 onPostExecute 가 실행된다  , 여기에 넘길 값을 쓰면 된다
            }
```



#### onPostExecute



doInBackground 가 처리되고 나서 호출된다. 메인쓰레드에서 실행된다.



이 메소드는 위의 서브스레드(doInBackground)의 처리가 끝나고 나서 MainThread로 값을 넘겨줄수 있다는 얘기이다.




```java
            @Override
            protected void onPostExecute(Float aVoid) {
                // 결과값을 메인 UI에 세팅하는 로직을 여기에 작성한다
                setDone();
                progress.dismiss();
            }
```



#### onProgressUpdate



주기적으로 doInBackground 에서 호출이 가능한 메소드이다. 진행률을 나타낼수 있다.



```java
            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setMessage("진행율 = " +values[0]+ "%");
            }
```





## Android Emulator


handler를 이용한 앱 실행, ProgressDialog 활용 , AsyncTask를 이용한 ProgressDialog 활용



![Async.jpg]()
