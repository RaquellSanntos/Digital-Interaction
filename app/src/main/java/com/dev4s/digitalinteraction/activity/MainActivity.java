package com.dev4s.digitalinteraction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.dev4s.digitalinteraction.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Vibrator vibrator;
    private FirebaseAuth autenticacao;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private TextInputLayout inputNome, inputComplemento, inputCep, inputLogradouro, inputBairro
            ,inputLocalidade,inputUf;
    private AppCompatEditText campoNome, campoCEP, campoLogradouro, campoComplemento,
            campoBairro, campoLocalidade, campoUF;
    private Button btBuscarCEP, btSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNome = findViewById(R.id.inputNome);
        inputComplemento =findViewById(R.id.inputComplemento);
        inputCep =findViewById(R.id.inputLayoutCEP);
        inputLogradouro =findViewById(R.id.inputLayoutLogradouro);
        inputBairro =findViewById(R.id.inputLayoutBairro);
        inputLocalidade =findViewById(R.id.inputLayoutLocalidade);
        inputUf =findViewById(R.id.inputLayoutUF);

        campoNome = findViewById(R.id.editNome);
        campoCEP = findViewById(R.id.editCEP);
        campoLogradouro = findViewById(R.id.editLogradouro);
        campoComplemento = findViewById(R.id.editComplemento);
        campoBairro = findViewById(R.id.editBairro);
        campoLocalidade = findViewById(R.id.editLocalidade);
        campoUF = findViewById(R.id.editUF);

        btBuscarCEP = findViewById(R.id.btPesquisaCEP);
        btBuscarCEP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.MyTaskCep task = new MainActivity.MyTaskCep();
                String cep = campoCEP.getText().toString();
                String urlCep = "https://viacep.com.br/ws/" + cep + "/json/";
                task.execute(urlCep);
                validarForm();
            }
        });
        btSalvar = findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarForm();
                // btSalvar.setEnabled(false);// DESATIVA O BOTÃO SALVAR DEPOIS QUE CLICADO A PRIMEIRA VEZ



                long pattern[] = { 0, 100, 200 };
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, -1);
            }
        });
    }

    public void validarForm(){

        if(campoNome.getText().toString().isEmpty()){
            inputNome.setErrorEnabled(true);
            inputNome.setError("Preencha acima: seu nome!");
        }else{ inputNome.setErrorEnabled(false); }

        if (campoComplemento.getText().toString().isEmpty()){
            inputComplemento.setErrorEnabled(true);
            inputComplemento.setError("Preencha acima: número, apto ou quadra");
        }else {
            inputComplemento.setErrorEnabled(false);
        }
        if (campoCEP.getText().toString().isEmpty()){
            inputCep.setErrorEnabled(true);
            inputCep.setError("Precisa Buscar o CEP");
        }else {
            inputCep.setErrorEnabled(false);

        }
        if (campoLogradouro.getText().toString().isEmpty()){
            inputLogradouro.setErrorEnabled(true);
            inputLogradouro.setError("Precisa validar o CEP!");
        }else {
            inputLogradouro.setErrorEnabled(false);
        }
        if (campoBairro.getText().toString().isEmpty()){
            inputBairro.setErrorEnabled(true);
            inputBairro.setError("Precisa validar o CEP!");
        }else {
            inputBairro.setErrorEnabled(false);
        }
        if (campoLocalidade.getText().toString().isEmpty()){
            inputLocalidade.setErrorEnabled(true);
            inputLocalidade.setError("Precisa validar o CEP!");
        }else {
            inputLocalidade.setErrorEnabled(false);
        }
        if (campoUF.getText().toString().isEmpty()){
            inputUf.setErrorEnabled(true);
            inputUf.setError("Precisa validar o CEP!");
        }else {
            inputUf.setErrorEnabled(false);
        }
    }

    public void cadastrarDados(){

    }

    public void salvarDados(View view){

    }

    class MyTaskCep extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            validarForm();
            startVibrate();
        }
        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;
           validarForm();

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                inputStream = conexao.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";
                validarForm();
                startVibrate();

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }
        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            validarForm();

            String logradouro = null;
            String cep = null;
            //   String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;

            try {
                JSONObject jsonObject = new JSONObject(resultado);
                logradouro = jsonObject.getString("logradouro");
                cep = jsonObject.getString("cep");
                //      complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            campoLogradouro.setText(logradouro);
            //  campoComplemento.setText(complemento);
            campoBairro.setText(bairro);
            campoLocalidade.setText(localidade);
            campoUF.setText(uf);
            validarForm();
        }
        public void startVibrate() {
            long pattern[] = { 0, 100, 200 };
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, -1);
        }
    }
}