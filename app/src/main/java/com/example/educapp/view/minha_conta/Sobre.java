package com.example.educapp.view.minha_conta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.educapp.R;
import com.example.educapp.view.minha_conta.sobre.Legal;
import com.example.educapp.view.minha_conta.sobre.Privacidade;
import com.example.educapp.view.minha_conta.sobre.Suporte;
import com.example.educapp.view.minha_conta.sobre.TermoServico;

public class Sobre extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conta_sobre);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
    }

    //Pegando as referências dos Botões e vinculando ao click
    public void iniciaVariavel() {
        Button btnPrivacidade = findViewById(R.id.btnPrivacidade);
        Button btnTermoServico = findViewById(R.id.btnTermoServico);
        Button btnSuporte = findViewById(R.id.btnSuporte);
        Button btnLegal = findViewById(R.id.btnLegal);

        btnPrivacidade.setOnClickListener(this);
        btnTermoServico.setOnClickListener(this);
        btnSuporte.setOnClickListener(this);
        btnLegal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnPrivacidade:
                intent = new Intent(this, Privacidade.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnTermoServico:
                intent = new Intent(this, TermoServico.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnSuporte:
                intent = new Intent(this, Suporte.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnLegal:
                intent = new Intent(this, Legal.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }
}