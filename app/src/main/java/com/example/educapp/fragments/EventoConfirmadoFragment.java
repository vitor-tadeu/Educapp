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
import com.example.educapp.dao.CriarEventoDAO;
import com.example.educapp.model.CriarEvento;
import com.example.educapp.util.adapter_recyclerview.EventoCofirmadoAdapter;
import com.example.educapp.util.adapter_recyclerview.callback.CriarEventoItemTouch;
import com.example.educapp.util.adapter_recyclerview.listener.CriarEventoOnItemClickListener;
import com.example.educapp.view.lembretes.EditarEvento;

import java.util.Collections;
import java.util.List;

public class EventoConfirmadoFragment extends Fragment {
    private static final String CHAVE_EVENTO = "EVENTO";
    private RecyclerView lista_evento_confirmado;
    private EventoCofirmadoAdapter adapter;
    private List<CriarEvento> eventos;
    private android.widget.SearchView searchView;
    private View view;

    public EventoConfirmadoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_evento_confirmado, container, false);
        chamaFuncao();
        return view;
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        animacaoEvento();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        lista_evento_confirmado = view.findViewById(R.id.lista_evento_confirmado);
        searchView = view.findViewById(R.id.searchView);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventos = carregaLista();
        configuraAdapter();
        ordemAlfabetica();
        teclado();
    }

    private List<CriarEvento> carregaLista() {
        CriarEventoDAO dao = new CriarEventoDAO(getActivity());
        return dao.listar();
    }

    public void configuraAdapter() {
        adapter = new EventoCofirmadoAdapter(getActivity(), getActivity(), eventos);
        lista_evento_confirmado.setAdapter(adapter);
        abreEditarEvento(adapter);
    }

    private void abreEditarEvento(EventoCofirmadoAdapter adapter) {
        adapter.setOnItemClickListener(new CriarEventoOnItemClickListener() {
            @Override
            public void OnItemClick(CriarEvento evento, int posicao) {
                Intent intent = new Intent(getActivity(), EditarEvento.class);
                intent.putExtra(CHAVE_EVENTO, evento);
                startActivity(intent);
            }
        });
    }

    private void animacaoEvento() {
        CriarEventoItemTouch.CriarEventoCallback callback = new CriarEventoItemTouch.CriarEventoCallback() {
            @Override
            public void onItemMove(int posicaoInicial, int posicaoFinal) {
                adapter.onItemMove(posicaoInicial, posicaoFinal);
            }

            @Override
            public void removeItem(int posicao) {
                adapter.removeItem(posicao);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(new CriarEventoItemTouch(callback));
        helper.attachToRecyclerView(lista_evento_confirmado);
    }

    private void  ordemAlfabetica(){
        Collections.sort(eventos, CriarEvento.EVENTO_COMPARATOR);
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