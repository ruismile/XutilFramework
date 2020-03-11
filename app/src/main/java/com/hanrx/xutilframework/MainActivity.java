package com.hanrx.xutilframework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hanrx.xutilframework.annotion.ContentView;
import com.hanrx.xutilframework.annotion.OnClick;
import com.hanrx.xutilframework.annotion.OnLongClick;
import com.hanrx.xutilframework.annotion.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final String TAG = "hanrx";
    /*@ViewInject(R.id.text)
    TextView textView;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //Log.i(TAG, "-------->" + textView);
    }

    @OnClick(R.id.text)
    public void onClick(View view) {
        Toast.makeText(this,"单击",Toast.LENGTH_SHORT).show();
    }
    @OnLongClick({R.id.text2})
    public  boolean click(View view) {
        Toast.makeText(this,"长按",Toast.LENGTH_SHORT).show();
        return true;
    }

}
