package com.example.educapp.view.menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.util.layout_inflater.DialogSair;
import com.example.educapp.util.navigation_bar.BottomNavigationViewHelper;
import com.example.educapp.view.minha_conta.AlterarSenha;
import com.example.educapp.view.minha_conta.EncerrarConta;
import com.example.educapp.view.minha_conta.Notificacao;
import com.example.educapp.view.minha_conta.Perfil;
import com.example.educapp.view.minha_conta.Sobre;

//TODO Setar nome e email do usuario
public class MinhaConta extends AppCompatActivity implements View.OnClickListener {
    private TextView txtNomeUsuario;
    private ImageView imgUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_minha_conta);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        navView();
        iniciaVariavel();
      //  recebeFoto();
        recebeNome();
    }

    //Seleção dos botões da barra de menu
    private void navView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(MinhaConta.this, Home.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.nav_criar_evento:
                        intent = new Intent(MinhaConta.this, Lembretes.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.nav_assistente:
                        intent = new Intent(MinhaConta.this, Assistente.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });
    }

    //Pegando as referências dos Botões e vinculando ao click
    public void iniciaVariavel() {
        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        imgUsuario = findViewById(R.id.imgUsuario);

        Button btnDesconectar = findViewById(R.id.btnDesconectar);
        Button btnAlterarSenha = findViewById(R.id.btnAlterarSenha);
        Button btnPerfil = findViewById(R.id.btnPerfil);
        Button btnEncerrarConta = findViewById(R.id.btnEncerrarConta);
        Button btnNotificacao = findViewById(R.id.btnNotificacao);
        Button btnSobre = findViewById(R.id.btnSobre);

        btnDesconectar.setOnClickListener(this);
        btnPerfil.setOnClickListener(this);
        btnAlterarSenha.setOnClickListener(this);
        btnEncerrarConta.setOnClickListener(this);
        btnNotificacao.setOnClickListener(this);
        btnSobre.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnDesconectar:
                sair();
                break;
            case R.id.btnPerfil:
                enviaNome();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnAlterarSenha:
                intent = new Intent(MinhaConta.this, AlterarSenha.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnEncerrarConta:
                intent = new Intent(MinhaConta.this, EncerrarConta.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnNotificacao:
                intent = new Intent(this, Notificacao.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnSobre:
                intent = new Intent(this, Sobre.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    //Abre caixa de dialgo
    private void sair() {
        DialogSair dialogSair = new DialogSair(MinhaConta.this);
        dialogSair.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSair.setCancelable(false);
        dialogSair.show();
    }

    //Recebe a foto do Perfil
    private void recebeFoto() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int foto = bundle.getInt("foto");
            imgUsuario.setImageResource(foto);
        }
    }

    private void recebeNome() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String nome = bundle.getString("nome");
                txtNomeUsuario.setText(nome);
            }
        }
    }

    //Envia o nome para Minha COnta
    private void enviaNome() {
        Bundle bundle = new Bundle();
        bundle.putString("nome", txtNomeUsuario.getText().toString());
        Intent intent = new Intent(MinhaConta.this, Perfil.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MinhaConta.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}