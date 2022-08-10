package com.appturma;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.appturma.LoginActivity;
import com.appturma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrocaSenha extends AppCompatActivity {

    private String apiPath = "http://10.0.2.2:8080/siteturma88/usuarios/trocar/";
    private JSONArray restulJsonArray;
    private int logado = 0;
    private String mensagem = "0", telaorigem = " ";
    private TextView txtemail, txtnomeuser, txtnomecompleto;
    EditText edtNovaSenha, edtNovaSenhaValidar ,edtSenhaAtual;
    Button btnTroca, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_senha);

        txtnomeuser = findViewById(R.id.textViewNomeUsuario);
        txtemail = findViewById(R.id.textViewEmailUsuario);
        txtnomecompleto = findViewById(R.id.textViewNomeCompleto);

        edtSenhaAtual= findViewById(R.id.editTextSenhaAtual);
        edtNovaSenha = findViewById(R.id.editTextNovaSenha);
        edtNovaSenhaValidar = findViewById(R.id.editTextNovaSenhaValidar);

        Intent login = getIntent();
        txtnomeuser.setText(String.valueOf(login.getStringExtra("nomeuser")));
        txtemail.setText(String.valueOf(login.getStringExtra("email")));
        txtnomecompleto.setText(String.valueOf(login.getStringExtra("nomecompleto")));
        telaorigem=(String.valueOf(login.getStringExtra("telaorigem")));

        btnTroca = findViewById(R.id.btnTroca);
        btnVoltar = findViewById(R.id.btnVoltar);

        AndroidNetworking.initialize(getApplicationContext());

        btnTroca.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String novaSenha, novaSenhaValidar, senhaAtual;

                senhaAtual = edtSenhaAtual.getText().toString();
                novaSenha = edtNovaSenha.getText().toString();
                novaSenhaValidar = edtNovaSenhaValidar.getText().toString();

                if (senhaAtual.isEmpty() || novaSenha.isEmpty() || novaSenhaValidar.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrocaSenha.this)
                            .setTitle("Erro")
                            .setMessage("Favor preencher os campos")
                            .setPositiveButton("OK",null);
                    builder.create().show();
                }

                else {
                    if (novaSenha.equals(novaSenhaValidar)){
                        apiNovaSenha();
                    }
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (telaorigem.equals("2")){
                Intent base = new Intent(getApplicationContext(), BaseMenu.class);
                base.putExtra("nomecompleto", txtnomecompleto.getText().toString());
                base.putExtra("email", txtemail.getText().toString());
                base.putExtra("nomeuser", txtnomeuser.getText().toString());
                startActivity(base);
                finish();
            }
                    else{
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
            }
        });
    }

    protected void apiNovaSenha(){
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT","application/json")
                .addBodyParameter("txtNomeUsuario",txtnomeuser.getText().toString())
                .addBodyParameter("txtSenhaUsuario",edtSenhaAtual.getText().toString())
                .addBodyParameter("txtEmailUsuario",txtemail.getText().toString())
                .addBodyParameter("txtNovaSenha", edtNovaSenha.getText().toString())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                restulJsonArray = jsonObject.getJSONArray("RetornoDados");
                                JSONObject jsonObj = null;
                                jsonObj = restulJsonArray.getJSONObject(0);
                                logado = jsonObj.getInt("plogado");
                                switch (logado) {
                                    case 1:
                                        mensagem = "Sua senha de primeiro acesso foi alterada com sucesso, logue novamente";
                                        break;
                                    case 2:
                                        mensagem = "Sua senha foi alterada com sucesso";
                                        break;
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(TrocaSenha.this)
                                        .setTitle("Aviso")
                                        .setMessage(mensagem)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (logado == 1) {
                                                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(login);
                                                    finish();
                                                } else if (logado == 2) {
                                                    Intent base = new Intent(getApplicationContext(), BaseMenu.class);
                                                    base.putExtra("nomecompleto", txtnomecompleto.getText().toString());
                                                    base.putExtra("email", txtemail.getText().toString());
                                                    base.putExtra("nomeuser", txtnomeuser.getText().toString());
                                                    startActivity(base);
                                                    finish();
                                                }
                                            }
                                        });
                                builder.create().show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError){
                        try {
                            if (anError.getErrorCode() == 0){
                                mensagem = "Problemas com a conexão!! \nTente Novamente.";
                            }
                            else {
                                JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                                if (jsonObject.getJSONObject("RetornoDados").getInt("sucesso") == 0){
                                    mensagem = "Usuário ou senha inválidos";
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(TrocaSenha.this)
                                    .setTitle("Aviso")
                                    .setMessage(mensagem)
                                    .setPositiveButton("OK",null);
                            builder.create().show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d("BridgeUpdateService","error" + anError.getErrorCode() +anError.getErrorDetail());
                    }


                });
    }

}