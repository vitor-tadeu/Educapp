package com.example.educapp.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.dao.CriarEventoDAO;
import com.example.educapp.fragments.data_time.AppUtils;
import com.example.educapp.fragments.data_time.DatePickerFragment;
import com.example.educapp.fragments.data_time.DatePickerSelectionInterface;
import com.example.educapp.fragments.data_time.TimePickerFragment;
import com.example.educapp.fragments.data_time.TimePickerInterface;
import com.example.educapp.model.CriarEvento;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.view.lembretes.AddTarefaConfirmado;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CriarEventoFragment extends Fragment implements TimePickerInterface, DatePickerSelectionInterface {
    private static final String CHAVE_TAREFA = "TAREFA";

    //Pega a data e a hora do sitema
    private Calendar dateTime = Calendar.getInstance();

    private long btnData, btnHora;
    private Button btnTipoEvento, btnAddTarefa;
    private EditText editTituloCriarEvento, editDescricao;
    private TextView txtData, txtHora;

    private String[] tipoEvento;
    private View view;

    public CriarEventoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_criar_evento, container, false);
        chamaFuncao();
        return view;
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        atualizaDataHora();
        selecionaDataHora();
        tipoEvento();
        abreTarefa();
        recebeTarefa();
        fabSalvar();
        teclado();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editTituloCriarEvento = view.findViewById(R.id.editTituloCriarEvento);
        editDescricao = view.findViewById(R.id.editDescricao);
        txtData = view.findViewById(R.id.btnData);
        txtHora = view.findViewById(R.id.btnHora);
        btnTipoEvento = view.findViewById(R.id.btnTipoEvento);
        btnAddTarefa = view.findViewById(R.id.btnAddTarefa);
    }

    //Atualiza a data e hora do sistema
    private void atualizaDataHora() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
        txtData.setText(dateFormat.format(dateTime.getTime()));
        txtHora.setText(hora.format(dateTime.getTime()));
    }

    //Função para botão data e hora
    private void selecionaDataHora() {
        Button Data = view.findViewById(R.id.btnData);
        Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizaData();
            }
        });

        Button Hora = view.findViewById(R.id.btnHora);
        Hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizaHora();
            }
        });
        atualizaDataHora();
    }

    //Atualiza a data e popula no botão
    private void atualizaData() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        if (btnData > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(btnData);
            Bundle bundle = new Bundle();
            bundle.putInt("year", calendar.get(Calendar.YEAR));
            bundle.putInt("month", calendar.get(Calendar.MONTH) + 1);
            bundle.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
            datePickerFragment.setArguments(bundle);
        }
        datePickerFragment.delegate = com.example.educapp.fragments.CriarEventoFragment.this;
        datePickerFragment.setCancelable(false);
        assert getFragmentManager() != null;
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    //Atualiza a hora e popula no botão
    private void atualizaHora() {
        TimePickerFragment timePicker = new TimePickerFragment();
        if (btnHora > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(btnHora);
            Bundle bundle = new Bundle();
            bundle.putInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
            bundle.putInt("minute", calendar.get(Calendar.MINUTE));
            timePicker.setArguments(bundle);
        }
        timePicker.delegate = com.example.educapp.fragments.CriarEventoFragment.this;
        timePicker.setCancelable(false);
        assert getFragmentManager() != null;
        timePicker.show(getFragmentManager(), "timePicker");
    }

    //Formata a data
    @Override
    public void selecionaData(int day, int month, int year) {
        btnData = 0;
        txtData.setText(String.valueOf(AppUtils.formatCharLength(2, day) + "/" + AppUtils.formatCharLength(2, (month + 1)) + "/" + year));
        btnData = AppUtils.dateIntoTimeStamp(String.valueOf(day + "/" + (month) + "/" + year));
    }

    //Formata a hora
    @Override
    public void selecionaHora(int hours, int minute) {
        btnHora = 0;
        txtHora.setText(String.valueOf(AppUtils.formatCharLength(2, hours) + ":" + AppUtils.formatCharLength(2, minute)));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        String formatted = format1.format(cal.getTime());
        btnHora = AppUtils.timeIntoTimeStamp(formatted + " " + hours + ":" + minute);
    }

    //Função botão tipo evento
    private void tipoEvento() {
        tipoEvento = getResources().getStringArray(R.array.tipo_evento);
        btnTipoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.layout_titulo_tipo_evento, null);
                builder.setCustomTitle(view);

                builder.setItems(tipoEvento, new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        btnTipoEvento.setText(tipoEvento[position]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void abreTarefa() {
        btnAddTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTarefaConfirmado.class);
                startActivity(intent);
            }
        });
    }

    private void recebeTarefa() {
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(CHAVE_TAREFA)) {
            TarefaNome nome = (TarefaNome) intent.getSerializableExtra(CHAVE_TAREFA);
            btnAddTarefa.setText(nome.getNomeLista());
        }
    }

    //Salva o evento e envia para Evento Confirmado
    private void fabSalvar() {
        FloatingActionButton fabSalvar = view.findViewById(R.id.fabSalvar);
        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaSalvar();
            }
        });
    }

    private void salvaEvento() {
        CriarEvento evento = new CriarEvento();
        evento.setTitulo(editTituloCriarEvento.getText().toString());
        evento.setDescricao(editDescricao.getText().toString());
        evento.setTipoEvento(btnTipoEvento.getText().toString());
        evento.setData(txtData.getText().toString());
        evento.setHora(txtHora.getText().toString());
        evento.setAddTarefa(btnAddTarefa.getText().toString());

        CriarEventoDAO dao = new CriarEventoDAO(getContext());
        dao.inserir(evento);

        editTituloCriarEvento.setText("");
        editDescricao.setText("");
        btnTipoEvento.setText("");
        atualizaDataHora();
        btnAddTarefa.setText("");
    }

    private void validaSalvar() {
        if (editTituloCriarEvento.getText().toString().trim().isEmpty()) {
            editTituloCriarEvento.requestFocus();

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.nome_titulo), Snackbar.LENGTH_SHORT);
            View v = snackbar.getView();
            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
        } else if (btnTipoEvento.getText().toString().trim().isEmpty()) {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.msg_tipo_evento), Snackbar.LENGTH_SHORT);
            View v = snackbar.getView();
            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
        } else if (txtData.getText().toString().trim().isEmpty()) {
            txtData.requestFocus();

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.msg_data), Snackbar.LENGTH_SHORT);
            View v = snackbar.getView();
            TextView mTextView = v.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            snackbar.show();
        } else salvaEvento();
    }

    private void teclado() {
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        editTituloCriarEvento.setFocusable(false);
                        editTituloCriarEvento.setFocusableInTouchMode(false);
                        editTituloCriarEvento.setFocusable(true);
                        editTituloCriarEvento.setFocusableInTouchMode(true);
                        editDescricao.setFocusable(false);
                        editDescricao.setFocusableInTouchMode(false);
                        editDescricao.setFocusable(true);
                        editDescricao.setFocusableInTouchMode(true);
                        return true;
                }
                return false;
            }
        });
    }
}