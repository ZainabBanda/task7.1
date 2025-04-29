package com.mine.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_DETAIL = 1001;

    private ItemAdapter  adapter;
    private DBHelper     db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        List<Item> items = db.getAllItems();

        adapter = new ItemAdapter(items, itemId -> {
            Intent i = new Intent(MainActivity.this, DetailActivity.class);
            i.putExtra("item_id", itemId);
            startActivityForResult(i, REQUEST_DETAIL);
        });

        RecyclerView rv = findViewById(R.id.rv_items);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        findViewById(R.id.btn_create_advert)
            .setOnClickListener(v ->
                startActivity(new Intent(this, AddItemActivity.class))
            );
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL
            && resultCode  == RESULT_OK
            && data != null) {

            int deletedId = data.getIntExtra("deleted_id", -1);
            if (deletedId != -1) {
                List<Item> list = adapter.getItemList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == deletedId) {
                        adapter.removeItemAt(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // in case you came back from AddItemActivity
        adapter.getItemList().clear();
        adapter.getItemList().addAll(db.getAllItems());
        adapter.notifyDataSetChanged();
    }
}
