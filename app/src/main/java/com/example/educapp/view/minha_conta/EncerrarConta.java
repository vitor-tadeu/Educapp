package com.example.educapp.view.minha_conta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.educapp.R;
import com.example.educapp.dao.UsuarioDAO;
import com.example.educapp.model.Usuario;
import com.example.educapp.view.login.Login;

public class EncerrarConta extends AppCompatActivity {
    private EditText editEmailEncerrarConta, editSenhaEncerrarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conta_encerrar_conta);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        alterarValido();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editEmailEncerrarConta = findViewById(R.id.editEmailEncerrarConta);
        editSenhaEncerrarConta = findViewById(R.id.editSenhaEncerrarConta);
    }

    //Função click encerrar conta
    private void alterarValido() {
        Button btnEncerrar = findViewById(R.id.btnEncerrar);
        btnEncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCampos();
            }
        });
    }

    //Valida os campos vazios
    private void validaCampos() {
        if (!(!(TextUtils.isEmpty(editEmailEncerrarConta.getText().toString()) || editEmailEncerrarConta.getText().toString().trim().isEmpty())
                && Patterns.EMAIL_ADDRESS.matcher(editEmailEncerrarConta.getText().toString()).matches())) {
            editEmailEncerrarConta.requestFocus();
            editEmailEncerrarConta.setError(getString(R.string.msgEmail));
        } else if (editSenhaEncerrarConta.getText().toString().trim().length() < 8) {
            editSenhaEncerrarConta.requestFocus();
            editSenhaEncerrarConta.setError(getString(R.string.msgSenhaNova));
            editSenhaEncerrarConta.setText("");
        } else excluirConta();
    }

    private void excluirConta() {
        Usuario usuario = new Usuario();
        UsuarioDAO usuarioCRUD = new UsuarioDAO(this);

        usuario.setEmail(editEmailEncerrarConta.getText().toString());
        usuario.setSenha(editSenhaEncerrarConta.getText().toString());

        if (usuario.getEmail() != null) {
            usuarioCRUD.deletar(usuario);
            Toast.makeText(EncerrarConta.this, R.string.msgEncerrarConta, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EncerrarConta.this, Login.class);
            startActivity(intent);
        }
    }
}