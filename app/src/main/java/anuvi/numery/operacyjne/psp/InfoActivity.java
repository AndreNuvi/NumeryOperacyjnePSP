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
        mInfo1TextView.setText("Aplikacja \"Numery Opecyjne PSP\" służy do szybkiego identyfikowania przynależności organizacyjnej pojazdów ratowniczo-gaśniczych\n\n" +
                "Aplikacja nie zawiera dokładnych nazw jednostek ratowniczo-gaśniczych ani jednostek ochotniczych straży pożarnych, a jedynie ogólną informację o rodzaju pojazdu, województwie oraz komendzie psp, pod którą podlega\n\n" +
                "Aplikacja bazuje na załączniku zarządzenia Komendanta Głównego PSP z dnia 10 kwietnia 2008 r. w sprawie gospodarki transportowej w jednostkach organizacyjnych PSP oraz na ogólnie dostępnych w internecie wykazach jednostek organizacyjnych PSP wraz z ich kryptonimami.");

    }
}
