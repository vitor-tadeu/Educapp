package com.example.educapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.educapp.R;
import com.example.educapp.dao.UsuarioDAO;
import com.example.educapp.model.Usuario;

public class Cadastro extends AppCompatActivity {
    private EditText editNomeCadastro, editEmailCadastro, editSenhaCadastro, editConfirmarSenhaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_cadastro);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        imgVoltar();
        cadastroValido();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editNomeCadastro = findViewById(R.id.editNomeCadastro);
        editEmailCadastro = findViewById(R.id.editEmailCadastro);
        editSenhaCadastro = findViewById(R.id.editSenhaCadastro);
        editConfirmarSenhaCadastro = findViewById(R.id.editConfirmarSenhaCadastro);
    }

    //Função click voltar
    private void imgVoltar() {
        ImageButton imgVoltarCadastro = findViewById(R.id.imgVoltarCadastro);
        imgVoltarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltaLogin();
            }
        });
    }

    //Valida cadastro e insere usuario no banco
    private void cadastroValido() {
        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCampos();
            }
        });
    }

    //Validando cadastro
    private void validaCampos() {
        if (TextUtils.isEmpty(editNomeCadastro.getText().toString()) || editNomeCadastro.getText().toString().trim().isEmpty()) {
            editNomeCadastro.requestFocus();
            editNomeCadastro.setError(getString(R.string.msgNome));
        } else if (!(!(TextUtils.isEmpty(editEmailCadastro.getText().toString()) || editEmailCadastro.getText().toString().trim().isEmpty())
                && Patterns.EMAIL_ADDRESS.matcher(editEmailCadastro.getText().toString()).matches())) {
            editEmailCadastro.requestFocus();
            editEmailCadastro.setError(getString(R.string.msgEmail));
        } else if (editSenhaCadastro.getText().toString().trim().length() < 8) {
            editSenhaCadastro.requestFocus();
            editSenhaCadastro.setError(getString(R.string.msgSenhaNova));
            editSenhaCadastro.setText("");
        } else if (!editConfirmarSenhaCadastro.getText().toString().equals(editSenhaCadastro.getText().toString())) {
            editConfirmarSenhaCadastro.requestFocus();
            editConfirmarSenhaCadastro.setError(getString(R.string.msgConfirmaSenha));
            editConfirmarSenhaCadastro.setText("");
        } else insereUsuario();
    }

    private void insereUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(editNomeCadastro.getText().toString());
        usuario.setEmail(editEmailCadastro.getText().toString());
        usuario.setSenha(editSenhaCadastro.getText().toString());
        usuario.setConfirmaSenha(editConfirmarSenhaCadastro.getText().toString());

        UsuarioDAO dao = new UsuarioDAO(Cadastro.this);
        dao.inserir(usuario);

        Toast.makeText(Cadastro.this, R.string.toastCadastro, Toast.LENGTH_SHORT).show();
        carregaTransicao();
    }

    //Função para voltar a tela login
    private void voltaLogin() {
        Intent intent = new Intent(Cadastro.this, Login.class);
        startActivity(intent);
        finish();
    }

    //Delay após cadastro valido
    private void carregaTransicao() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                voltaLogin();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        voltaLogin();
    }
}