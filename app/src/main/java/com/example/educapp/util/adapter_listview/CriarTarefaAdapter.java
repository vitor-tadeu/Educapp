package com.example.educapp.util.adapter_listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.model.CriarTarefa;

import java.util.List;

public class CriarTarefaAdapter extends BaseAdapter {
    private final Context context;
    private final List<CriarTarefa> criarTarefas;

    public CriarTarefaAdapter(Context context, List<CriarTarefa> criarTarefas) {
        this.context = context;
        this.criarTarefas = criarTarefas;
    }

    //Devolve o tamanho da lista
    @Override
    public int getCount() {
        return criarTarefas.size();
    }

    //Devolve o item da lista
    @Override
    public Object getItem(int position) {
        return criarTarefas.get(position);
    }

    //Devolve o item da lista pelo id
    @Override
    public long getItemId(int position) {
        return criarTarefas.get(position).getId();
    }

    //Mostra o layout na tela
    //position = posição da lista
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CriarTarefa criarTarefa = criarTarefas.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        //Reaproveitando views
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.linha_tarefa_remover, viewGroup, false);
        }

        TextView txtItemRemover = view.findViewById(R.id.txtItemRemover);
        txtItemRemover.setText(criarTarefa.getItemLista());
        return view;
    }
}