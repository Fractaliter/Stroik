package akawa.stroik;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;


/**
 * Created by Andżej on 2017-06-13.
 */

public class Main extends Activity {
    private ImageView guitarImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void manual(View view) {
        Intent intent;
        intent = new Intent(Main.this, Stroik.class);
        startActivity(intent);

    }

    public void sluchu(View view) {
        Intent intent;
        intent = new Intent(Main.this, StrojenieSluch.class);
        startActivity(intent);
    }

    public void Wyjscie(View view) {
        finish();
        System.exit(0);
    }

}