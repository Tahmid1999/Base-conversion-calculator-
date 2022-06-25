package com.example.basetobase;

import static java.lang.Math.pow;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputLayout from, to;
    AutoCompleteTextView spinner1, spinner2;
    TextInputEditText input, result;
    Button convertBtn;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from = (TextInputLayout) findViewById(R.id.from);
        spinner1 = (AutoCompleteTextView) findViewById(R.id.fromAutoCmpltTV);

        to = (TextInputLayout) findViewById(R.id.to);
        spinner2 = (AutoCompleteTextView) findViewById(R.id.toAutoCmpltTV);


        ArrayList<String> list=new ArrayList<String>();
        list.add("Bin");
        list.add("Oct");
        list.add("Dec");
        list.add("Hex");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list);
        spinner1.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner1.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner1.setSelection(0);
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner2.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner2.setSelection(0);
            }
        });

        //Button
        convertBtn=(Button) findViewById(R.id.convertBtn);
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = (TextInputEditText) findViewById(R.id.input);
                result = (TextInputEditText) findViewById(R.id.result);

                result.setText("");

                int bin = 2;
                int oct = 8;
                int dec = 10;
                int hex = 16;
                boolean check = true;
                int convertedTo = 0, convertedFrom = 0, placeOfPoint;
                ArrayList<Character> mainArray = new ArrayList<Character>();
                String inputFromUser;

                inputFromUser = input.getText().toString();

                if (spinner1.getText().toString().equalsIgnoreCase("Bin")) {
                    convertedFrom = 2;
                } else if (spinner1.getText().toString().equalsIgnoreCase("Oct")) {
                    convertedFrom = 8;
                } else if (spinner1.getText().toString().equalsIgnoreCase("Dec")) {
                    convertedFrom = 10;
                } else if (spinner1.getText().toString().equalsIgnoreCase("Hex")) {
                    convertedFrom = 16;
                }


                if (spinner2.getText().toString().equalsIgnoreCase("Bin")) {
                    convertedTo = 2;
                } else if (spinner2.getText().toString().equalsIgnoreCase("Oct")) {
                    convertedTo = 8;
                } else if (spinner2.getText().toString().equalsIgnoreCase("Dec")) {
                    convertedTo = 10;
                } else if (spinner2.getText().toString().equalsIgnoreCase("Hex")) {
                    convertedTo = 16;
                }

                if(convertedFrom==convertedTo){
                    Toast.makeText(getApplicationContext(),"You entered same base",Toast.LENGTH_SHORT).show();
                    spinner1.setSelection(0);
                }

                check=check_number(inputFromUser,convertedFrom);
                if(check==false){
                    if(convertedFrom==2) {
                        Toast.makeText(getApplicationContext(), "Your number is not  Binary", Toast.LENGTH_SHORT).show();
                        spinner1.setSelection(0);
                        return;
                    }

                    else if(convertedFrom==8) {
                        Toast.makeText(getApplicationContext(), "Your number is not  Octal", Toast.LENGTH_SHORT).show();
                        spinner1.setSelection(0);
                        return;
                    }

                    else if(convertedFrom==10) {
                        Toast.makeText(getApplicationContext(), "Your number is not  Decimal", Toast.LENGTH_SHORT).show();
                        spinner1.setSelection(0);
                        return;
                    }

                    else if(convertedFrom==16) {
                        Toast.makeText(getApplicationContext(), "Your number is not  Hexadecimal", Toast.LENGTH_SHORT).show();
                        spinner1.setSelection(0);
                        return;
                    }
                }


                for (char c : inputFromUser.toCharArray()) {
                    mainArray.add(c);
                }

                if (mainArray.contains('.')) {
                    placeOfPoint = mainArray.indexOf('.');
                    split_mainArray(mainArray, placeOfPoint, convertedFrom, convertedTo);
                } else {

                    base_To_base_decide_function(mainArray, convertedFrom, convertedTo);

                }
            }
        });


    }

    void split_mainArray(ArrayList<Character> mainArray,int placeOfPoint,int convertedFrom,int convertedTo) {

        ArrayList<Character> subArray_decPart=new ArrayList<>();
        ArrayList<Character> subArray_pointPart=new ArrayList<>();


        ///Populate Decimal part at subArray_decPart

        for(int i=0;i<placeOfPoint;i++){
            subArray_decPart.add(mainArray.get(i));
        }

        ///Populate Point part at subArray_pointPart

        for(int i=placeOfPoint+1;i<mainArray.size();i++){
            subArray_pointPart.add(mainArray.get(i));
        }

        ///Call function for which base to which base decide
        base_To_base_decide_function(subArray_decPart,subArray_pointPart,convertedFrom,convertedTo);

    }

    void base_To_base_decide_function(ArrayList<Character>subArray_decPart, ArrayList<Character> subArray_pointPart,int convertedFrom,int convertedTo){
        int bin=2;
        int oct=8;
        int dec=10;
        int hex=16;
        double dec_value;

        if(convertedFrom==bin && convertedTo==dec ){
            result.setText(String.valueOf(base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom)));
        }

        else if(convertedFrom==bin && convertedTo==oct){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);

        }

        else if(convertedFrom==bin && convertedTo==hex){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==dec && convertedTo==bin){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==dec && convertedTo==oct){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==dec && convertedTo==hex){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==oct && convertedTo==bin){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==oct && convertedTo==dec){
            result.setText(String.valueOf(base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom)));
        }


        else if(convertedFrom==oct && convertedTo==hex){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==hex && convertedTo==bin){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }

        else if(convertedFrom==hex && convertedTo==dec){
            result.setText(String.valueOf(base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom)));
        }

        else if(convertedFrom==hex && convertedTo==oct){
            dec_value=base_To_dec(subArray_decPart, subArray_pointPart,convertedFrom);
            dec_To_base(dec_value,convertedTo);
        }


    }

    void base_To_base_decide_function(ArrayList<Character>mainArray,int convertedFrom,int convertedTo){
        int bin=2;
        int oct=8;
        int dec=10;
        int hex=16;
        int dec_value;

        if(convertedFrom==bin && convertedTo==dec ){
            result.setText(String.valueOf(base_To_dec(mainArray,convertedFrom)));
        }

        else if(convertedFrom==bin && convertedTo==oct){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);

        }

        else if(convertedFrom==bin && convertedTo==hex){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);
        }

        else if(convertedFrom==dec && convertedTo==bin){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);

        }

        else if(convertedFrom==dec && convertedTo==oct){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);
        }

        else if(convertedFrom==dec && convertedTo==hex){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);

        }

        else if(convertedFrom==oct && convertedTo==bin){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);

        }

        else if(convertedFrom==oct && convertedTo==dec){
            result.setText(String.valueOf(base_To_dec(mainArray,convertedFrom)));
        }


        else if(convertedFrom==oct && convertedTo==hex){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);

        }

        else if(convertedFrom==hex && convertedTo==bin){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);
        }

        else if(convertedFrom==hex && convertedTo==dec){
            result.setText(String.valueOf(base_To_dec(mainArray,convertedFrom)));
        }

        else if(convertedFrom==hex && convertedTo==oct){
            dec_value=(int)base_To_dec(mainArray,convertedFrom);
            dec_To_base_withoutFraction(dec_value,convertedTo);

        }

    }



    double base_To_dec(ArrayList<Character>subArray_decPart,ArrayList<Character>subArray_pointPart,int convertedFrom){



        char charOfArray;
        int convertedCharToInt;
        double decimal=0,power=0;
        double final_Decimal,decimal_pointPart=0.0;
        int power_pointPart=-1;

///Convert Base to Decimal of decPart
        for(int i=subArray_decPart.size()-1;i!=-1;i--){

            charOfArray=subArray_decPart.get(i);
            if(charOfArray=='A') {convertedCharToInt=10; }
            else if(charOfArray=='B') {convertedCharToInt=11; }
            else if(charOfArray=='C') {convertedCharToInt=12; }
            else if(charOfArray=='D') {convertedCharToInt=13; }
            else if(charOfArray=='E') {convertedCharToInt=14; }
            else if(charOfArray=='F') {convertedCharToInt=15; }
            else{ convertedCharToInt=charOfArray-'0'; }
            decimal=decimal+((pow(convertedFrom,power))*convertedCharToInt);
            power++;
        }

///Convert Base to Decimal of pointPart
        for(int i=0;i<subArray_pointPart.size();i++){
            charOfArray=subArray_pointPart.get(i);
            if(charOfArray=='A') {convertedCharToInt=10; }
            else if(charOfArray=='B') {convertedCharToInt=11; }
            else if(charOfArray=='C') {convertedCharToInt=12; }
            else if(charOfArray=='D') {convertedCharToInt=13; }
            else if(charOfArray=='E') {convertedCharToInt=14; }
            else if(charOfArray=='F') {convertedCharToInt=15; }
            else{ convertedCharToInt=charOfArray-'0'; }
            decimal_pointPart=decimal_pointPart+((pow(convertedFrom,power_pointPart))*convertedCharToInt);
            power_pointPart--;
        }

///Combined decPart & pointPart
        final_Decimal=decimal+decimal_pointPart;

        return final_Decimal;
    }


    int base_To_dec(ArrayList<Character>mainArray,int convertedFrom){

        char charOfArray;
        int convertedCharToInt;
        double decimal=0,power=0;


///Convert Base to Decimal of decPart
        for(int i=mainArray.size()-1;i!=-1;i--){

            charOfArray=mainArray.get(i);
            System.out.println(charOfArray);
            if(charOfArray=='A') {convertedCharToInt=10; }
            else if(charOfArray=='B') {convertedCharToInt=11; }
            else if(charOfArray=='C') {convertedCharToInt=12; }
            else if(charOfArray=='D') {convertedCharToInt=13; }
            else if(charOfArray=='E') {convertedCharToInt=14; }
            else if(charOfArray=='F') {convertedCharToInt=15; }
            else{ convertedCharToInt=charOfArray-'0'; }
            decimal=decimal+((pow(convertedFrom,power))*convertedCharToInt);
            power++;
        }

        return (int)decimal;

    }

    void dec_To_base_withoutFraction(int dec_value,int convertedTo){



        ArrayList<Character>convertedTo_intPart=new ArrayList<Character>();
        ArrayList<Character>final_Part=new ArrayList<Character>();

        while(dec_value>0){
            int x=dec_value%convertedTo;
            if(x==10){convertedTo_intPart.add('A');}
            else if(x==11){convertedTo_intPart.add('B');}
            else if(x==12){convertedTo_intPart.add('C');}
            else if(x==13){convertedTo_intPart.add('D');}
            else if(x==14){convertedTo_intPart.add('E');}
            else if(x==15){convertedTo_intPart.add('F');}
            else{convertedTo_intPart.add((char)(x+'0'));}
            dec_value=dec_value/convertedTo;
        }

        for(int i=convertedTo_intPart.size()-1;i!=-1;i--){
            final_Part.add(convertedTo_intPart.get(i));
        }

        for(char c:final_Part){
            result.append(String.valueOf(c));
        }
    }

    void dec_To_base(double dec_value,int convertedTo){

        int int_Part=(int)dec_value;
        double int_Part_doubleCasting=int_Part;
        double pointPart=dec_value-int_Part_doubleCasting;
        int count=0;

        ArrayList<Character>convertedTo_intPart=new ArrayList<Character>();
        ArrayList<Character>convertedTo_pointPart=new ArrayList<Character>();
        ArrayList<Character>final_Part=new ArrayList<Character>();

        while(int_Part>0){
            int x=int_Part%convertedTo;
            if(x==10){convertedTo_intPart.add('A');}
            else if(x==11){convertedTo_intPart.add('B');}
            else if(x==12){convertedTo_intPart.add('C');}
            else if(x==13){convertedTo_intPart.add('D');}
            else if(x==14){convertedTo_intPart.add('E');}
            else if(x==15){convertedTo_intPart.add('F');}
            else{convertedTo_intPart.add((char)(x+'0'));}
            int_Part=int_Part/convertedTo;
        }

        for(int i=convertedTo_intPart.size()-1;i!=-1;i--){
            final_Part.add(convertedTo_intPart.get(i));
        }

        final_Part.add('.');

        while(true){
            dec_value=pointPart*convertedTo;
            int_Part=(int)dec_value;

            if(int_Part==10){convertedTo_pointPart.add('A');}
            else if(int_Part==11){convertedTo_pointPart.add('B');}
            else if(int_Part==12){convertedTo_pointPart.add('C');}
            else if(int_Part==13){convertedTo_pointPart.add('D');}
            else if(int_Part==14){convertedTo_pointPart.add('E');}
            else if(int_Part==15){convertedTo_pointPart.add('F');}
            else{convertedTo_pointPart.add((char)(int_Part+'0'));}
            int_Part_doubleCasting=int_Part;
            pointPart=dec_value-int_Part_doubleCasting;
            count++;

            if(pointPart == 0.0) {
                break;
            }
        }

        String my_rslt = "";

        for(int i=0;i<convertedTo_pointPart.size();i++){
            final_Part.add(convertedTo_pointPart.get(i));
        }

        for(int i = 0; i<final_Part.size(); i++){
            my_rslt = my_rslt + final_Part.get(i);
        }

        result.setText(my_rslt);
//        for(char c:final_Part){
//            result.append(String.valueOf(c));
//        }
    }

    boolean check_number(String inputFromUser,int convertedFrom){

        ArrayList<Character>x=new ArrayList<Character>();

        for (char c : inputFromUser.toCharArray()) {
            x.add(c);
        }
        for(int i=0;i<x.size();i++){
            if(convertedFrom==2){
                if((int)x.get(i)<46 || (int)x.get(i)==47  || (int)x.get(i)>49){
                    return false;
                }

            }
            else if(convertedFrom==8){
                if((int)x.get(i)<46 || (int)x.get(i)==47 || (int)x.get(i)>55){
                    return false;
                }
            }
            else if(convertedFrom==10){
                if((int)x.get(i)<46 || (int)x.get(i)==47  || (int)x.get(i)>57){
                    return false;
                }
            }
            else if(convertedFrom==16){
                if((int)x.get(i)<48 || ((int)x.get(i)>57 && (int)x.get(i)<65) || (int)x.get(i)>70 ){
                    return false;
                }
            }
        }
        return true;

    }

    
}
