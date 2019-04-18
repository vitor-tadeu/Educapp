package com.example.educapp.view.lembretes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.educapp.R;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.CriarTarefa;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.util.adapter_recyclerview.TarefaItemAdapter;
import com.example.educapp.util.adapter_recyclerview.callback.TarefaItemItemTouch;
import com.example.educapp.util.adapter_recyclerview.listener.TarefaItemOnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TarefaItem extends AppCompatActivity {
    private static final String CHAVE_TAREFA = "TAREFA";
    public static Integer idNome;
    private static String NomeTarefa;

    private EditText editNomeLista;
    private RecyclerView listItemTarefa;
    private TarefaItemAdapter adapter;
    private List<CriarTarefa> tarefas;

    private TarefaNomeDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarefa_item);
        chamaFuncao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tarefa_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                LayoutInflater inflater = getLayoutInflater();
//                View view = inflater.inflate(R.layout.layout_titulo_tipo_evento, null);
//                builder.setCustomTitle(view);
//                builder.setTitle("Insira um item:");
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        tarefas = carregaLista();
        configuraAdapter();
        ordemAlfabetica();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        carregaLista();
        recebeItens();
        pegaID();
        animacaoEvento();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editNomeLista = findViewById(R.id.editNomeTarefa);
        listItemTarefa = findViewById(R.id.lista_item_tarefa);

        dao = new TarefaNomeDAO(TarefaItem.this);
    }

    private List<CriarTarefa> carregaLista() {
        NomeTarefa = editNomeLista.getText().toString();
        return dao.listarItens(NomeTarefa);
    }

    public void configuraAdapter() {
        adapter = new TarefaItemAdapter(TarefaItem.this, TarefaItem.this, tarefas);
        listItemTarefa.setAdapter(adapter);
        abreLista(adapter);
    }

    private void abreLista(TarefaItemAdapter adapter) {
        adapter.setOnItemClickListener(new TarefaItemOnItemClickListener() {
            @Override
            public void OnItemClick(CriarTarefa tarefa, int posicao) {

            }
        });
    }

    private void animacaoEvento() {
        TarefaItemItemTouch.TarefaItemCallback callback = new TarefaItemItemTouch.TarefaItemCallback() {
            @Override
            public void onItemMove(int posicaoInicial, int posicaoFinal) {
                adapter.onItemMove(posicaoInicial, posicaoFinal);
            }

            @Override
            public void removeItem(int posicao) {
                adapter.removeItem(posicao);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(new TarefaItemItemTouch(callback));
        helper.attachToRecyclerView(listItemTarefa);
    }

    public void pegaID() {
        ArrayList<TarefaNome> tarefas = (ArrayList<TarefaNome>) dao.listar();
        for (int i = 0; i < tarefas.size(); i++) {
            TarefaNome tarefa = tarefas.get(i);
            if (tarefa.getNomeLista().equals(editNomeLista.getText().toString())) {
                idNome = Integer.parseInt(tarefa.getId().toString());
            }
        }
    }

    private void recebeItens() {
        Intent intent = getIntent();
        if (intent.hasExtra(CHAVE_TAREFA)) {
            TarefaNome tarefa = (TarefaNome) intent.getSerializableExtra(CHAVE_TAREFA);
            editNomeLista.setText(tarefa.getNomeLista());
        }
    }

    private void ordemAlfabetica() {
        Collections.sort(tarefas, CriarTarefa.NOME_COMPARATOR);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                editNomeLista.setFocusable(false);
                editNomeLista.setFocusableInTouchMode(false);
                editNomeLista.setFocusable(true);
                editNomeLista.setFocusableInTouchMode(true);
                fechaTeclado();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void fechaTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

//    @Override
//    public void onBackPressed() {
//        TarefaNome tarefa = new TarefaNome();
//        tarefa.setNomeLista(editNomeLista.getText().toString());
//
//        TarefaNomeDAO dao = new TarefaNomeDAO(getApplicationContext());
//        ArrayList<TarefaNome> nomes = (ArrayList<TarefaNome>) dao.listar();
//
//        for (int i = 0; i < nomes.size(); i++) {
//            TarefaNome nome = nomes.get(i);
//            idTarefa = nome.getId();
//        }
//
//        if (tarefa.getNomeLista() != null)
//            dao.alterar(tarefa);
//        finish();
//    }
}