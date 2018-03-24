package cn.yan.androidhippo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import cn.yan.androidhippo.activity.HippoActivity;
import cn.yan.androidhippo.broadcast.SyncBroadcastReceiver;

/**
 * Demo for Hippo use.
 */
public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        findViewById(R.id.activty_state_test).setOnClickListener(this);
        findViewById(R.id.sync_bc_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activty_state_test:
                startActivity(new Intent(this, HippoActivity.class));
                break;
            case R.id.sync_bc_test:
                sendBroadcast(new Intent(SyncBroadcastReceiver.ACTION_TEST));
                break;
        }
    }
}
