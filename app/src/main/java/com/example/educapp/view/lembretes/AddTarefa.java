package com.example.educapp.view.lembretes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.educapp.R;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.util.adapter_recyclerview.TarefaNomeAdapter;
import com.example.educapp.util.adapter_recyclerview.listener.TarefaNomeOnItemClickListener;

import java.util.Collections;
import java.util.List;

public class AddTarefa extends AppCompatActivity {
    private static final String CHAVE_TAREFA = "TAREFA";
    private RecyclerView lista_add_tarefa;
    private TarefaNomeAdapter adapter;
    private List<TarefaNome> nomes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tarefa);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        nomes = carregaLista();
        configuraAdapter();
        ordemAlfabetica();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        lista_add_tarefa = findViewById(R.id.lista_add_tarefa);
    }

    private List<TarefaNome> carregaLista() {
        TarefaNomeDAO dao = new TarefaNomeDAO(this);
        return dao.listar();
    }

    public void configuraAdapter() {
        adapter = new TarefaNomeAdapter(AddTarefa.this, AddTarefa.this, nomes);
        lista_add_tarefa.setAdapter(adapter);
        abreLista(adapter);
    }

    private void abreLista(TarefaNomeAdapter adapter) {
        adapter.setOnItemClickListener(new TarefaNomeOnItemClickListener() {
            @Override
            public void OnItemClick(TarefaNome nome, int posicao) {
                Intent intent = new Intent(AddTarefa.this, EditarEvento.class);
                intent.putExtra(CHAVE_TAREFA, nome);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ordemAlfabetica() {
        Collections.sort(nomes, TarefaNome.NOME_COMPARATOR);
    }
}