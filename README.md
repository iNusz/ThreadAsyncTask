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
