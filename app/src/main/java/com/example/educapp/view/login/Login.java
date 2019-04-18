package com.example.educapp.view.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.educapp.R;
import com.example.educapp.view.menu.Home;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText editEmailLogin, editSenhaLogin;
    private Switch switchGravar;

    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_login);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        checkLembrar();
    }

    //Pegando as referências das Views e dos Botões e vinculando ao click
    @SuppressLint("CommitPrefEdits")
    private void iniciaVariavel() {
        editEmailLogin = findViewById(R.id.editEmailLogin);
        editSenhaLogin = findViewById(R.id.editSenhaLogin);
        switchGravar = findViewById(R.id.switchGravar);

        preferencias = getDefaultSharedPreferences(this);
        editor = preferencias.edit();

        Button btnEntrar = findViewById(R.id.btnEntrar);
        Button btnInscrever = findViewById(R.id.btnInscrever);
        Button btnRedefinir = findViewById(R.id.btnRedefinir);
        CardView btnFacebook = findViewById(R.id.btnFacebook);

        btnEntrar.setOnClickListener(this);
        btnInscrever.setOnClickListener(this);
        btnRedefinir.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnEntrar:
                validaCampos();
                lembrarLogin();
                finish();
                break;
            case R.id.btnInscrever:
                intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnRedefinir:
                intent = new Intent(Login.this, RedefinirSenha.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnFacebook:
                login();
                finish();
                break;
        }
    }

    //Login com facebook
    private void login() {
        Session.openActiveSession(this, true, new Session.StatusCallback() {
            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                Log.i("Script", "Name: " + user.getName());
                            }
                        }
                    }).executeAsync();
                    carregaTransicao();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    //Valida campos vazios
    private void validaCampos() {
//        if (!(!(TextUtils.isEmpty(editEmailLogin.getText().toString()) || editEmailLogin.getText().toString().trim().isEmpty())
//                && Patterns.EMAIL_ADDRESS.matcher(editEmailLogin.getText().toString()).matches())) {
//            editEmailLogin.requestFocus();
//            editEmailLogin.setError(getString(R.string.msgEmail));
//        } else if (editSenhaLogin.getText().toString().trim().length() < 8) {
//            editSenhaLogin.requestFocus();
//            editSenhaLogin.setError(getString(R.string.msgSenha));
//            editSenhaLogin.setText("");
//        } else {
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }
//}

    //Grava o login se o switch for ativado
    private void lembrarLogin() {
        if (switchGravar.isChecked()) {
            editor.putString(getString(R.string.emailSP), editEmailLogin.getText().toString());
            editor.putString(getString(R.string.senhaSP), editSenhaLogin.getText().toString());
            editor.putString(getString(R.string.lembrarSP), "True");
            editor.apply();
        } else {
            editor.putString(getString(R.string.emailSP), "");
            editor.putString(getString(R.string.senhaSP), "");
            editor.putString(getString(R.string.lembrarSP), "False");
            editor.apply();
        }
    }

    //Checa se tem um login salvo
    private void checkLembrar() {
        String email = preferencias.getString(getString(R.string.emailSP), "");
        String senha = preferencias.getString(getString(R.string.senhaSP), "");
        String lembrar = preferencias.getString(getString(R.string.lembrarSP), "False");

        editEmailLogin.setText(email);
        editSenhaLogin.setText(senha);

        if (lembrar.equals("True")) {
            switchGravar.setChecked(true);
        } else {
            switchGravar.setChecked(false);
        }
    }

    private void carregaTransicao() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}