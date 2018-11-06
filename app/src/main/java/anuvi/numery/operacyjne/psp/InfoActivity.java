package anuvi.numery.operacyjne.psp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_avtivity);
        TextView mInfo1TextView = findViewById(R.id.textViewInfoOne);
        mInfo1TextView.setText(R.string.info_text);

    }
}
