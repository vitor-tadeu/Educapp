package com.example.educapp.view.lembretes;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.educapp.R;
import com.example.educapp.dao.CriarEventoDAO;
import com.example.educapp.model.CriarEvento;
import com.example.educapp.model.TarefaNome;

import java.util.ArrayList;
import java.util.Calendar;

public class EditarEvento extends AppCompatActivity {
    private static final String CHAVE_EVENTO = "EVENTO";
    private static final String CHAVE_TAREFA = "TAREFA";
    public static Long idEvento;

    private Calendar dateTime = Calendar.getInstance();

    private EditText editTituloEventoConfirmado, editDescricaoEventoConfirmado;
    private Button btnTipoEventoConfirmado, btnDataEventoConfirmado, btnHoraEventoConfirmado;
    private Button btnAddTarefa;
    private String[] tipoEvento;

    private static String Titulo = null;
    private static String Descricao = null;
    private static String TipoEvento = null;
    private static String Data = null;
    private static String Hora = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_evento_confirmado);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        recebeEvento();
        String titulo = editTituloEventoConfirmado.getText().toString();
        String descricao = editDescricaoEventoConfirmado.getText().toString();
        alteraEvento(titulo, descricao);
        tipoEvento();
        selecionaDataHora();
        recebeTarefa();
        abreTarefa();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        editTituloEventoConfirmado = findViewById(R.id.editTituloEventoConfirmado);
        editDescricaoEventoConfirmado = findViewById(R.id.editDescricaoEventoConfirmado);
        btnTipoEventoConfirmado = findViewById(R.id.btnTipoEventoConfirmado);
        btnDataEventoConfirmado = findViewById(R.id.btnDataEventoConfirmado);
        btnHoraEventoConfirmado = findViewById(R.id.btnHoraEventoConfirmado);
        btnAddTarefa = findViewById(R.id.btnAddTarefaEventoConfirmado);
    }

    //Função para botão data e hora
    private void selecionaDataHora() {
        btnDataEventoConfirmado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizaData();
            }
        });

        btnHoraEventoConfirmado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizaHora();
            }
        });
    }

    private void atualizaData() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            btnDataEventoConfirmado.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    private void atualizaHora() {
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            btnHoraEventoConfirmado.setText(hourOfDay + ":" + minute);
        }
    };

    //Função botão tipo evento
    private void tipoEvento() {
        tipoEvento = getResources().getStringArray(R.array.tipo_evento);
        btnTipoEventoConfirmado.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarEvento.this);
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.layout_titulo_tipo_evento, null);
                builder.setCustomTitle(view);
                builder.setItems(tipoEvento, new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        btnTipoEventoConfirmado.setText(tipoEvento[position]);
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void abreTarefa(){
        btnAddTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarEvento.this, AddTarefaConfirmado.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void recebeEvento() {
        Intent intent = getIntent();
        if (intent.hasExtra(CHAVE_EVENTO)) {
            CriarEvento evento = (CriarEvento) intent.getSerializableExtra(CHAVE_EVENTO);

            editTituloEventoConfirmado.setText(evento.getTitulo());
            editDescricaoEventoConfirmado.setText(evento.getDescricao());
            btnTipoEventoConfirmado.setText(evento.getTipoEvento());
            btnDataEventoConfirmado.setText(evento.getData());
            btnHoraEventoConfirmado.setText(evento.getHora());
            btnAddTarefa.setText(evento.getAddTarefa());

            Titulo = (evento.getTitulo());
            Descricao = (evento.getDescricao());
            TipoEvento = (evento.getTipoEvento());
            Data = (evento.getData());
            Hora = (evento.getHora());
        }
    }

    private void recebeTarefa(){
        Intent intent = getIntent();
        if (intent.hasExtra(CHAVE_TAREFA)) {
            TarefaNome nome = (TarefaNome) intent.getSerializableExtra(CHAVE_TAREFA);
            btnAddTarefa.setText(nome.getNomeLista());
        }
        editTituloEventoConfirmado.setText(Titulo);
        editDescricaoEventoConfirmado.setText(Descricao);
        btnTipoEventoConfirmado.setText(TipoEvento);
        btnDataEventoConfirmado.setText(Data);
        btnHoraEventoConfirmado.setText(Hora);
    }

    //Salva o evento alterado no banco
    private void alteraEvento(final String titulo, final String descricao) {
        FloatingActionButton fabAlterarEventoConfirmado = findViewById(R.id.fabAlterarEventoConfirmado);
        fabAlterarEventoConfirmado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CriarEvento evento = new CriarEvento();
                CriarEventoDAO dao = new CriarEventoDAO(EditarEvento.this);

                evento.setTitulo(editTituloEventoConfirmado.getText().toString());
                evento.setDescricao(editDescricaoEventoConfirmado.getText().toString());
                evento.setTipoEvento(btnTipoEventoConfirmado.getText().toString());
                evento.setData(btnDataEventoConfirmado.getText().toString());
                evento.setHora(btnHoraEventoConfirmado.getText().toString());
                evento.setAddTarefa(btnAddTarefa.getText().toString());

                CriarEventoDAO criarDAO = new CriarEventoDAO(getApplicationContext());
                ArrayList<CriarEvento> eventos = (ArrayList<CriarEvento>) criarDAO.listar();

                for (int i = 0; i < eventos.size(); i++) {
                    CriarEvento criar = eventos.get(i);
                    if((criar.getTitulo()).equals (titulo)){
                        if(criar.getDescricao().equals(descricao)){
                            idEvento = criar.getId();
                        }
                    }
                }

                if (evento.getTitulo() != null)
                    dao.alterar(evento);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                editTituloEventoConfirmado.setFocusable(false);
                editTituloEventoConfirmado.setFocusableInTouchMode(false);
                editTituloEventoConfirmado.setFocusable(true);
                editTituloEventoConfirmado.setFocusableInTouchMode(true);
                editDescricaoEventoConfirmado.setFocusable(false);
                editDescricaoEventoConfirmado.setFocusableInTouchMode(false);
                editDescricaoEventoConfirmado.setFocusable(true);
                editDescricaoEventoConfirmado.setFocusableInTouchMode(true);
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}