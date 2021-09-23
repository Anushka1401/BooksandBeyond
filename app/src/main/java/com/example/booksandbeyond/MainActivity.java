package com.example.booksandbeyond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        signOut=findViewById(R.id.sign_out);


    }


    public void onNext(View view){
        Intent intent=new Intent(MainActivity.this, FavoriteList.class);
        startActivity(intent);
    }

    public void onsignout(View view) {

        Intent intent=new Intent(MainActivity.this, SignoutActivity.class);
        startActivity(intent);

    }
}