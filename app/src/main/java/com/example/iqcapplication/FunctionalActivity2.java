package com.example.iqcapplication;

import static com.example.iqcapplication.MainActivity.connectionClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iqcapplication.Update.DimensionActivity;
import com.example.iqcapplication.add.InspectionDetailsActivity;
import com.example.iqcapplication.fragments.FragmentForFunctional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FunctionalActivity2 extends AppCompatActivity {


    EditText Fc_Checkpoints, Fc_Samplesize, Fc_Sampleunit, Fc_1, Fc_2, Fc_3, Fc_4, Fc_5, Fc_6, Fc_7, Fc_8, Fc_9, Fc_10, Lowerspec, Upperspec;

    Button rejectquantityyyy,finishhh;

    TextView FC_Judgement,  Minimum, Average, Maximum,dateTodayfc;


    AutoCompleteTextView instrumentUsed;

    public ArrayAdapter dcinstrument_adapter;

    Button viewDatafc,uploadtosqlitefc,SQLSERVERUP ;

    float num1 = 0;
    float num2 = 0;
    float num3 = 0;
    float num4 = 0;
    float num5 = 0;
    float num6 = 0;
    float num7 = 0;
    float num8 = 0;
    float num9 = 0;
    float num10 = 0;

    public static int  samplesizefc_id_hldr=0, funcheck_id_hldr = 0, sampleSizeDC = 0;
    public  static String judgeHolder = "PASSED", colorHolder = "#58f40b";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional);

        Fc_Checkpoints   = findViewById(R.id.checkPointfc);
        Fc_Sampleunit  = findViewById(R.id.sampleUnit);
        Fc_Samplesize = findViewById(R.id.sampleSizefc_);
        Fc_Checkpoints  = findViewById(R.id.checkPointfc);
        Lowerspec   = findViewById(R.id.lowerspecfc);
        Upperspec = findViewById(R.id.upperspecsfc);
        instrumentUsed  = findViewById(R.id.instrumentUsedfc);
        Minimum = findViewById(R.id.minimumfc);
        Maximum = findViewById(R.id.maximumfc);
        Average = findViewById(R.id.averagefc);
        FC_Judgement = findViewById(R.id.fcjudgeMent);
        dateTodayfc = findViewById(R.id.dateTodayfc);
        rejectquantityyyy = findViewById(R.id.rejectquanttt);
        Fc_1  = findViewById(R.id.fc1);
        Fc_2  = findViewById(R.id.fc2);
        Fc_3  = findViewById(R.id.fc3);
        Fc_4  = findViewById(R.id.fc4);
        Fc_5  = findViewById(R.id.fc5);
        Fc_6  = findViewById(R.id.fc6);
        Fc_7  = findViewById(R.id.fc7);
        Fc_8  = findViewById(R.id.fc8);
        Fc_9  = findViewById(R.id.fc9);
        Fc_10  = findViewById(R.id.fc10);

        Button deleteRecords = (Button) findViewById(R.id.deleteAllRecordfunc2);
        uploadtosqlitefc  = findViewById(R.id.updateTosqlite);
        SQLSERVERUP = findViewById(R.id.uploadtoSQL);
        finishhh = findViewById(R.id.rejectquanttt2);


        samplenumberenabled();
        upperSpec();
        sampleComputation();
        fcinstrumentUsedd();
        disableTexts();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String noww = df.format(new Date());
        dateTodayfc.setText(noww);


        viewDatafc = findViewById(R.id.viewdadtfun);


        deleteRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecords();
            }
        });
        rejectquantityyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionalActivity2.this, SapmpleActivityinlot.class);

                startActivity(intent);
            }
        });


        viewDatafc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragmentForFunctional());
            }
        });


        SQLSERVERUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog10();
            }
        });

        finishhh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });


    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    void confirmDialog10() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UPLOAD TO " + "SQL SERVER" + "?");
        builder.setMessage("Are you sure you want to upload?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insert_funcheck();
                addDatatoSQLite();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }



    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("FINISH " + "FILING DATA" + "?");
        builder.setMessage("Are you sure you want to proceed?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(FunctionalActivity2.this, SapmpleActivityinlot.class);
                    Toast.makeText(FunctionalActivity2.this, "Finish Inspection", Toast.LENGTH_SHORT).show();
                    startActivity(intent);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }



    private void replaceFragment(FragmentForFunctional fcfragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fcframelayout,fcfragment);
        fragmentTransaction.commit();
    }


    public void fcinstrumentUsedd(){
        AutoCompleteTextView instrumentUsed =  findViewById(R.id.instrumentUsedfc);
        dcinstrument_adapter = ArrayAdapter.createFromResource(this, R.array.func, android.R.layout.simple_dropdown_item_1line);
        dcinstrument_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instrumentUsed.setAdapter(dcinstrument_adapter);

        instrumentUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instrumentUsed.showDropDown();

            }
        });

    }



    public void disableTexts() {
        Fc_2.setEnabled(false);
        Fc_3.setEnabled(false);
        Fc_4.setEnabled(false);
        Fc_5.setEnabled(false);
        Fc_6.setEnabled(false);
        Fc_7.setEnabled(false);
        Fc_8.setEnabled(false);
        Fc_9.setEnabled(false);
        Fc_10.setEnabled(false);

    }


    //----------------------SAMPLE ENABLE DIABLE---------------------------//
    public void samplenumberenabled(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Fc_Samplesize.getText().toString().equals("2")){
                    Fc_2.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);
                }
                if(Fc_Samplesize.getText().toString().equals("3")){
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }
                if(Fc_Samplesize.getText().toString().equals("4")){
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }

                if(Fc_Samplesize.getText().toString().equals("5")){
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                    Fc_5.setEnabled(true);
                }
                else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }


                if(Fc_Samplesize.getText().toString().equals("6")){
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                    Fc_5.setEnabled(true);
                    Fc_6.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }
                if(Fc_Samplesize.getText().toString().equals("7")) {
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                    Fc_5.setEnabled(true);
                    Fc_6.setEnabled(true);
                    Fc_7.setEnabled(true);

                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }

                if(Fc_Samplesize.getText().toString().equals("8")) {
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                    Fc_5.setEnabled(true);
                    Fc_6.setEnabled(true);
                    Fc_7.setEnabled(true);
                    Fc_8.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }


                if(Fc_Samplesize.getText().toString().equals("9")) {
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                    Fc_5.setEnabled(true);
                    Fc_6.setEnabled(true);
                    Fc_7.setEnabled(true);
                    Fc_8.setEnabled(true);
                    Fc_9.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }
                if(Fc_Samplesize.getText().toString().equals("10")) {
                    Fc_2.setEnabled(true);
                    Fc_3.setEnabled(true);
                    Fc_4.setEnabled(true);
                    Fc_5.setEnabled(true);
                    Fc_6.setEnabled(true);
                    Fc_7.setEnabled(true);
                    Fc_8.setEnabled(true);
                    Fc_9.setEnabled(true);
                    Fc_10.setEnabled(true);
                }else if(Fc_Samplesize.getText().toString().equals("")){
                    Fc_2.setEnabled(false);
                    Fc_3.setEnabled(false);
                    Fc_4.setEnabled(false);
                    Fc_5.setEnabled(false);
                    Fc_6.setEnabled(false);
                    Fc_7.setEnabled(false);
                    Fc_8.setEnabled(false);
                    Fc_9.setEnabled(false);
                    Fc_10.setEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        Fc_Samplesize.addTextChangedListener(textWatcher);
        Fc_1.addTextChangedListener(textWatcher);
        Fc_2 .addTextChangedListener(textWatcher);
        Fc_3.addTextChangedListener(textWatcher);
        Fc_4.addTextChangedListener(textWatcher);
        Fc_5.addTextChangedListener(textWatcher);
        Fc_6.addTextChangedListener(textWatcher);
        Fc_7.addTextChangedListener(textWatcher);
        Fc_8.addTextChangedListener(textWatcher);
        Fc_9.addTextChangedListener(textWatcher);
        Fc_10.addTextChangedListener(textWatcher);
    }


    //----------------------UPPERSPEC TEXTCHANGE---------------------------//
    public void upperSpec(){

        Upperspec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                judgeHolder = "PASSED";
                colorHolder = "#58f40b";


                if(Upperspec.getText().toString().equals(""))
                {
                    FC_Judgement.setText("");
                    Fc_1.setTextColor(Color.parseColor("#000000"));
                    Fc_2.setTextColor(Color.parseColor("#000000"));
                    Fc_3.setTextColor(Color.parseColor("#000000"));
                    Fc_4.setTextColor(Color.parseColor("#000000"));
                    Fc_5.setTextColor(Color.parseColor("#000000"));
                    Fc_6.setTextColor(Color.parseColor("#000000"));
                    Fc_7.setTextColor(Color.parseColor("#000000"));
                    Fc_8.setTextColor(Color.parseColor("#000000"));
                    Fc_9.setTextColor(Color.parseColor("#000000"));
                    Fc_10.setTextColor(Color.parseColor("#000000"));

                }

                else {
                    float Lspec = 0;
                    float Uspec = 0;

                    try{
                        Lspec = Float.parseFloat(Lowerspec.getText().toString());
                        Uspec = Float.parseFloat(Upperspec.getText().toString());
                    }
                    catch (Exception ex){
                        Toast.makeText(FunctionalActivity2.this, "Input Valid Value", Toast.LENGTH_SHORT).show();
                    }

                    if (!Fc_1.getText().toString().equals(""))
                    {
                        try {
                            num1 = Float.parseFloat(Fc_1.getText().toString());
                        }
                        catch (Exception ex)
                        {

                            Toast.makeText(getApplicationContext(), ex.toString()  , Toast.LENGTH_LONG).show();

                        }

                        if (num1 < Lspec || num1 > Uspec)
                        {

                            judgeHolder = "FAILED";
                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_1.setTextColor(Color.parseColor("#FF0000"));

                        }else{
                            FC_Judgement.setText(judgeHolder);

                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_1.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!Fc_2.getText().toString().equals("")) {

                        try {
                            num2 = Float.parseFloat(Fc_2.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }

                        if (num2 < Lspec || num2 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);

                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_2.setTextColor(Color.parseColor("#FF0000"));
                        } else {
                            FC_Judgement.setText(judgeHolder);

                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_2.setTextColor(Color.parseColor("#58f40b"));
                        }

                    }

                    if (!Fc_3.getText().toString().equals("")) {
                        try {
                            num3 = Float.parseFloat(Fc_3.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }



                        if (num3 < Lspec || num3 > Uspec)
                        {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);

                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_3.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_3.setTextColor(Color.parseColor("#58f40b"));
                        }

                    }

                    if (!Fc_4.getText().toString().equals("")) {
                        try{
                            num4 = Float.parseFloat(Fc_4.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num4 < Lspec || num4 > Uspec)
                        {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);

                            Fc_4.setTextColor(Color.parseColor("#FF0000"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_4.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!Fc_5.getText().toString().equals("")) {
                        try{
                            num5 = Float.parseFloat(Fc_5.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num5 < Lspec || num5 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);

                            Fc_5.setTextColor(Color.parseColor(colorHolder));
                            FC_Judgement.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_5.setTextColor(Color.parseColor("#58f40b"));

                        }

                    }

                    if (!Fc_6.getText().toString().equals("")) {

                        try{
                            num6 = Float.parseFloat(Fc_6.getText().toString());
                        }catch (Exception e ){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num6 < Lspec || num6 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);
                            Fc_6.setTextColor(Color.parseColor(colorHolder));
                            FC_Judgement.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_6.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!Fc_7.getText().toString().equals(""))
                    {
                        try{
                            num7 = Float.parseFloat(Fc_7.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num7 < Lspec || num7 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);
                            Fc_7.setTextColor(Color.parseColor("#FF0000"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                            Fc_7.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!Fc_8.getText().toString().equals("")) {

                        try{
                            num8 = Float.parseFloat(Fc_8.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num8 < Lspec || num8 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);
                            Fc_8.setTextColor(Color.parseColor("#FF0000"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            Fc_8.setTextColor(Color.parseColor("#58f40b"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }
                    }

                    if (!Fc_9.getText().toString().equals("")) {

                        try{
                            num9 = Float.parseFloat(Fc_9.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num9 < Lspec || num9 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);
                            Fc_9.setTextColor(Color.parseColor("#FF0000"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            Fc_9.setTextColor(Color.parseColor("#58f40b"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }
                    }

                    if (!Fc_10.getText().toString().equals("")) {

                        try{
                            num10 = Float.parseFloat(Fc_10.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num10 < Lspec || num10 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            FC_Judgement.setText(judgeHolder);
                            Fc_10.setTextColor(Color.parseColor("#FF0000"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            FC_Judgement.setText(judgeHolder);
                            Fc_10.setTextColor(Color.parseColor("#58f40b"));
                            FC_Judgement.setTextColor(Color.parseColor(colorHolder));
                        }
                    }

                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //---------------------COMPUTATION FOR MIN MAX AVE---------------------//
    void sampleComputation(){

        TextWatcher textWatcher = new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(isNumeric(Fc_1.getText().toString()) == true) {

                    Lowerspec.setEnabled(true);
                    Upperspec.setEnabled(true);

                    List<Float> list = new ArrayList<Float>();
                    float sum = 0;
                    list.add(num1);
                    Collections.sort(list);

                    float average = (num1) / list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);

                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }
                else{
                    Minimum.setText("");
                    Average.setText("");
                    Maximum.setText("");
                    Lowerspec.setEnabled(false);
                    Upperspec.setEnabled(false);
                }
                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true) {
                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    List<Float> list = new ArrayList<Float>();
                    float sum = 0;
                    list.add(num1);
                    list.add(num2);
                    Collections.sort(list);



                    float average = (num1+num2)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString())== true && isNumeric(Fc_2.getText().toString()) == true &&isNumeric(Fc_3.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    Collections.sort(list);

                    float average = (num1+num2+num3)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString())== true && isNumeric(Fc_2.getText().toString()) == true &&isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true && isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true && isNumeric(Fc_5.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    float num5 = Float.parseFloat(Fc_5.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    list.add(num5);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4+num5)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true && isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true && isNumeric(Fc_5.getText().toString()) == true && isNumeric(Fc_6.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    float num5 = Float.parseFloat(Fc_5.getText().toString());
                    float num6 = Float.parseFloat(Fc_6.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    list.add(num5);
                    list.add(num6);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4+num5+num6)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);

                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true && isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true && isNumeric(Fc_5.getText().toString()) == true && isNumeric(Fc_6.getText().toString()) == true && isNumeric(Fc_1.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    float num5 = Float.parseFloat(Fc_5.getText().toString());
                    float num6 = Float.parseFloat(Fc_6.getText().toString());
                    float num7 = Float.parseFloat(Fc_7.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    list.add(num5);
                    list.add(num6);
                    list.add(num7);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4+num5+num6+num7)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);

                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }


                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true && isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true && isNumeric(Fc_5.getText().toString()) == true && isNumeric(Fc_6.getText().toString()) == true && isNumeric(Fc_7.getText().toString()) == true && isNumeric(Fc_8.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    float num5 = Float.parseFloat(Fc_5.getText().toString());
                    float num6 = Float.parseFloat(Fc_6.getText().toString());
                    float num7 = Float.parseFloat(Fc_7.getText().toString());
                    float num8 = Float.parseFloat(Fc_8.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    list.add(num5);
                    list.add(num6);
                    list.add(num7);
                    list.add(num8);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4+num5+num6+num7+num8)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true && isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true && isNumeric(Fc_5.getText().toString()) == true && isNumeric(Fc_6.getText().toString()) == true && isNumeric(Fc_7.getText().toString()) == true && isNumeric(Fc_8.getText().toString()) == true && isNumeric(Fc_9.getText().toString()) == true) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    float num5 = Float.parseFloat(Fc_5.getText().toString());
                    float num6 = Float.parseFloat(Fc_6.getText().toString());
                    float num7 = Float.parseFloat(Fc_7.getText().toString());
                    float num8 = Float.parseFloat(Fc_8.getText().toString());
                    float num9 = Float.parseFloat(Fc_9.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    list.add(num5);
                    list.add(num6);
                    list.add(num7);
                    list.add(num8);
                    list.add(num9);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4+num5+num6+num7+num8+num9)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }else{

                }

                if(isNumeric(Fc_1.getText().toString()) == true && isNumeric(Fc_2.getText().toString()) == true && isNumeric(Fc_3.getText().toString()) == true && isNumeric(Fc_4.getText().toString()) == true && isNumeric(Fc_5.getText().toString()) == true && isNumeric(Fc_6.getText().toString()) == true && isNumeric(Fc_7.getText().toString()) == true && isNumeric(Fc_8.getText().toString()) == true && isNumeric(Fc_9.getText().toString()) == true && isNumeric(Fc_10.getText().toString()) == true  ) {

                    float num1 = Float.parseFloat(Fc_1.getText().toString());
                    float num2 = Float.parseFloat(Fc_2.getText().toString());
                    float num3 = Float.parseFloat(Fc_3.getText().toString());
                    float num4 = Float.parseFloat(Fc_4.getText().toString());
                    float num5 = Float.parseFloat(Fc_5.getText().toString());
                    float num6 = Float.parseFloat(Fc_6.getText().toString());
                    float num7 = Float.parseFloat(Fc_7.getText().toString());
                    float num8 = Float.parseFloat(Fc_8.getText().toString());
                    float num9 = Float.parseFloat(Fc_9.getText().toString());
                    float num10 = Float.parseFloat(Fc_10.getText().toString());
                    List<Float> list = new ArrayList<Float>();

                    list.add(num1);
                    list.add(num2);
                    list.add(num3);
                    list.add(num4);
                    list.add(num5);
                    list.add(num6);
                    list.add(num7);
                    list.add(num8);
                    list.add(num9);
                    list.add(num10);
                    Collections.sort(list);

                    float average = (num1+num2+num3+num4+num5+num6+num7+num8+num9+num10)/list.size();

                    final float min = list.get(0);
                    float max = list.get(list.size() - 1);


                    Minimum.setText(String.valueOf(min));
                    Maximum.setText(String.valueOf(max));
                    Average.setText(String.valueOf(average));

                }
                else{

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        Fc_1.addTextChangedListener(textWatcher);
        Fc_2.addTextChangedListener(textWatcher);
        Fc_3.addTextChangedListener(textWatcher);
        Fc_3.addTextChangedListener(textWatcher);
        Fc_4.addTextChangedListener(textWatcher);
        Fc_5.addTextChangedListener(textWatcher);
        Fc_6.addTextChangedListener(textWatcher);
        Fc_7.addTextChangedListener(textWatcher);
        Fc_8.addTextChangedListener(textWatcher);
        Fc_9.addTextChangedListener(textWatcher);
        Fc_10.addTextChangedListener(textWatcher);

    }

    //----------------------GET LATEST ID---------------------------------//
    public int Latest_ID(String tablename){
        int output = 0;

        connectionClass = new ConnectionClass();

        try {
            Connection con = connectionClass.CONN2();//open ng connection sa connection class
            String query = "SELECT TOP 1 id FROM "+tablename+" ORDER BY id DESC";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                output = id;
            }
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT);
        }

        return output;
    }


    //----------------------SQLITE DATABASE INSERTION--------------------//
    public void addDatatoSQLite(){
        try{


            DatabaseHelper myDB = new DatabaseHelper(FunctionalActivity2.this);
            myDB.addFC(

                    instrumentUsed.getText().toString().trim(),
                    Fc_Samplesize.getText().toString().trim(),
                    Fc_Checkpoints.getText().toString().trim(),
                    Fc_Sampleunit.getText().toString().trim(),

                    Fc_1.getText().toString().trim(),
                    Fc_2.getText().toString().trim(),
                    Fc_3.getText().toString().trim(),
                    Fc_4.getText().toString().trim(),
                    Fc_5.getText().toString().trim(),
                    Fc_6.getText().toString().trim(),
                    Fc_7.getText().toString().trim(),
                    Fc_8.getText().toString().trim(),
                    Fc_9.getText().toString().trim(),
                    Fc_10.getText().toString().trim(),

                    Lowerspec.getText().toString().trim(),
                    Upperspec.getText().toString().trim(),
                    Minimum.getText().toString().trim(),
                    Average.getText().toString().trim(),
                    Maximum.getText().toString().trim(),
                    FC_Judgement.getText().toString().trim(),
                    dateTodayfc.getText().toString().trim()
            );
            //   insert_dimcheck();


        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        return;
    }


    void deleteRecords() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("DELETE " + " " +
                "PREVIOUS RECORDS " + "?");
        builder.setMessage("Are you sure you want to DELETE data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(FunctionalActivity2.this);
                myDB.deleteallRow3();
                Toast.makeText(FunctionalActivity2.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FunctionalActivity2.this, FunctionalActivity2.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }



  //-------------------------SQL SERVER INSERTION-----------------------//
    public void insert_funcheck()
    {

        String Checkpoints = Fc_Checkpoints.getText().toString();
        String Instrumentused = instrumentUsed.getText().toString();
        String Sampleunit = Fc_Sampleunit.getText().toString();
        String DC1 = Fc_1.getText().toString();
        String DC2 = Fc_2.getText().toString();
        String DC3 = Fc_3.getText().toString();
        String DC4 = Fc_4.getText().toString();
        String DC5 = Fc_5.getText().toString();
        String DC6 = Fc_6.getText().toString();
        String DC7 = Fc_7.getText().toString();
        String DC8 = Fc_8.getText().toString();
        String DC9 = Fc_9.getText().toString();
        String DC10 = Fc_10.getText().toString();
        String Min = Minimum.getText().toString();
        String Ave = Average.getText().toString();
        String Max = Maximum.getText().toString();
        String LowerSpec = Lowerspec.getText().toString();
        String UppperSpec = Upperspec.getText().toString();
        String Judgmnt = FC_Judgement.getText().toString();
        String Samplesize = Fc_Samplesize.getText().toString();
//        String Remm = Remarkss.getText().toString();


        if (Checkpoints.trim().equals(""))//dagdagan mo
        {
            Toast.makeText(getApplicationContext(), "Must input atleast 1 checkpoint!", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                connectionClass = new ConnectionClass();
                Connection con = connectionClass.CONN2();

                String query = "INSERT INTO FunctionalCheck (invoice_no, goodsCode, checkpoints, instrument_used, sample_unit, sample1, sample2, sample3, sample4, sample5, sample6, sample7, sample8, sample9, sample10, minimum, average, maximum, lower_spec_limit, upper_spec_limit, judgement, MaterialCodeBoxSeqID) values ('"+ SapmpleActivityinlot.invoicenumholder+"', '"+SapmpleActivityinlot.materialholder+"', '"+Checkpoints+"','"+Instrumentused+"','"+Sampleunit+"','"+DC1+"','"+DC2+"','"+DC3+"','"+DC4+"','"+DC5+"','"+DC6+"','"+DC7+"','"+DC8+"','"+DC9+"','"+DC10+"','"+Min+"','"+Ave+"','"+Max+"','"+LowerSpec+"','"+UppperSpec+"','"+Judgmnt+"','"+SapmpleActivityinlot.boxseqholder+"')";
                //Toast.makeText(this, String.valueOf(sampleSizeFC), Toast.LENGTH_LONG).show();
                String query1 = "UPDATE SampleSize SET function_sample_size = '"+ Samplesize +"' WHERE MaterialCodeBoxSeqID = '"+ SapmpleActivityinlot.boxseqholder+"'";
                Statement stmt = con.createStatement();
                stmt.execute(query+query1);

                samplesizefc_id_hldr = Latest_ID("SampleSize");
                funcheck_id_hldr = Latest_ID("FunctionalCheck");

                Toast.makeText(getApplicationContext(), "Successfully added!", Toast.LENGTH_SHORT).show();



            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

}

