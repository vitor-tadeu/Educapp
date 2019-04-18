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

public class RedefinirSenha extends AppCompatActivity {
    private EditText editEmailRedefinirSenha, editSenhaRedefinirSenha, editConfirmarSenhaRedefinirSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_redefinir_senha);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        imgVoltar();
        alterarValido();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editEmailRedefinirSenha = findViewById(R.id.editEmailRedefinirSenha);
        editSenhaRedefinirSenha = findViewById(R.id.editSenhaRedefinirSenha);
        editConfirmarSenhaRedefinirSenha = findViewById(R.id.editConfirmarSenhaRedefinirSenha);
    }

    //Função click voltar
    private void imgVoltar() {
        ImageButton imageButton = findViewById(R.id.imgVoltarRedefinir);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltaLogin();
            }
        });
    }

    //Função click alterar
    private void alterarValido() {
        Button btnAlterarRedefinirSenha = findViewById(R.id.btnAlterarRedefinirSenha);
        btnAlterarRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCampos();
            }
        });
    }

    //Valida os campos vazios
    private void validaCampos() {
        if (!(!(TextUtils.isEmpty(editEmailRedefinirSenha.getText().toString()) || editEmailRedefinirSenha.getText().toString().trim().isEmpty())
                && Patterns.EMAIL_ADDRESS.matcher(editEmailRedefinirSenha.getText().toString()).matches())) {
            editEmailRedefinirSenha.requestFocus();
            editEmailRedefinirSenha.setError(getString(R.string.msgEmail));
        } else if (editSenhaRedefinirSenha.getText().toString().trim().length() < 8) {
            editSenhaRedefinirSenha.requestFocus();
            editSenhaRedefinirSenha.setError(getString(R.string.msgSenhaNova));
            editSenhaRedefinirSenha.setText("");
        } else if (!editConfirmarSenhaRedefinirSenha.getText().toString().equals(editSenhaRedefinirSenha.getText().toString())) {
            editConfirmarSenhaRedefinirSenha.requestFocus();
            editConfirmarSenhaRedefinirSenha.setError(getString(R.string.msgConfirmaSenha));
            editConfirmarSenhaRedefinirSenha.setText("");
        } else alteraUsuario();
    }

    private void alteraUsuario() {
        Usuario usuario = new Usuario();
        UsuarioDAO dao = new UsuarioDAO(RedefinirSenha.this);

        usuario.setEmail(editEmailRedefinirSenha.getText().toString());
        usuario.setSenha(editSenhaRedefinirSenha.getText().toString());
        usuario.setConfirmaSenha(editConfirmarSenhaRedefinirSenha.getText().toString());
        if (usuario.getEmail() != null)  {
            dao.alterar(usuario);
        }

        Toast.makeText(RedefinirSenha.this, R.string.toastRedefinirSenha, Toast.LENGTH_SHORT).show();
        carregaTransicao();
    }

    //Volta a tela anterior pelo botão cadastro
    private void voltaLogin() {
        Intent volta = new Intent(RedefinirSenha.this, Login.class);
        startActivity(volta);
        finish();
    }

    //Delay após senha alterada
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