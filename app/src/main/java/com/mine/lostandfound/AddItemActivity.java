package com.mine.lostandfound;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.mine.lostandfound.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddItemActivity extends AppCompatActivity {
    private RadioGroup rgType;
    private EditText   etName, etPhone, etDesc, etDate, etLocation;
    private Button     btnSave;
    private DBHelper   db;
    // date formatter
    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_add_item);

        rgType     = findViewById(R.id.rg_type);
        etName     = findViewById(R.id.et_name);
        etPhone    = findViewById(R.id.et_phone);
        etDesc     = findViewById(R.id.et_description);
        etDate     = findViewById(R.id.et_date);
        etLocation = findViewById(R.id.et_location);
        btnSave    = findViewById(R.id.btn_save);
        db         = new DBHelper(this);

        // 1) Auto-fill today’s date
        String today = fmt.format(new Date());
        etDate.setText(today);

        // 2) On click → show DatePicker
        etDate.setOnClickListener(v -> {
            // Initialize calendar to current date in the field
            Calendar cal = Calendar.getInstance();
            try {
                Date parsed = fmt.parse(etDate.getText().toString());
                if (parsed != null) cal.setTime(parsed);
            } catch (ParseException ignored) {}

            new DatePickerDialog(
                AddItemActivity.this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // month is 0-based
                    Calendar picked = Calendar.getInstance();
                    picked.set(year, month, dayOfMonth);
                    etDate.setText(fmt.format(picked.getTime()));
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnSave.setOnClickListener(v -> {
            String status = rgType.getCheckedRadioButtonId() == R.id.rb_lost
                ? "Lost" : "Found";
            boolean ok = db.insertItem(
                etName.getText().toString(),
                etDesc.getText().toString(),
                etDate.getText().toString(),
                etLocation.getText().toString(),
                etPhone.getText().toString(),
                status
            );
            Toast.makeText(this,
                ok ? "Item saved" : "Save failed",
                Toast.LENGTH_SHORT).show();
            if (ok) finish();
        });
    }
}
