package com.github.bkhezry.demomapdrawingtools.cal.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;


import com.github.bkhezry.demomapdrawingtools.R;
import com.github.bkhezry.demomapdrawingtools.cal.utils.FileUtils;
import com.github.bkhezry.demomapdrawingtools.cal.utils.PermissionHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SampleActivity extends AppCompatActivity implements
        CsvPickerFragment.OnCsvFileSelectedListener {

    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
            if (PermissionHelper.checkOrRequest(SampleActivity.this, REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                createDemoFile();
        }
    }


    @Override
    public void onCsvFileSelected(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(fileName);
            if (file.exists() && fileName.endsWith(".csv")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, TableLayoutFragment.newInstance(fileName), CsvPickerFragment.class.getSimpleName())
                        .addToBackStack(CsvPickerFragment.class.getSimpleName())
                        .commit();
            } else {
                Toast.makeText(this, R.string.not_csv_file_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.no_such_file_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE_DEMO
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          createDemoFile();
        }
    }

    public void testmethod(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(fileName);
            if (file.exists() && fileName.endsWith(".csv")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, TableLayoutFragment.newInstance(fileName), CsvPickerFragment.class.getSimpleName())
                        .addToBackStack(CsvPickerFragment.class.getSimpleName())
                        .commit();
            } else {
                Toast.makeText(this, R.string.not_csv_file_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.no_such_file_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void createDemoFile() {

        File file = createDemoTempFile();
        try {
            if (!file.exists() && file.createNewFile()) {
                InputStream inputStream = getAssets().open("farmer.csv");
                FileUtils.copy(inputStream, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }

        String path = file == null ? "" : file.getPath();

        if (!TextUtils.isEmpty(path)) {
            testmethod(path);
        }
    }

    public File createDemoTempFile() {
        String tempFileName = "farmer.csv";
        return new File(Environment.getExternalStorageDirectory(), tempFileName);
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            this.finish();
        } else {
            this.finish();
        }
    }
}
