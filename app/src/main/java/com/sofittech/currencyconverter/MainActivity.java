package com.sofittech.currencyconverter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ImageView swap;
    MaterialSpinner spinner,spinner1;
    String result;
    EditText convertedCurrency;
    TextView getConvertedCurrency;
    TextView getRate;
    ArrayList<String> ITEMS = new ArrayList<>();
    ArrayList<String> abbrivations= new ArrayList<>();
    ArrayList<Currency> currencies;
    JSONObject jsonObject;
    public String to, from,value;
    public Button button;
    String getResult;
    String resultt;
    public OkHttpClient client;
    String cRate,rRate;
    Typeface typeface;

    /*{"AUD","BGN","BRL","CAD","CHF", "CNY", "CZK","DKK", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "JPY",
            "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "ZAR", "EUR"};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        typeface= Typeface.createFromAsset(getAssets(),"GothamRoundedMedium.otf");
        swap= (ImageView)findViewById(R.id.swap);
        spinner=(MaterialSpinner)findViewById(R.id.firstCurrency);
        spinner1=(MaterialSpinner)findViewById(R.id.SecCurrency);
        button=(Button)findViewById(R.id.button);
        currencies= new ArrayList<>();

        convertedCurrency=(EditText)findViewById(R.id.converted);
        getConvertedCurrency=(TextView)findViewById(R.id.getconverted);
        getRate=(TextView)findViewById(R.id.rate);
        swap.setImageResource(R.drawable.swap);

        convertedCurrency.setText("");
        
        convertedCurrency.setTypeface(typeface);
        button.setTypeface(typeface);
        getConvertedCurrency.setTypeface(typeface);
        getRate.setTypeface(typeface);

        convertedCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        getRate.setTypeface(typeface);

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int spinner1Index = spinner.getSelectedItemPosition();
               // int s= spinner1.getSelectedItemPosition();

                spinner.setSelection(spinner1.getSelectedItemPosition());
                spinner1.setSelection(spinner1Index );

                //spinner1.setSelection(spinner.getSelectedItemPosition());
                //spinner.setSelection(s);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                value=convertedCurrency.getText().toString();
                if(value.length()>=1){
                    getConvertedCurrencyReturn(to,from,value);
                }else {
                    Toast.makeText(MainActivity.this,"Please Enter Value",Toast.LENGTH_LONG).show();
                }

            }
        });


         client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://currencyconvertersofit.herokuapp.com/getCountries")
                .build();
        //http://api.fixer.io/latest?base=USD&symbols=EUR
        //http://api.fixer.io/latest
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result=response.body().string();
                Log.e("Hello",result);

                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject jsonObject1=jsonObject.getJSONObject("rates");
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i=0;i<jsonArray.length();i++){

                        jsonObject= jsonArray.getJSONObject(i);
                        String name =jsonObject.getString("name");
                        String currency = jsonObject.getString("currency");

                        Currency currency1= new Currency(jsonObject.getString("name"),jsonObject.getString("currency"));                        currencies.add(currency1);
                       // Log.i("Name",name);
                        //Log.i("Currency",currency);
                    }
                    Log.e("Currency size", String.valueOf(currencies.size()));

                    for(int j=0; j<currencies.size();j++){
                        ITEMS.add(currencies.get(j).name);
                        abbrivations.add(currencies.get(j).currency);

                    }
                    Log.e("Currency size ITems", String.valueOf(ITEMS.size()));
                    Log.e("Currency size abv", String.valueOf(abbrivations.size()));




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,ITEMS)


                            {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent)
                                {
                                    View v = super.getView(position, convertView, parent);
                                    Typeface externalFont=Typeface.createFromAsset(getAssets(), "GothamRoundedMedium.otf");
                                    ((TextView) v).setTypeface(externalFont);
                                    return v;
                                }

                                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                                    View v =super.getDropDownView(position, convertView, parent);

                                    Typeface externalFont=Typeface.createFromAsset(getAssets(), "GothamRoundedMedium.otf");
                                    ((TextView) v).setTypeface(externalFont);
//                                    v.setBackgroundColor(Color.GREEN);

                                    return v;
                                }

                            };


                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner.setAdapter(arrayAdapter);
                            spinner1.setAdapter(arrayAdapter);

                        }
                    });

//                    Log.e("Names", jsonObject1.toString());
//                    Log.e("Length", String.valueOf(jsonObject1.length()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("Name to",abbrivations.get(i));

                to=abbrivations.get(i);
                cRate=to;
//                if(ITEMS.get(i).contains("United States")){
//                    String CurrentString = abbrivations.get(i);
//                    String[] separated = CurrentString.split(",");
//                        to=separated[0];
//                    Log.e("Name",to);
//                    Log.e("Name to",ITEMS.get(i));
//                }else{
//                    to=abbrivations.get(i);
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from=abbrivations.get(i);

                rRate=from;

                Log.e("rRate",rRate);
                Log.e("Name from",abbrivations.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    private void getConvertedCurrencyReturn(String to,String from,String value) {
        Log.e("Result **", "HAHAHAH");
        RequestBody formBody = new FormBody.Builder()
                .add("to", to).add("from",from).add("value",value)
                .build();
        final Request request = new Request.Builder()
                .url("http://currencyconvertersofit.herokuapp.com/converter")
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Result **", String.valueOf(e));
                        Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
            String rate;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getResult=response.body().string();
                Log.e("getresult ****",getResult);
                try {
                    JSONObject jsonObject = new JSONObject(getResult);

                    resultt=jsonObject.getString("result");
                    rate=jsonObject.getString("rate");
                    Log.e("resultt ***",resultt);
                    Log.e("rate ***",rate);

                  //  Log.e("Json Result",result);
                    //JSONArray jsonArray = new JSONArray(getResult);
//                    for(int i=0;i<jsonArray.length();i++){
//                        jsonObject2=jsonArray.getJSONObject(i);
//
//                        resultt= jsonObject2.getString("result");
//                        rate=jsonObject2.getString("rate");
//
//
//                        Log.e("resultt ***",resultt);
//                        Log.e("rate ***",rate);
//                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getConvertedCurrency.setText(resultt);
                        getRate.setText("1"+" "+rRate+" "+"="+" "+rate+" "+cRate);
                       // Log.e("Get Result Converted",resultt);
                    }
                });

            }

        });
    }





}
