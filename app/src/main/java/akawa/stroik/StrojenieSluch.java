package akawa.stroik;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.AudioManager;
import android.content.Context;

/**
 *  strojenie z słuchu - tu pytaj krzyśka co się odjaniepawla ja tego nie tykałem
 *
 */

public class StrojenieSluch   extends Activity {

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strojenie_sluch);



        /*AudioManager amanager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);*/
     //   emp.setAudioStreamType(AudioManager.STREAM_ALARM);

        final MediaPlayer emp = MediaPlayer.create(this, R.raw.e1);
        Button playe= (Button) this.findViewById(R.id.struna6);
        playe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                emp.setVolume(1.0f, 1.0f);
                emp.start();
            }
        });
                final MediaPlayer amp = MediaPlayer.create(this, R.raw.a);
                Button playa= (Button) this.findViewById(R.id.struna5);
                playa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        amp.setVolume(1.0f, 1.0f);
                        amp.start();
                    }
                });




                final MediaPlayer dmp = MediaPlayer.create(this, R.raw.d);
                Button playd= (Button) this.findViewById(R.id.struna4);
                playd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dmp.setVolume(1.0f, 1.0f);
                        dmp.start();

                    }
                });




                final MediaPlayer gmp = MediaPlayer.create(this, R.raw.g);
                Button playg= (Button) this.findViewById(R.id.struna3);
                playg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        gmp.setVolume(1.0f, 1.0f);
                        gmp.start();
                    }
                });





                final MediaPlayer hmp = MediaPlayer.create(this, R.raw.h);
                Button playh= (Button) this.findViewById(R.id.struna2);
                playh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        hmp.setVolume(1.0f, 1.0f);
                        hmp.start();
                    }
                });

                final MediaPlayer e1mp = MediaPlayer.create(this, R.raw.e6);
                Button playe1mp= (Button) this.findViewById(R.id.struna1);
                playe1mp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        e1mp.setVolume(1.0f, 1.0f);
                        e1mp.start();
                    }
                });






    }


}