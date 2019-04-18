package com.example.educapp.view.introducao;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.educapp.R;

public class TelaInicial extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_tela_inicial);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        rotacao();
        carregaTransicao();
    }

    //Faz a rotação da logo
    private void rotacao() {
        ImageView imgLogo = findViewById(R.id.imgLogo);
        AnimationDrawable rotacao = (AnimationDrawable) imgLogo.getDrawable();
        rotacao.start();
    }

    //Splash Screen
    private void carregaTransicao() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TelaInicial.this, IntroViewPager.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}