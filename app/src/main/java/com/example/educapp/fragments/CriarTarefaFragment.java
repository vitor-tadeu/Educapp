package com.example.educapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.dao.TarefaItemDAO;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.CriarTarefa;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.util.adapter_listview.CriarTarefaAdapter;

import java.util.ArrayList;
import java.util.List;

public class CriarTarefaFragment extends Fragment implements View.OnClickListener {
    public static Integer idNomeTarefa;
    private EditText editNomeListaTarefa, editItem;
    private Button btnAddItens;
    private ListView listItemCriarTarefa;

    private RelativeLayout relAddItem;
    private View view;

    private TarefaNomeDAO dao;

    public CriarTarefaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_criar_tarefa, container, false);
        chamaFuncao();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregaLista();
        removeItem();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        habilitaTeclado();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editNomeListaTarefa = view.findViewById(R.id.editNomeListaTarefa);
        editItem = view.findViewById(R.id.editItem);

        relAddItem = view.findViewById(R.id.relAddItem);
        listItemCriarTarefa = view.findViewById(R.id.lista_criar_tarefa);

        dao = new TarefaNomeDAO(getActivity());

        btnAddItens = view.findViewById(R.id.btnAddItens);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        FloatingActionButton fabSalvar = view.findViewById(R.id.fabSalvar);

        btnAddItens.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        fabSalvar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddItens:
                validaNome();
                break;
            case R.id.btnAdd:
                validaItem();
                break;
            case R.id.fabSalvar:
                validaSalvar();
                break;
        }
    }

    public void carregaLista() {
        TarefaItemDAO dao = new TarefaItemDAO(getActivity());
        List<CriarTarefa> criarTarefas = dao.listarItemFake();

        CriarTarefaAdapter adapter = new CriarTarefaAdapter(getActivity(), criarTarefas);
        listItemCriarTarefa.setAdapter(adapter);
    }

    //Valida nome da tarefa e habilita adicionar item
    private void validaNome() {
        if (editNomeListaTarefa.getText().toString().trim().isEmpty()) {
            editNomeListaTarefa.requestFocus();

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.msgTitulo), Snackbar.LENGTH_SHORT);
            View v = snackbar.getView();
            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
        } else if (relAddItem.getVisibility() == View.GONE) {
            btnAddItens.setVisibility(View.GONE);
            relAddItem.setVisibility(View.VISIBLE);
            editItem.requestFocus();
            salvarNomeTarefa();
        }
    }

    //Salva a tarefa e envia para Tarefa Confirmada
    private void salvarNomeTarefa() {
        TarefaNome tarefa = new TarefaNome();
        tarefa.setNomeLista(editNomeListaTarefa.getText().toString());
        dao.inserir(tarefa);
    }

    //Valida os campos e adiciona o item na lista
    private void validaItem() {
        if (TextUtils.isEmpty(editItem.getText().toString()) || editItem.getText().toString().trim().isEmpty()) {
            editItem.requestFocus();

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.msgItem), Snackbar.LENGTH_SHORT);
            View v = snackbar.getView();
            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
        } else salvaItem();
    }

    private void salvaItem() {
        ArrayList<TarefaNome> tarefas = (ArrayList<TarefaNome>) dao.listar();
        for (int i = 0; i < tarefas.size(); i++) {
            TarefaNome tarefa = tarefas.get(i);
            idNomeTarefa = Integer.parseInt(tarefa.getId().toString());
        }

        CriarTarefa tarefa = new CriarTarefa();
        tarefa.setIdNome(idNomeTarefa);
        tarefa.setItemLista(editItem.getText().toString());
        dao.inserirItem(tarefa);

        CriarTarefa tarefaFake = new CriarTarefa();
        tarefaFake.setItemLista(editItem.getText().toString());
        TarefaItemDAO itemDAO = new TarefaItemDAO(getActivity());
        itemDAO.inserirItemFake(tarefaFake);

        editItem.setText("");
        carregaLista();

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.item_adicionado), Snackbar.LENGTH_SHORT);
        View v = snackbar.getView();
        TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    //Define o toque no item da lista
    public void removeItem() {
        listItemCriarTarefa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> tarefa, View item, int position, long id) {
                CriarTarefa criarTarefa = (CriarTarefa) listItemCriarTarefa.getItemAtPosition(position);
                dao.deletarItem(criarTarefa);
                carregaLista();

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.item_removido), Snackbar.LENGTH_SHORT);
                View v = snackbar.getView();
                TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                else
                    mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                snackbar.show();
            }
        });
    }

    //Salva a tarefa e exclui os itens
    private void validaSalvar() {
        if (editNomeListaTarefa.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.msgTitulo), Snackbar.LENGTH_SHORT);
            View v = snackbar.getView();
            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
//        } else if (itemDAO == null) {
//            editItem.requestFocus();
//
//            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.msgItem), Snackbar.LENGTH_SHORT);
//            View v = snackbar.getView();
//            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
//                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            else
//                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
//            snackbar.show();
        } else salvaLista();
    }

    private void salvaLista() {
        TarefaItemDAO dao = new TarefaItemDAO(getActivity());
        CriarTarefa tarefa = new CriarTarefa();
        dao.deletarItemFake(tarefa);
        editNomeListaTarefa.setText("");
        relAddItem.setVisibility(View.GONE);
        btnAddItens.setVisibility(View.VISIBLE);
        carregaLista();

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.lista_criada), Snackbar.LENGTH_SHORT);
        View v = snackbar.getView();
        TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    private void habilitaTeclado() {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        editNomeListaTarefa.setFocusable(false);
                        editNomeListaTarefa.setFocusableInTouchMode(false);
                        editNomeListaTarefa.setFocusable(true);
                        editNomeListaTarefa.setFocusableInTouchMode(true);
                        return true;
                }
                return false;
            }
        });
    }
}