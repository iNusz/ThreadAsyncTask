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


## ProgressDialog


ProgressDialog를 만들어 보자

<br/>




###### 프로그래스 다이얼로그를 정의한다.




###### setTitle , setMessage , setProgressStyle 는 필수요소이다.




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



###### run()에서 show()로 창을 호출해준다



```java
private void run() {
    progress.show();
}
```



###### setDone() --> 프로그래스가 끝나는부분에서 dismiss();로 창을 해제한다



```java
private void setDone() {
      progress.dismiss();
}
```
