package com.example.booksandbeyond;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booksandbeyond.ui.home.HomeFragment;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadingStory extends AppCompatActivity {
	
	 private Button mStart;
    private Button mPause;
    private Button mReset;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    TextView textView;
    String Talk=null;
    String SearchQueryTerm = null;
    private TextToSpeech mTTs;
    Point p;
    private PaginationController mController;
    private SeekBar mSeekBarPitch;
    private SeekBar mSeekBarSpeed;
    private Button mButtonSpeak;
    private Button button;
    ImageView imageView;
    Spinner spinner;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //ReadingStory layout

            setContentView(R.layout.activity_reading_story);
            Intent intent = getIntent();
            InputStream inputStream = null;
            imageView = (ImageView)findViewById(R.id.ImageDetail);
            textView = (TextView) findViewById(R.id.tView);
            mController = new PaginationController(textView);
			
			mStart= findViewById(R.id.start);
            mPause=findViewById(R.id.pause);
            mReset=findViewById(R.id.reset);
            chronometer = findViewById(R.id.chronometer);
            chronometer.setFormat("Time: %s");
            chronometer.setBase(SystemClock.elapsedRealtime());
			
			

            //Pagination
            findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mController.previous();
                }
            });
            findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mController.next();
                }
            });

            //Custom color
            spinner = (Spinner) findViewById(R.id.spinner1);
            spinner.setAdapter(new SpinnerAdapter(this));

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    switch(position){
                        case 0 : textView.setTextColor(getResources().getColor(R.color.black)); break;

                        case 1 : textView.setTextColor(getResources().getColor(R.color.Blue)); break;

                        case 2: textView.setTextColor(getResources().getColor(R.color.purple)); break;

                        case 3: textView.setTextColor(getResources().getColor(R.color.green)); break;

                        case 4: textView.setTextColor(getResources().getColor(R.color.orange)); break;

                        case 5:textView.setTextColor(getResources().getColor(R.color.red)); break;

                        case 6:textView.setTextColor(getResources().getColor(R.color.darkblue)); break;

                        case 7:textView.setTextColor(getResources().getColor(R.color.darkpurple)); break;

                        case 8:textView.setTextColor(getResources().getColor(R.color.darkgreen)); break;

                        case 9:textView.setTextColor(getResources().getColor(R.color.darkorange)); break;

                        case 10:textView.setTextColor(getResources().getColor(R.color.darkred)); break;

                        default:textView.setTextColor(getResources().getColor(R.color.black));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    // sometimes you need nothing here
                }
            });


            //Popup
            Button btn_show = (Button) findViewById(R.id.show_popup);
            btn_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //Open popup window
                    if (p != null)
                        showPopup(ReadingStory.this, p);
                }
            });

            //Receiving ID
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                SearchQueryTerm = extras.getString("titleOfStory");
            }

            //Substituting Stories ID-wise
            if (SearchQueryTerm.equals("0")) {
                inputStream = this.getResources().openRawResource(R.raw.monty);
                imageView.setImageResource(R.drawable.beginner);
            } else if (SearchQueryTerm.equals("1")) {
                inputStream = this.getResources().openRawResource(R.raw.maple);
                imageView.setImageResource(R.drawable.intermediate);
            } else if (SearchQueryTerm.equals("2")) {
                inputStream = this.getResources().openRawResource(R.raw.emily);
                imageView.setImageResource(R.drawable.advanced);
            } else {
                inputStream = this.getResources().openRawResource(R.raw.unavailable);
            }


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String strData = "";

            if (inputStream != null) {
                try {
                    while ((strData = bufferedReader.readLine()) != null) {
                        stringBuffer.append(strData + "\n");
                    }

                    textView.setText(stringBuffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Talk=stringBuffer.toString();
            onTextLoaded(textView.getText().toString());

        }

    //Pagination loading text
    void onTextLoaded(String text) {
        // start displaying loading here
        mController.onTextLoaded(text, new PaginationController.OnInitializedListener() {
            @Override
            public void onInitialized() {
                // stop displaying loading here
                // enable buttons
            }
        });
    }
        //Popup window focus
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        Button button = (Button) findViewById(R.id.show_popup);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p) {
        int popupWidth = 680;
        int popupHeight = 780;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popupmenu, viewGroup);


        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Speech to text

        mButtonSpeak=(Button)layout.findViewById(R.id.button_speak);

        mTTs = new TextToSpeech(this, new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status){
                if (status==TextToSpeech.SUCCESS){
                    int result = mTTs.setLanguage(Locale.ENGLISH);

                    if (result== TextToSpeech.LANG_MISSING_DATA
                            || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","Language not supported");
                    }
                    else{
                        mButtonSpeak.setEnabled(true);
                    }
                } else {
                    Log.e("TTS","Initialization failed");
                }
            }
        });


        mSeekBarPitch=(SeekBar)layout.findViewById(R.id.seek_bar_pitch);
        mSeekBarSpeed=(SeekBar)layout.findViewById(R.id.seek_bar_speed);


        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.button_speak);
        close.setOnClickListener(new View.OnClickListener() {

            //Breaking into chunks for TTS
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String text=Talk;
                Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
                Matcher reMatcher = re.matcher(text);
                /////
                int position=0 ;
                int sizeOfChar= text.length();
                String testStri= text.substring(position,sizeOfChar);



                float pitch=(float) mSeekBarPitch.getProgress() / 50;
                if (pitch<0.1) pitch=0.1f;
                float speed=(float) mSeekBarSpeed.getProgress() / 50;
                if (speed<0.1) speed=0.1f;

                mTTs.setPitch(pitch);
                mTTs.setSpeechRate(speed);
                while(reMatcher.find()) {
                    String temp="";

                    try {

                        temp = testStri.substring(text.lastIndexOf(reMatcher.group()), text.indexOf(reMatcher.group())+reMatcher.group().length());
                        mTTs.speak(temp, TextToSpeech.QUEUE_ADD, null,"speak");


                    } catch (Exception e) {
                        temp = testStri.substring(0, testStri.length());
                        mTTs.speak(temp, TextToSpeech.QUEUE_FLUSH, null);
                        break;

                    }

                }

                popup.dismiss();

            }
        });
    }


    //TTS requires to stop
    @Override
    protected void onDestroy() {
        if (mTTs != null){
            mTTs.stop();
            mTTs.shutdown();
        }
        super.onDestroy();
    }

    //Spinner
    class SpinnerAdapter extends BaseAdapter
    {
        ArrayList<Integer> colors;
        Context context;

        public SpinnerAdapter(Context context)
        {
            this.context=context;
            colors=new ArrayList<Integer>();
            int retrieve []=context.getResources().getIntArray(R.array.androidcolors);
            for(int re:retrieve)
            {
                colors.add(re);
            }
        }
        @Override
        public int getCount()
        {
            return colors.size();
        }
        @Override
        public Object getItem(int arg0)
        {
            return colors.get(arg0);
        }
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }
        @Override
        public View getView(int pos, View view, ViewGroup parent)
        {
            LayoutInflater inflater=LayoutInflater.from(context);
            view=inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            TextView txv=(TextView)view.findViewById(android.R.id.text1);
            txv.setBackgroundColor(colors.get(pos));
            txv.setTextSize(20f);
            txv.setText("     ");
            return view;
        }

    }
	
	 public void startChronometer(View view) {
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronometer.start();
            running=true;
            Toast toast = Toast.makeText(this,"Timer Started",Toast.LENGTH_SHORT);
            toast.show();
            mPause.setEnabled(true);
        }
    }


    public void pauseChronometer(View view) {
        if(running){
            chronometer.stop();
            pauseOffset= SystemClock.elapsedRealtime() - chronometer.getBase();
            Toast toast = Toast.makeText(this,"Timer Paused ",Toast.LENGTH_SHORT);
            toast.show();
            running=false;

            mStart.setEnabled(true);
            mPause.setEnabled(false);

        }
    }

    public void stopChronometer(View view) {
        if(running){
            chronometer.stop();
            pauseOffset= SystemClock.elapsedRealtime() - chronometer.getBase();
            Toast toast = Toast.makeText(this,"Final Time is: "+chronometer.getContentDescription(),Toast.LENGTH_SHORT);
            toast.show();
            resetChronometer(view);
            mStart.setEnabled(true);

        }
    }

    public void resetChronometer(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset=0;
        //Toast toast = Toast.makeText(this,"Reset Time",Toast.LENGTH_SHORT);
        //toast.show();
        mPause.setEnabled(true);
    }



}