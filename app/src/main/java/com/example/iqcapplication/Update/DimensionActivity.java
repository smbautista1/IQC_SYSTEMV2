package com.example.iqcapplication.Update;

import static com.example.iqcapplication.MainActivity.connectionClass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iqcapplication.ConnectionClass;
import com.example.iqcapplication.DatabaseHelper;
import com.example.iqcapplication.DimensionalActivity;
import com.example.iqcapplication.R;
import com.example.iqcapplication.SapmpleActivityinlot;
import com.example.iqcapplication.add.InspectionDetailsActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DimensionActivity extends AppCompatActivity {
    EditText dc_checkPoints,upperSpec,lowerSpec,sammpleUnit,dcsampleSize;

    TextView dc_Minimum,dc_Maximum,dc_Average,dc_Judgemen,dateToday,tvinvoice, invoiceupdimmm,goodsupdimchh;

    EditText dc1,dc2,dc3,dc4,dc5,dc6,dc7,dc8,dc9,dc10,dcdefectquant,encountered,dcdefectremarks, dcremarks;
    AutoCompleteTextView instrumentUsed;

    String id,instrumentUsedstring,  samplenum,  checkpoint,  samplUnit,  sample1,  sample2,  sample3,  sample4 ,  sample5,  sample6,
     sample7,  sample8,  sample9, sample10, lower,  upper,  max,  min,  average,  judgement,gdatedim,dcremarkss;
   public static String dcsamplesizeHolder,dccheckpointsholder,instrumenholder;
    public static int ctr = 1, samplesize_id_hldr=0, dimcheck_id_hldr = 0, sampleSizeDC = 0;
    public  static String judgeHolder = "PASSED", colorHolder = "#58f40b";
    public static boolean fourinstrument = false;


    Button addData,uploadtosqlite,backbutton,addDefectdc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimension);
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

        sammpleUnit = findViewById(R.id.sampleUnitup);
        addData = findViewById(R.id.Viewdatafc);

        //-----SAMPLE VALUES---
        dc1 = findViewById(R.id.dc1up);
        dc2 = findViewById(R.id.dc2up);
        dc3 = findViewById(R.id.dc3up);
        dc4 = findViewById(R.id.dc4up);
        dc5 = findViewById(R.id.dc5up);
        dc6 = findViewById(R.id.dc6up);
        dc7 = findViewById(R.id.dc7up);
        dc8 = findViewById(R.id.dc8up);
        dc9 = findViewById(R.id.dc9up);
        dc10 = findViewById(R.id.dc10up);

        instrumentUsed = findViewById(R.id.instrumentUsedup);
        dcdefectquant = findViewById(R.id.dimensionfcQuantitiy);
        encountered  = findViewById(R.id.dcencountered);
        dcdefectremarks = findViewById(R.id.DimensionRemarksdefect);
        addDefectdc = findViewById(R.id.adddefectdc);
        dcremarks = findViewById(R.id.remarkdcup);

        //-----MIN MAX AVERAGE
        dc_Minimum = findViewById(R.id.minimumup);
        dc_Maximum = findViewById(R.id.maximumup);
        dc_Average = findViewById(R.id.average_txtup);
        dc_Judgemen = findViewById(R.id.dcjudgeMentup);
        //--- upper and lower specs
        upperSpec = findViewById(R.id.upperspecsup);
        lowerSpec = findViewById(R.id.lowerspecup);
        dc_checkPoints = findViewById(R.id.checkPointup);
        dcsampleSize = findViewById(R.id.sampleSizedc_up);
        dateToday  = findViewById(R.id.datedimup);
        addData = findViewById(R.id.Viewdatafc);
        tvinvoice = findViewById(R.id.textView_invoiceno);
        uploadtosqlite = findViewById(R.id.uploadfcDatafc);
        backbutton = findViewById(R.id.backbuttondim);

        goodsupdimchh = findViewById(R.id.goodsupdimch);
        invoiceupdimmm = findViewById(R.id.invoicedimm);


        dc_Judgemen.setEnabled(false);

        upperSpec();

        getIntentData();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DimensionActivity.this, DimensionalActivity.class);
                dcsamplesizeHolder = dcsampleSize.getText().toString();
               // dccheckpointsholder = dc_checkPoints.getText().toString();
                instrumenholder = instrumentUsed.getText().toString();
               // insert_sampleSize();
                startActivity(intent);
            }
        });

        addDefectdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dcdefectquant.length()==0){
                    dcdefectquant.setError("Enter Defect qty");
                }
                // ERROR MESSAGE FOR TOTAL QUUANTITY
                else if(encountered.length()==0){
                    encountered.setError("Enter Defect Encountered");
                }
                else
                insert_defect_DM();
            }
        });


        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog1();

            }
        });

        uploadtosqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });


        judgeHolder = "PASSED";
        colorHolder = "#58f40b";

        if(upperSpec.getText().toString().equals("")){
            dc_Judgemen.setText("");
            dc1.setTextColor(Color.parseColor("#000000"));
            dc2.setTextColor(Color.parseColor("#000000"));
            dc3.setTextColor(Color.parseColor("#000000"));
            dc4.setTextColor(Color.parseColor("#000000"));
            dc5.setTextColor(Color.parseColor("#000000"));
            dc6.setTextColor(Color.parseColor("#000000"));
            dc7.setTextColor(Color.parseColor("#000000"));
            dc8.setTextColor(Color.parseColor("#000000"));
            dc9.setTextColor(Color.parseColor("#000000"));
            dc10.setTextColor(Color.parseColor("#000000"));
        }
        else{

            float Lspec = 0;
            float Uspec = 0;

            try{
                Lspec = Float.parseFloat(lowerSpec.getText().toString());
                Uspec = Float.parseFloat(upperSpec.getText().toString());
            }
            catch (Exception ex){
                Toast.makeText(DimensionActivity.this, "Input Valid Value", Toast.LENGTH_SHORT).show();
            }

            if (!dc1.getText().toString().equals(""))
            {
                try {
                    num1 = Float.parseFloat(dc1.getText().toString());
                }
                catch (Exception ex)
                {

                    Toast.makeText(getApplicationContext(), ex.toString()  , Toast.LENGTH_LONG).show();

                }


                if (num1 < Lspec || num1 > Uspec)
                {

                    judgeHolder = "FAILED";
                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc1.setTextColor(Color.parseColor("#FF0000"));

                }else{
                    dc_Judgemen.setText(judgeHolder);

                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc1.setTextColor(Color.parseColor("#58f40b"));
                }
            }

            if (!dc2.getText().toString().equals("")) {

                try {
                    num2 = Float.parseFloat(dc2.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

                if (num2 < Lspec || num2 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);

                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc2.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    dc_Judgemen.setText(judgeHolder);

                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc2.setTextColor(Color.parseColor("#58f40b"));
                }

            }

            if (!dc3.getText().toString().equals("")) {
                try {
                    num3 = Float.parseFloat(dc3.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }



                if (num3 < Lspec || num3 > Uspec)
                {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);

                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc3.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc3.setTextColor(Color.parseColor("#58f40b"));
                }

            }

            if (!dc4.getText().toString().equals("")) {
                try{
                    num4 = Float.parseFloat(dc4.getText().toString());
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num4 < Lspec || num4 > Uspec)
                {

                    judgeHolder = "FAILED";
                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);

                    dc4.setTextColor(Color.parseColor("#FF0000"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc4.setTextColor(Color.parseColor("#58f40b"));
                }
            }

            if (!dc5.getText().toString().equals("")) {
                try{

                    num5 = Float.parseFloat(dc5.getText().toString());

                }catch (Exception e){

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num5 < Lspec || num5 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);

                    dc5.setTextColor(Color.parseColor(colorHolder));
                    dc_Judgemen.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc5.setTextColor(Color.parseColor("#58f40b"));

                }

            }

            if (!dc6.getText().toString().equals("")) {

                try{
                    num6 = Float.parseFloat(dc6.getText().toString());
                }catch (Exception e ){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num6 < Lspec || num6 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);
                    dc6.setTextColor(Color.parseColor(colorHolder));
                    dc_Judgemen.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc6.setTextColor(Color.parseColor("#58f40b"));
                }
            }

            if (!dc7.getText().toString().equals(""))
            {
                try{
                    num7 = Float.parseFloat(dc7.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num7 < Lspec || num7 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);
                    dc7.setTextColor(Color.parseColor("#FF0000"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                    dc7.setTextColor(Color.parseColor("#58f40b"));
                }
            }

            if (!dc8.getText().toString().equals("")) {

                try{
                    num8 = Float.parseFloat(dc8.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num8 < Lspec || num8 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);
                    dc8.setTextColor(Color.parseColor("#FF0000"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc8.setTextColor(Color.parseColor("#58f40b"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }
            }

            if (!dc9.getText().toString().equals("")) {

                try{
                    num9 = Float.parseFloat(dc9.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num9 < Lspec || num9 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);
                    dc9.setTextColor(Color.parseColor("#FF0000"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc9.setTextColor(Color.parseColor("#58f40b"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }
            }

            if (!dc10.getText().toString().equals("")) {

                try{
                    num10 = Float.parseFloat(dc10.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


                if (num10 < Lspec || num10 > Uspec) {
                    judgeHolder = "FAILED";

                    colorHolder = "#FF0000";
                    dc_Judgemen.setText(judgeHolder);
                    dc10.setTextColor(Color.parseColor("#FF0000"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }else{
                    dc_Judgemen.setText(judgeHolder);
                    dc10.setTextColor(Color.parseColor("#58f40b"));
                    dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                }
            }


        }

    }


    public void upperSpec(){


        upperSpec.addTextChangedListener(new TextWatcher() {
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                judgeHolder = "PASSED";
                colorHolder = "#58f40b";

                if(upperSpec.getText().toString().equals("")){
                    judgeHolder = "PASSED";
                    colorHolder = "#58f40b";

                    dc_Judgemen.setText("");
                    dc1.setTextColor(Color.parseColor("#000000"));
                    dc2.setTextColor(Color.parseColor("#000000"));
                    dc3.setTextColor(Color.parseColor("#000000"));
                    dc4.setTextColor(Color.parseColor("#000000"));
                    dc5.setTextColor(Color.parseColor("#000000"));
                    dc6.setTextColor(Color.parseColor("#000000"));
                    dc7.setTextColor(Color.parseColor("#000000"));
                    dc8.setTextColor(Color.parseColor("#000000"));
                    dc9.setTextColor(Color.parseColor("#000000"));
                    dc10.setTextColor(Color.parseColor("#000000"));
                }
                else{

                    float Lspec = 0;
                    float Uspec = 0;

                    try{
                        Lspec = Float.parseFloat(lowerSpec.getText().toString());
                        Uspec = Float.parseFloat(upperSpec.getText().toString());
                    }
                    catch (Exception ex){
                        Toast.makeText(DimensionActivity.this, "Input Valid Value", Toast.LENGTH_SHORT).show();
                    }

                    if (!dc1.getText().toString().equals(""))
                    {
                        try {
                            num1 = Float.parseFloat(dc1.getText().toString());
                        }
                        catch (Exception ex)
                        {

                            Toast.makeText(getApplicationContext(), ex.toString()  , Toast.LENGTH_LONG).show();

                        }


                        if (num1 < Lspec || num1 > Uspec)
                        {

                            judgeHolder = "FAILED";
                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc1.setTextColor(Color.parseColor("#FF0000"));

                        }else{
                            dc_Judgemen.setText(judgeHolder);

                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc1.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!dc2.getText().toString().equals("")) {

                        try {
                            num2 = Float.parseFloat(dc2.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }

                        if (num2 < Lspec || num2 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);

                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc2.setTextColor(Color.parseColor("#FF0000"));
                        } else {
                            dc_Judgemen.setText(judgeHolder);

                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc2.setTextColor(Color.parseColor("#58f40b"));
                        }

                    }

                    if (!dc3.getText().toString().equals("")) {
                        try {
                            num3 = Float.parseFloat(dc3.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }



                        if (num3 < Lspec || num3 > Uspec)
                        {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);

                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc3.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc3.setTextColor(Color.parseColor("#58f40b"));
                        }

                    }

                    if (!dc4.getText().toString().equals("")) {
                        try{
                            num4 = Float.parseFloat(dc4.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num4 < Lspec || num4 > Uspec)
                        {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);

                            dc4.setTextColor(Color.parseColor("#FF0000"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc4.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!dc5.getText().toString().equals("")) {
                        try{
                            num5 = Float.parseFloat(dc5.getText().toString());
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num5 < Lspec || num5 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);

                            dc5.setTextColor(Color.parseColor(colorHolder));
                            dc_Judgemen.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc5.setTextColor(Color.parseColor("#58f40b"));

                        }

                    }

                    if (!dc6.getText().toString().equals("")) {

                        try{
                            num6 = Float.parseFloat(dc6.getText().toString());
                        }catch (Exception e ){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num6 < Lspec || num6 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);
                            dc6.setTextColor(Color.parseColor(colorHolder));
                            dc_Judgemen.setTextColor(Color.parseColor("#FF0000"));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc6.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!dc7.getText().toString().equals(""))
                    {
                        try{
                            num7 = Float.parseFloat(dc7.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num7 < Lspec || num7 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);
                            dc7.setTextColor(Color.parseColor("#FF0000"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                            dc7.setTextColor(Color.parseColor("#58f40b"));
                        }
                    }

                    if (!dc8.getText().toString().equals("")) {

                        try{
                            num8 = Float.parseFloat(dc8.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num8 < Lspec || num8 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);
                            dc8.setTextColor(Color.parseColor("#FF0000"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc8.setTextColor(Color.parseColor("#58f40b"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }
                    }

                    if (!dc9.getText().toString().equals("")) {

                        try{
                            num9 = Float.parseFloat(dc9.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num9 < Lspec || num9 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);
                            dc9.setTextColor(Color.parseColor("#FF0000"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc9.setTextColor(Color.parseColor("#58f40b"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }
                    }

                    if (!dc10.getText().toString().equals("")) {

                        try{
                            num10 = Float.parseFloat(dc10.getText().toString());
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }


                        if (num10 < Lspec || num10 > Uspec) {
                            judgeHolder = "FAILED";

                            colorHolder = "#FF0000";
                            dc_Judgemen.setText(judgeHolder);
                            dc10.setTextColor(Color.parseColor("#FF0000"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }else{
                            dc_Judgemen.setText(judgeHolder);
                            dc10.setTextColor(Color.parseColor("#58f40b"));
                            dc_Judgemen.setTextColor(Color.parseColor(colorHolder));
                        }
                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UPDATE" + "DATA" + "?");
        builder.setMessage("Are you sure you want to UPDATE?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateDatainSQlite();
                updateData();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


    void confirmDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UPDATE" + "DATA" + "?");
        builder.setMessage("Are you sure you want to UPLOAD? this process can't be undone");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insert_dimcheck();



            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }




    public void insert_sampleSize(){

        try{
            connectionClass = new ConnectionClass();
            Connection con = connectionClass.CONN2();
            String query1 = "INSERT INTO SampleSize (dimension_sample_size, goodsCode,invoice_number,MaterialCodeBoxSeqID) values ('"+dcsampleSize.getText().toString()+"', '"+SapmpleActivityinlot.materialholder+"', '"+SapmpleActivityinlot.invoicenumholder+"', '"+SapmpleActivityinlot.boxseqholder+"')";
            Statement stmt = con.createStatement();
            stmt.execute(query1);
            samplesize_id_hldr = Latest_ID("SampleSize");
            dimcheck_id_hldr = Latest_ID("DimensionalCheck");
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    public void updateDatainSQlite(){
        try{
            DatabaseHelper myDB = new DatabaseHelper(DimensionActivity.this);

            instrumentUsedstring =    instrumentUsed.getText().toString().trim();
            samplenum  = dcsampleSize.getText().toString().trim();
            checkpoint = dc_checkPoints.getText().toString().trim();
            samplUnit  = sammpleUnit.getText().toString().trim();

            sample1 = dc1.getText().toString().trim();
            sample2 = dc2.getText().toString().trim();
            sample3 = dc3.getText().toString().trim();
            sample4 = dc4.getText().toString().trim();
            sample5 = dc5.getText().toString().trim();
            sample6 = dc6.getText().toString().trim();
            sample7 = dc7.getText().toString().trim();
            sample8 = dc8.getText().toString().trim();
            sample9 = dc9.getText().toString().trim();
            sample10 = dc10.getText().toString().trim();

            lower = lowerSpec.getText().toString().trim();
            upper = upperSpec.getText().toString().trim();

            min = dc_Minimum.getText().toString().trim();
            average = dc_Average.getText().toString().trim();
            max = dc_Maximum.getText().toString().trim();

            judgement = dc_Judgemen.getText().toString().trim();
            gdatedim = dateToday.getText().toString().trim();
            dcremarkss  = dcremarks.getText().toString();

                    myDB.updateDc( id, instrumentUsedstring, samplenum,  checkpoint,  samplUnit,  sample1,  sample2,  sample3,  sample4 ,  sample5,  sample6,
                             sample7,  sample8,  sample9, sample10, lower,  upper,  max,  min,  average,  judgement,gdatedim, dcremarkss);

            Toast.makeText(DimensionActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void insert_dimcheck()
    {
        String Checkpoints = dc_checkPoints.getText().toString();
        String Instrumentused = instrumentUsed.getText().toString();
        String Sampleunit = sammpleUnit.getText().toString();
        String DC1 = dc1.getText().toString();
        String DC2 = dc2.getText().toString();
        String DC3 = dc3.getText().toString();
        String DC4 = dc4.getText().toString();
        String DC5 = dc5.getText().toString();
        String DC6 = dc6.getText().toString();
        String DC7 = dc7.getText().toString();
        String DC8 = dc8.getText().toString();
        String DC9 = dc9.getText().toString();
        String DC10 = dc10.getText().toString();
        String Min = dc_Minimum.getText().toString();
        String Ave = dc_Average.getText().toString();
        String Max = dc_Maximum.getText().toString();
        String LowerSpec = lowerSpec.getText().toString();
        String UppperSpec = upperSpec.getText().toString();
        String Judgmnt = dc_Judgemen.getText().toString();



        if (Checkpoints.trim().equals("")||Instrumentused.trim().equals(""))//dagdagan mo

        {
            Toast.makeText(getApplicationContext(), "Must input atleast 1 checkpoint!", Toast.LENGTH_LONG).show();
        }

        else {
            try {
                connectionClass = new ConnectionClass();
                Connection con = connectionClass.CONN2();
                String query = "SELECT * FROM DimensionalCheck WHERE Date =  '"+ dateToday.getText().toString() +"' ";
                PreparedStatement stmtt = con.prepareStatement(query);
                ResultSet rs = stmtt.executeQuery();
                if(rs.next()){

                     // String time = rs.getString("Date");
                    Toast.makeText(DimensionActivity.this, "Already Existing in our database ", Toast.LENGTH_SHORT).show();
                }else{
                    String query2 = "INSERT INTO DimensionalCheck (invoice_no, goodsCode, checkpoints, instrument_used, sample_unit, sample1, sample2, sample3, sample4, sample5, sample6, sample7, sample8, sample9, sample10, minimum, average, maximum, lower_spec_limit, upper_spec_limit, judgement,MaterialCodeBoxSeqID, Date)" +
                            " values ('"+SapmpleActivityinlot.invoicenumholder+"', '"+ SapmpleActivityinlot.goodscodeholder+"', '"+Checkpoints+"','"+Instrumentused+"','"+Sampleunit+"','"+DC1+"','"+DC2+"','"+DC3+"','"+DC4+"','"+DC5+"','"+DC6+"','"+DC7+"','"+DC8+"','"+DC9+"','"+DC10+"','"+Min+"','"+Ave+"','"+Max+"','"+LowerSpec+"','"+UppperSpec+"','"+Judgmnt+"','"+SapmpleActivityinlot.boxseqholder+"', '"+dateToday.getText().toString()+"')"   ;

                    Statement stmt = con.createStatement();
                    stmt.execute(query2);
                    dimcheck_id_hldr = Latest_ID("DimensionalCheck");

                    Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();

                }



            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

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


    public void getIntentData(){
        if(getIntent().hasExtra("id") &&  getIntent().hasExtra("getInstrumentUsed") && getIntent().hasExtra("getCheckpointdc") &&  getIntent().hasExtra("getSampleUnit") &&
                getIntent().hasExtra("getSampleSize") &&  getIntent().hasExtra("getSample1dc") &&
                getIntent().hasExtra("getSample2dc") &&  getIntent().hasExtra("getSample3dc") &&  getIntent().hasExtra("getSample4dc") &&   getIntent().hasExtra("getSample5dc") &&
                getIntent().hasExtra("getSample6dc") &&  getIntent().hasExtra("getSample7dc") &&  getIntent().hasExtra("getSample8dc") &&  getIntent().hasExtra("getSample9dc") &&
                getIntent().hasExtra("getSample10dc") &&  getIntent().hasExtra("getLoweSpecs") &&   getIntent().hasExtra("getUpperSpecs") &&
        getIntent().hasExtra("getMinimum") &&   getIntent().hasExtra("getAverage") &&  getIntent().hasExtra("getMaximum") &&  getIntent().hasExtra("getJudgement") &&  getIntent().hasExtra("getdatedim")
        ){


            id   = getIntent().getStringExtra("id");
            instrumentUsedstring = getIntent().getStringExtra("getInstrumentUsed");
            checkpoint   = getIntent().getStringExtra("getCheckpointdc");
            samplUnit   = getIntent().getStringExtra("getSampleUnit");
            samplenum   = getIntent().getStringExtra("getSampleSize");

            sample1   = getIntent().getStringExtra("getSample1dc");
            sample2   = getIntent().getStringExtra("getSample2dc");
            sample3   = getIntent().getStringExtra("getSample3dc");
            sample4   = getIntent().getStringExtra("getSample4dc");
            sample5   = getIntent().getStringExtra("getSample5dc");

            sample6   = getIntent().getStringExtra("getSample6dc");
            sample7   = getIntent().getStringExtra("getSample7dc");
            sample8   = getIntent().getStringExtra("getSample8dc");
            sample9   = getIntent().getStringExtra("getSample9dc");
            sample10   = getIntent().getStringExtra("getSample10dc");

            upper   = getIntent().getStringExtra("getUpperSpecs");
            lower   = getIntent().getStringExtra("getLoweSpecs");
            min   = getIntent().getStringExtra("getMinimum");
            average   = getIntent().getStringExtra("getAverage");
            max   = getIntent().getStringExtra("getMaximum");
            judgement   = getIntent().getStringExtra("getJudgement");
            gdatedim = getIntent().getStringExtra("getdatedim");


            sammpleUnit.setText(samplUnit);
            dcsampleSize.setText(samplenum);
            dcsamplesizeHolder = dcsampleSize.getText().toString();
            dccheckpointsholder = dc_checkPoints.getText().toString();
            instrumenholder = instrumentUsed.getText().toString();

            instrumentUsed.setText(instrumentUsedstring);
            dc_checkPoints.setText(checkpoint);

            dc1.setText(sample1);
            dc2.setText(sample2);
            dc3.setText(sample3);
            dc4.setText(sample4);
            dc5.setText(sample5);
            dc6.setText(sample6);
            dc7.setText(sample7);
            dc8.setText(sample8);
            dc9.setText(sample9);
            dc10.setText(sample10);
            lowerSpec.setText(lower);
            upperSpec.setText(upper);
            dc_Minimum.setText(min);
            dc_Maximum.setText(max);
            dc_Average.setText(average);

            dc_Judgemen.setText(judgement);

            dateToday.setText(gdatedim);
        }
    }

    void updateData(){
        try{
            connectionClass = new ConnectionClass();
            Connection con = connectionClass.CONN2();

            String query = " UPDATE DimensionalCheck SET invoice_no = '" + invoiceupdimmm.getText().toString() + "', checkpoints = '"+dc_checkPoints.getText().toString()+ "'," +
                    " instrument_used = '"+instrumentUsed.getText().toString()+ "', sample_unit = '"+sammpleUnit.getText().toString()+ "'," +
                    " sample1 = '"+dc1.getText().toString()+ "', sample2 = '"+dc2.getText().toString()+ "', sample3 = '"+dc3.getText().toString()+ "'," +
                    " sample4 = '"+dc4.getText().toString()+ "', sample5 = '"+dc5.getText().toString()+ "', sample6 = '"+dc6.getText().toString()+ "', sample7 = '"+dc7.getText().toString()+ "', sample8 = '"+dc8.getText().toString()+ "', sample9 = '"+dc9.getText().toString()+ "'," +
                    "sample10 = '"+dc10.getText().toString()+ "',  minimum = '"+dc_Minimum.getText().toString()+ "',  average = '"+dc_Average.getText().toString()+ "',  " +
                    "maximum = '"+dc_Maximum.getText().toString()+" ',  lower_spec_limit = '"+lowerSpec.getText().toString()+" ',  upper_spec_limit = '"+upperSpec.getText().toString()+" ', " +
                    "judgement = '"+dc_Judgemen.getText().toString()+" ', WHERE  MaterialCodeBoxSeqID = '" + InspectionDetailsActivity.boxseqholder+ "'";
            Statement stmt =  con.createStatement();
            stmt.execute(query);

            Toast.makeText(getApplicationContext(),"Successfully updated!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
        finally {

        }
    }



    public void insert_defect_DM() {
        String Quantity = dcdefectquant.getText().toString();
        String Encountered = encountered.getText().toString();

        if (Quantity.trim().equals("") || Encountered.trim().equals(""))
            Toast.makeText(getApplicationContext(),"Fillup required fields!", Toast.LENGTH_LONG).show();
        else {

            try {
                connectionClass = new ConnectionClass();
                Connection con = connectionClass.CONN2();

                String query = " UPDATE DimensionalCheck SET defectqty = '" + dcdefectquant.getText().toString() + "', defect_enc = '"+encountered.getText().toString()+ "' WHERE invoice_no = '" + InspectionDetailsActivity.invoicenumholderr + "' AND id = '" + DimensionalActivity.dimcheck_id_hldr + "'";
                Statement stmt =  con.createStatement();
                stmt.execute(query);

                Toast.makeText(getApplicationContext(),"Successfully updated!", Toast.LENGTH_SHORT).show();


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),ex.toString(), Toast.LENGTH_LONG).show();

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}