package com.example.educapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.dao.CriarEventoDAO;
import com.example.educapp.model.CriarEvento;
import com.example.educapp.util.adapter_recyclerview.CalendarioAdapter;
import com.example.educapp.util.adapter_recyclerview.callback.CalendarioItemTouch;
import com.example.educapp.util.adapter_recyclerview.listener.CalendarioOnItemClickListener;
import com.example.educapp.view.lembretes.EditarEvento;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CalendarioFragment extends Fragment {
    private static final String CHAVE_EVENTO = "EVENTO";
    public static String Dia;
    public static Integer flag;

    private TextView txtAno, txtMes, txtDia, txtSemana;
    private RecyclerView lista_calendario;
    private CalendarioAdapter adapter;
    private List<CriarEvento> eventos;
    private View view;

    public CalendarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendario, container, false);
        chamaFuncao();
        return view;
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        data();
        eventos = carregaLista();
        configuraAdapter();
        ordemAlfabetica();
        animacaoEvento();
        flag = 0;
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        txtAno = view.findViewById(R.id.txtAno);
        txtMes = view.findViewById(R.id.txtMes);
        txtDia = view.findViewById(R.id.txtDia);
        txtSemana = view.findViewById(R.id.txtSemana);
        lista_calendario = view.findViewById(R.id.lista_calendario);
    }

    //Formata a data para mostrar no calendario
    private void data() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMMM/d/E", new Locale("pt", "BR"));
        String strDate = sdf.format(cal.getTime());
        SimpleDateFormat dia = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
        Dia = dia.format(cal.getTime());
        String[] values = strDate.split("/", 0);

        for (String value : values) {
            Log.v("CHECK_DATE", value);
        }

        String mes = values[1].substring(0,1).toUpperCase().concat(values[1].substring(1));
        txtAno.setText(values[0]);
        txtMes.setText(mes);
        txtDia.setText(values[2]);
        txtSemana.setText(values[3]);
    }

    private List<CriarEvento> carregaLista() {
        CriarEventoDAO dao = new CriarEventoDAO(getActivity());
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dia = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
        Dia = dia.format(cal.getTime());
        flag = 1;
        return dao.listar();
    }

    public void configuraAdapter() {
        adapter = new CalendarioAdapter(getActivity(), getActivity(), eventos);
        lista_calendario.setAdapter(adapter);
        abreEditarEvento(adapter);
    }

    private void abreEditarEvento(CalendarioAdapter adapter) {
        adapter.setOnItemClickListener(new CalendarioOnItemClickListener() {
            @Override
            public void OnItemClick(CriarEvento evento, int posicao) {
                Intent intent = new Intent(getActivity(), EditarEvento.class);
                intent.putExtra(CHAVE_EVENTO, evento);
                startActivity(intent);
            }
        });
    }

    private void animacaoEvento() {
        CalendarioItemTouch.CalendarioCallback callback = new CalendarioItemTouch.CalendarioCallback() {
            @Override
            public void onItemMove(int posicaoInicial, int posicaoFinal) {
                adapter.onItemMove(posicaoInicial, posicaoFinal);
            }

            @Override
            public void removeItem(int posicao) {
                adapter.removeItem(posicao);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(new CalendarioItemTouch(callback));
        helper.attachToRecyclerView(lista_calendario);
    }

    private void  ordemAlfabetica(){
        Collections.sort(eventos, CriarEvento.EVENTO_COMPARATOR);
    }
}