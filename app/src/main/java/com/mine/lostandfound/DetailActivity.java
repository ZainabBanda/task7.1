package com.mine.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private DBHelper db;
    private Item     item;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_detail);

        db   = new DBHelper(this);
        int id = getIntent().getIntExtra("item_id", -1);
        item = db.getItemById(id);

        TextView tvName   = findViewById(R.id.tv_name);
        TextView tvDate   = findViewById(R.id.tv_date);
        TextView tvLoc    = findViewById(R.id.tv_location);
        TextView tvPhone  = findViewById(R.id.tv_phone);
        TextView tvStatus = findViewById(R.id.tv_status);
        Button   btnRemove= findViewById(R.id.btn_remove);

        if (item != null) {
            tvName.setText(item.getName());
            tvDate.setText("Date: "   + item.getDateReported());
            tvLoc .setText("At: "     + item.getLocation());
            tvPhone.setText("Phone: " + item.getPhone());
            tvStatus.setText("Status: "+ item.getStatus());
        }

        btnRemove.setOnClickListener(v -> {
            db.removeItem(id);
            Intent result = new Intent();
            result.putExtra("deleted_id", id);
            setResult(RESULT_OK, result);
            finish();
        });

    }
}
