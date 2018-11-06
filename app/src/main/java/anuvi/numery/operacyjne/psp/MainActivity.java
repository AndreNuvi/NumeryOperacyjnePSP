package anuvi.numery.operacyjne.psp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabaseFirstNumber;
    private DatabaseReference mDatabaseProvince;
    private DatabaseReference mDatabaseSecondNumber;
    private EditText mFirstNumberEdiText;
    private EditText mSecondNumberEditText;
    private TextView mProvinceTextView;
    private TextView mUnitNameTextView;
    private TextView mKindTextView;
    private  TextView mAdnotationTextView;
    private Spinner mInfixSpinner;

    Button mEnterButton;
    String mGottenFirstNumber;
    String mGottenSecondNumber;
    String mInfixSpinnerSelectedItem;
    String mUnitName = null;
    String mTypeOfHeadquarter;
    String mTypeOfUnit;
    String mOspOrJrg;


    Boolean isOSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirstNumberEdiText = findViewById(R.id.edit_first_number);
        mSecondNumberEditText = findViewById(R.id.edit_second_number);
        mProvinceTextView = findViewById(R.id.text_province);
        mUnitNameTextView = findViewById(R.id.text_city);
        mKindTextView = findViewById(R.id.text_kind);
        mAdnotationTextView = findViewById(R.id.text_adnotacja);
        mEnterButton = findViewById(R.id.btn_enter);

        //Infix spinner setup
        mInfixSpinner = findViewById(R.id.spinner_infix);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.infix_spinner, R.layout.spinner_infix_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        mInfixSpinner.setAdapter(adapter);
        mInfixSpinner.setOnItemSelectedListener(this);

        //Load infix from shared preferences
        SharedPreferences sp = getSharedPreferences("infix_prefs", Activity.MODE_PRIVATE);
        int mSpinnerPosition = sp.getInt("chosenLetter", 0);

        mInfixSpinner.setSelection(mSpinnerPosition);
        mInfixSpinnerSelectedItem = mInfixSpinner.getSelectedItem().toString();
        mInfixSpinner.setBackgroundColor(Color.TRANSPARENT);

        //Clean text views
        mProvinceTextView.setText("");
        mUnitNameTextView.setText("");
        mKindTextView.setText("");
        mAdnotationTextView.setText("");

        //Set font
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/oznaczenia.ttf");
        mFirstNumberEdiText.setTypeface(typeface);
        mSecondNumberEditText.setTypeface(typeface);

        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGottenFirstNumber = mFirstNumberEdiText.getText().toString();
                mGottenSecondNumber = mSecondNumberEditText.getText().toString();

                //Hide keyboard after click
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                //Save position of spinner
                SharedPreferences sp = getSharedPreferences("infix_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("chosenLetter", mInfixSpinner.getSelectedItemPosition());
                editor.apply();

                //Infix
                mDatabaseProvince = FirebaseDatabase.getInstance().getReference().child(mInfixSpinnerSelectedItem).child("province");
                mDatabaseProvince.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String mInfix = dataSnapshot.getValue().toString();
                            mProvinceTextView.setText("województwo " + mInfix);
                        } catch (NullPointerException e) {
                            mProvinceTextView.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                if (mGottenFirstNumber.length() == 3) {
//                    mTypeOfUnit = mGottenFirstNumber.substring(2);
//                    String jrg = "Jednostka Ratowniczo-Gaśnicza";
//                    String ospWKsrg = "OSP w KSRG";
//                    String ospPozaKsrg = "OSP w KSRG";
//                    if (mTypeOfUnit.equals("1")) {
//                        mOspOrJrg = jrg;
//                        isOSP = false;
//                    } else if (mTypeOfUnit.equals("2")) {
//                        mOspOrJrg = jrg;
//                        isOSP = false;
//                    } else if (mTypeOfUnit.equals("3")) {
//                        mOspOrJrg = jrg;
//                        isOSP = false;
//                    } else if (mTypeOfUnit.equals("4")) {
//                        mOspOrJrg = jrg;
//                        isOSP = false;
//                    } else if (mTypeOfUnit.equals("5")) {
//                        mOspOrJrg = jrg;
//                        isOSP = false;
//                    } else if (mTypeOfUnit.equals("6")) {
//                        mOspOrJrg = jrg;
//                        isOSP = false;
//                    } else if (mTypeOfUnit.equals("7")) {
//                        mOspOrJrg = ospPozaKsrg;
//                        isOSP = true;
//                    } else if (mTypeOfUnit.equals("8")) {
//                        mOspOrJrg = ospPozaKsrg;
//                        isOSP = true;
//                    } else if (mTypeOfUnit.equals("9")) {
//                        mOspOrJrg = ospWKsrg;
//                        isOSP = true;
//                    } else if (mTypeOfUnit.equals("0")) {
//                        mOspOrJrg = "";
//                        isOSP = false;
//                    }
//                }


                //Prefix
                if (mGottenFirstNumber.length() == 2 || mGottenFirstNumber.length() == 1 || mGottenFirstNumber.equals("")) {
                    Toast.makeText(MainActivity.this, "Prefix musi zawierać 3 cyfry", Toast.LENGTH_SHORT).show();
                    mUnitNameTextView.setText("");
                } else if (mGottenFirstNumber.length() == 3) {
                    //Check if unit is in database
                    mDatabaseFirstNumber = FirebaseDatabase.getInstance().getReference().child(mInfixSpinnerSelectedItem).child(mGottenFirstNumber);
                    mDatabaseFirstNumber.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                mUnitName = dataSnapshot.getValue().toString();
                                mUnitNameTextView.setText(mUnitName);
                            } catch (NullPointerException e) {
                                // Check what type of headquarter it is
                                mTypeOfHeadquarter = mGottenFirstNumber.substring(0, 2) + "0";
                                mDatabaseFirstNumber = FirebaseDatabase.getInstance().getReference().child(mInfixSpinnerSelectedItem).child(mTypeOfHeadquarter);
                                mDatabaseFirstNumber.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            mUnitName = dataSnapshot.getValue().toString();
//                                            mUnitNameTextView.setText(mOspOrJrg + "\n" + mUnitName);
                                            mUnitNameTextView.setText(mUnitName);

                                        } catch (NullPointerException e) {
                                            Toast.makeText(MainActivity.this, "Niepoprawny prefix", Toast.LENGTH_SHORT).show();
                                            mUnitNameTextView.setText("");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                //Sufix
                if (mGottenSecondNumber.equals("")) {
                    mKindTextView.setText("");
//                } else if (isOSP == true && mGottenSecondNumber.length() == 2) {
//                    mKindTextView.setText("w przypadku OSP sufix nie oznacza typu samochodu");
                } else if (mGottenSecondNumber.length() == 1) {
                    Toast.makeText(MainActivity.this, "Sufix musi zawierać 2 cyfry", Toast.LENGTH_SHORT).show();
                    mKindTextView.setText("");
                } else {
                    mDatabaseSecondNumber = FirebaseDatabase.getInstance().getReference().child("sufix").child(mGottenSecondNumber);
                    mDatabaseSecondNumber.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                String secondNumber = dataSnapshot.getValue().toString();
                                mKindTextView.setText(secondNumber);
                                mAdnotationTextView.setText(R.string.sufix);
                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, "Niepoprawny sufix", Toast.LENGTH_SHORT).show();
                                mKindTextView.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                checkNetwork();
            }

        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mInfixSpinnerSelectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
            default:
        }
        return true;
    }

    public void checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();
        if (ninfo != null && ninfo.isConnected()) {
            //do nothing
        } else {
            mUnitNameTextView.setText(R.string.internet);
            mKindTextView.setText("");
            mProvinceTextView.setText("");
            mAdnotationTextView.setText("");

        }
    }


}
