package com.appturma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private String apiPath = "http://10.0.2.2:8080/siteturma88/usuarios/listar/";
    private JSONArray restulJsonArray;
    private int logado=0, resultado;
    private String mensagem = "", strnomecompleto= "", stremail="";
    EditText edtNomeUsuario,edtSenha;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtNomeUsuario = (EditText) findViewById(R.id.editTextUsuario);
        edtSenha = (EditText) findViewById(R.id.editTextSenha);

        btnlogin = (Button) findViewById(R.id.btnLogin);

        AndroidNetworking.initialize(getApplicationContext());

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario,senha;

                usuario = edtNomeUsuario.getText().toString();
                senha = edtSenha.getText().toString();
                if (usuario.isEmpty() || senha.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Erro")
                            .setMessage("Favor preencher os campos")
                            .setPositiveButton("Ok", null);
                    builder.create().show();
                }
                else{
                    sendApi();
                }

            }
        });
    }
    protected void sendApi(){
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT","application/json")
                .addBodyParameter("txtNomeUsuario",edtNomeUsuario.getText().toString())
                .addBodyParameter("txtSenhaUsuario",edtSenha.getText().toString())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null){

                                restulJsonArray = jsonObject.getJSONArray("RetornoDados");
                                JSONObject jsonObj = null;
                                jsonObj = restulJsonArray.getJSONObject(0);
                                resultado = jsonObj.getInt("resultado");

                                if(jsonObj.getInt("resultado") == 1) {
                                for (int i = 0; i <restulJsonArray.length(); i++){
                                    jsonObj = restulJsonArray.getJSONObject(i);
                                    logado = jsonObj.getInt("plogado");
                                    strnomecompleto = jsonObj.getString("pnomecompleto");
                                    stremail = jsonObj.getString("pemail");
                                    }
                                    switch (logado){
                                        case 1:
                                            mensagem = "Bem vindo ao Sistema";
                                            break;
                                        case 2:
                                            mensagem = "Usuário já está conectado.";
                                            break;
                                        case 3:
                                            mensagem = "Primeiro acesso. Altere sua senha.";
                                            break;
                                    }
                                }
                                else {
                                    mensagem = "Usuário ou senha estão incorretos.";
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Aviso")
                                        .setMessage(mensagem)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (resultado == 1) {
                                                    switch (logado){
                                                        case 1:
                                                            Intent base = new Intent(getApplicationContext(), BaseMenu.class);
                                                            base.putExtra("nomecompleto", strnomecompleto.toString());
                                                            base.putExtra("email", stremail.toString());
                                                            base.putExtra("nomeuser", edtNomeUsuario.getText().toString());
                                                            startActivity(base);
                                                            finish();
                                                            break;
                                                        case 3:
                                                            Intent troca = new Intent(getApplicationContext(), TrocaSenha.class);
                                                            troca.putExtra("telaorigem", "3");
                                                            troca.putExtra("nomecompleto", strnomecompleto.toString());
                                                            troca.putExtra("email", stremail.toString());
                                                            troca.putExtra("nomeuser", edtNomeUsuario.getText().toString());
                                                            startActivity(troca);
                                                            finish();
                                                            break;
                                                    }

                                                }
                                            else {
                                                    edtNomeUsuario.setText("");
                                                    edtSenha.setText("");
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
                    public void onError(ANError anError) {
                        try {
                            if (anError.getErrorCode()==0){
                                mensagem = "Porblemas com a conexão!! \nTente novamente.";
                            }
                            else{
                                JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                                if(jsonObject.getJSONObject("RetornoDados").getInt("sucesso") == 0){
                                    mensagem = "Usuário ou senha inválidos";
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Aviso")
                                    .setMessage(mensagem)
                                    .setPositiveButton("Ok", null);
                            builder.create().show();

                        }


                        catch (Exception e){
                            e.printStackTrace();
                        }

                        Log.d("BridgeUpdateService", "error" + anError.getErrorCode() + anError.getErrorDetail());
                    }
                });

    }
}