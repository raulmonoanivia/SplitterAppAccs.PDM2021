package com.example.splitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    TextToSpeech ttsPlayer;
    FloatingActionButton share, tts;
    EditText oper1, oper2;
    TextView tvRes;
    int operacao=0; //Somar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Chamando o código da Activty mãe
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main); //Setando o Layout a ser carregado

        //Acessando os componentes
        tvRes= (TextView) findViewById(R.id.tV_res);

        oper1= (EditText) findViewById(R.id.edT_oper1);
        oper1.addTextChangedListener(this);
        oper2= (EditText) findViewById(R.id.edT_oper2);
        oper2.addTextChangedListener(this);

        share= (FloatingActionButton) findViewById(R.id.btShare);
        share.setOnClickListener(this);

        tts= (FloatingActionButton) findViewById(R.id.btTts);
        tts.setOnClickListener(this);


        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1122){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlayer = new TextToSpeech(this, this);

            }else{
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        try {
            double op1 = Double.parseDouble(oper1.getText().toString());
            double op2 = Double.parseDouble(oper2.getText().toString());
            double res = (op1/op2);
            DecimalFormat df= new DecimalFormat("0.00");
            if (op2==0){
                tvRes.setText(R.string.has_to_pay);
            }else{
                tvRes.setText(getString(R.string.currency)+" "+ df.format(res));
            }

        } catch (Exception e) {
            tvRes.setText(getString(R.string.currency)+" "+getString(R.string.zerozerozero));
        }

    }

    @Override
    public void onClick(View view) {
        if (view==share){
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.owe)+tvRes.getText().toString());
            startActivity(Intent.createChooser(intent, getString(R.string.where_share)));
        }

        if (view==tts){
            if (ttsPlayer!=null){
                ttsPlayer.speak(getString(R.string.ind_value) + tvRes.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS){
            Toast.makeText(this, getString(R.string.activate_tts),
                Toast.LENGTH_LONG).show();}
        else if (initStatus == TextToSpeech.ERROR){
            Toast.makeText(this, getString(R.string.deactivate_tts),
                    Toast.LENGTH_LONG).show();
        }

    }
}