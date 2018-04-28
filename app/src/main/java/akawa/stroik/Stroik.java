package akawa.stroik;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 *
 * Stroik klasa zajmująca się strojeniem automatycznym
 *
 * posiada spinner który wyświetla nazwy strun w zalezności od częstotliwości w Hz
 *
 * opisz dokładnie te metody, raczej tu nie ma nic trudnego, a jak coś to pisz na fb
 *
 *
 */


public class Stroik extends Activity {

    private ImageView guitarImageView;
    private TextView messageTextView;
    private TextView messageTextViewGood;
    private TextView messageTextViewThis;
    private TextView messageTextViewInfo;

    private FrequencyCheck frequencyChecker;
    private Interface controller;
    private byte tuningType = 0;
    private short tuningStringID = 0;
    int oldString = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stroik);

        guitarImageView = (ImageView) findViewById(R.id.imageView);
        messageTextView = (TextView) findViewById(R.id.textView2);
        messageTextViewGood = (TextView) findViewById(R.id.textView);
        messageTextViewThis = (TextView) findViewById(R.id.textView3);
        messageTextViewInfo = (TextView) findViewById(R.id.textView4);
        initSpinners();

        controller = new Interface(this);
        frequencyChecker = new FrequencyCheck();
        frequencyChecker.addObserver(controller);
    }

    private void initSpinners() {
        final Spinner stringSpinner;


        stringSpinner = (Spinner) findViewById(R.id.spinner3);

        List<String> list2 = new ArrayList<>();
        list2.add("E");
        list2.add("A");
        list2.add("D");
        list2.add("G");
        list2.add("B");
        list2.add("e");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           stringSpinner.setAdapter(dataAdapter2);
           stringSpinner.setEnabled(false);



        stringSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String guitarString = stringSpinner.getSelectedItem().toString();
                if (tuningType == 1)
                    switch (guitarString) {
                        case "E": {
                            tuningStringID = 0;
                            updateMessageG("E");
                            break;
                        }
                        case "A":{
                            tuningStringID = 1;
                            updateMessageG("A");
                            break;
                        }
                        case "D":{
                            tuningStringID = 2;
                            updateMessageG("D");
                            break;
                        }
                        case "G":{
                            tuningStringID = 3;
                            updateMessageG("G");
                            break;
                        }
                        case "B":{
                            tuningStringID = 4;
                            updateMessageG("B");
                            break;
                        }
                        case "e":{
                            tuningStringID = 5;
                            updateMessageG("e");
                            break;
                        }
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }


    public void changeString(int stringID) {
        if (oldString != stringID) {
            // guitarImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), images[stringID], null));
            oldString = stringID;
        }
    }

    public void updateMessage(String msg) {
        messageTextView.setText(msg);
    }
    public void updateMessageG(String msg) {
        messageTextViewGood.setText(msg);
    }
    public void updateMessageT(String msg) {
        messageTextViewThis.setText(msg);
    }
    public void updateMessageI(String msg) {
        messageTextViewInfo.setText(msg);
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (frequencyChecker != null)
            frequencyChecker.startThreadAgain();
    }


    public byte getTuningType() {
        return tuningType;
    }
    public short getTuningStringID() {
        return tuningStringID;
    }
}