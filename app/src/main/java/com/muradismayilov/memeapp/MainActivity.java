package com.muradismayilov.memeapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private static final int RESULT_CODE = 4;

    ImageView mImage;

    TextView topTV;
    TextView bottomTV;
    EditText topET;
    EditText bottomET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = findViewById(R.id.mImage);

        topTV = findViewById(R.id.topTV);
        bottomTV = findViewById(R.id.bottomTV);
        topET = findViewById(R.id.topET);
        bottomET = findViewById(R.id.bottomET);
    }

    public void addImage(View view){

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_CODE && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();

            int columnndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnndex);
            cursor.close();

            mImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

    public void tryText(View view){

        topTV.setText(topET.getText().toString());
        bottomTV.setText(bottomET.getText().toString());

        hideKeyboard(view);
    }

    public void hideKeyboard(View view){

        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    public void Save(View view){

        View area = findViewById(R.id.relativeLayout);
        Bitmap bitmap = getScreenshot(area);
        String currentImage = "image" + System.currentTimeMillis() + ".png";
        store(bitmap,currentImage);
    }

    public static Bitmap getScreenshot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bitmap, String filename){
        String dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File dir = new File(dirpath);

        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(dirpath,filename);

        try {
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
