package com.example.educapp.view.minha_conta;

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

public class AlterarSenha extends AppCompatActivity {
    private EditText editEmailAlterarSenha, editSenhaAlterarSenha, editConfirmarSenhaAlterarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conta_alterar_senha);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        alterarValido();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editEmailAlterarSenha = findViewById(R.id.editEmailAlterarSenha);
        editSenhaAlterarSenha = findViewById(R.id.editSenhaAlterarSenha);
        editConfirmarSenhaAlterarSenha = findViewById(R.id.editConfirmarSenhaAlterarSenha);
    }

    //Função click alterar
    private void alterarValido() {
        Button btnAlterarAlterarSenha = findViewById(R.id.btnAlterarAlterarSenha);
        btnAlterarAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCampos();
            }
        });
    }

    //Valida os campos vazios
    private void validaCampos() {
        if (!(!(TextUtils.isEmpty(editEmailAlterarSenha.getText().toString()) || editEmailAlterarSenha.getText().toString().trim().isEmpty())
                && Patterns.EMAIL_ADDRESS.matcher(editEmailAlterarSenha.getText().toString()).matches())) {
            editEmailAlterarSenha.requestFocus();
            editEmailAlterarSenha.setError(getString(R.string.msgEmail));
        } else if (editSenhaAlterarSenha.getText().toString().trim().length() < 8) {
            editSenhaAlterarSenha.requestFocus();
            editSenhaAlterarSenha.setError(getString(R.string.msgSenhaNova));
            editSenhaAlterarSenha.setText("");
        } else if (!editConfirmarSenhaAlterarSenha.getText().toString().equals(editSenhaAlterarSenha.getText().toString())) {
            editConfirmarSenhaAlterarSenha.requestFocus();
            editConfirmarSenhaAlterarSenha.setError(getString(R.string.msgConfirmaSenha));
            editConfirmarSenhaAlterarSenha.setText("");
        } else {
            alteraUsuario();
            Toast.makeText(AlterarSenha.this, R.string.toastRedefinirSenha, Toast.LENGTH_SHORT).show();
        }
    }

    private void alteraUsuario() {
        Usuario usuario = new Usuario();
        UsuarioDAO dao = new UsuarioDAO(AlterarSenha.this);

        usuario.setEmail(editEmailAlterarSenha.getText().toString());
        usuario.setSenha(editSenhaAlterarSenha.getText().toString());
        usuario.setConfirmaSenha(editConfirmarSenhaAlterarSenha.getText().toString());
        if (usuario.getEmail() != null)  {
            dao.alterar(usuario);
        }
        editEmailAlterarSenha.setText("");
        editSenhaAlterarSenha.setText("");
        editConfirmarSenhaAlterarSenha.setText("");

        Toast.makeText(AlterarSenha.this, R.string.toastRedefinirSenha, Toast.LENGTH_SHORT).show();
    }
}