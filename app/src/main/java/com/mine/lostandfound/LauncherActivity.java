package com.mine.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.mine.lostandfound.MainActivity;


public class LauncherActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_launcher);

        ((Button)findViewById(R.id.btn_create_advert))
            .setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class)));

        ((Button)findViewById(R.id.btn_show_all))
            .setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
    }
}
