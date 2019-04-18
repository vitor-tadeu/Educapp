package com.example.educapp.util.layout_inflater;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.educapp.R;
import com.example.educapp.view.login.Login;

public class DialogSair extends Dialog implements View.OnClickListener {
    private Activity activity;

    public DialogSair(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_sair);
        iniciaVariavel();
    }

    //Pegando as referências dos Botões e vinculando ao click
    public void iniciaVariavel() {
        Button btnSair = findViewById(R.id.btnSair);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnSair.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSair:
                carregaTransicao();
                break;
            case R.id.btnCancelar:
                this.dismiss();
                break;
        }
    }

    //Delay após sair da conta
    private void carregaTransicao() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), Login.class);
                getContext().startActivity(intent);
                activity.finish();
            }
        }, 1500);
    }
}
