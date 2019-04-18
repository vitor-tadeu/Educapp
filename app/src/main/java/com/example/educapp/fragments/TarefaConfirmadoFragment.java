package com.example.educapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.educapp.R;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.util.adapter_recyclerview.TarefaNomeAdapter;
import com.example.educapp.util.adapter_recyclerview.callback.TarefaNomeItemTouch;
import com.example.educapp.util.adapter_recyclerview.listener.TarefaNomeOnItemClickListener;
import com.example.educapp.view.lembretes.TarefaItem;

import java.util.Collections;
import java.util.List;

public class TarefaConfirmadoFragment extends Fragment {
    private static final String CHAVE_TAREFA = "TAREFA";
    private RecyclerView lista_tarefa_nome;
    private TarefaNomeAdapter adapter;
    private List<TarefaNome> nomes;
    private android.widget.SearchView searchView;
    private View view;

    public TarefaConfirmadoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tarefa_confirmada, container, false);
        chamaFuncao();
        return view;
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        animacaoEvento();
        teclado();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        lista_tarefa_nome = view.findViewById(R.id.lista_tarefa_nome);
        searchView = view.findViewById(R.id.searchView);
    }

    @Override
    public void onResume() {
        super.onResume();
        nomes = carregaLista();
        configuraAdapter();
        ordemAlfabetica();
    }

    private List<TarefaNome> carregaLista() {
        TarefaNomeDAO dao = new TarefaNomeDAO(getActivity());
        return dao.listar();
    }

    public void configuraAdapter() {
        adapter = new  TarefaNomeAdapter(getActivity(), getActivity(), nomes);
        lista_tarefa_nome.setAdapter(adapter);
        abreLista(adapter);
    }

    private void abreLista(TarefaNomeAdapter adapter) {
        adapter.setOnItemClickListener(new TarefaNomeOnItemClickListener() {
            @Override
            public void OnItemClick(TarefaNome nome, int posicao) {
                Intent intent = new Intent(getActivity(), TarefaItem.class);
                intent.putExtra(CHAVE_TAREFA, nome);
                startActivity(intent);
            }
        });
    }

    private void animacaoEvento() {
        TarefaNomeItemTouch.TarefaNomeCallback callback = new TarefaNomeItemTouch.TarefaNomeCallback() {
            @Override
            public void onItemMove(int posicaoInicial, int posicaoFinal) {
                adapter.onItemMove(posicaoInicial, posicaoFinal);
            }

            @Override
            public void removeItem(int posicao) {
                adapter.removeItem(posicao);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(new TarefaNomeItemTouch(callback));
        helper.attachToRecyclerView(lista_tarefa_nome);
    }

    private void  ordemAlfabetica(){
        Collections.sort(nomes, TarefaNome.NOME_COMPARATOR);
    }

    private void teclado() {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        searchView.setFocusable(false);
                        searchView.setFocusableInTouchMode(false);
                        searchView.setFocusable(true);
                        searchView.setFocusableInTouchMode(true);
                        return true;
                }
                return false;
            }
        });
    }
}