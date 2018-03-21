package cn.nieking.materialdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.rey.material.widget.ProgressView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ProgressView pv;

    private final MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> mainActivityWeakReference;

        MyHandler(MainActivity activity) {
            mainActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mainActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.pv.stop();
                        break;
                }
            }
        }
    }

    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pv = findViewById(R.id.pv);
        pv.start();
        run.run();
    }
}
