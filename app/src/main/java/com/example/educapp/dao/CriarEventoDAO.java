package com.example.educapp.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.educapp.fragments.CalendarioFragment;
import com.example.educapp.model.CriarEvento;

import java.util.ArrayList;
import java.util.List;

import static com.example.educapp.view.lembretes.EditarEvento.idEvento;

public class CriarEventoDAO extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db;
    public static String sql;

    @SuppressLint("StaticFieldLeak")
    private static CriarEventoDAO instancia;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Evento";
    private static final String TABELA = "EVENTO";

    private static final String ID = "ID";
    private static final String TITULO = "TITULO";
    private static final String DESCRICAO = "DESCRICAO";
    private static final String TIPO_EVENTO = "TIPO_EVENTO";
    private static final String DATA = "DATA";
    private static final String HORA = "HORA";
    private static final String TAREFA = "TAREFA";

    public CriarEventoDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        db = getWritableDatabase();
    }

    public static CriarEventoDAO getInstancia(Context context) {
        if (instancia == null) instancia = new CriarEventoDAO(context);
        return instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS " + TABELA
                + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITULO + " TEXT NOT NULL, "
                + DESCRICAO + " TEXT, "
                + TIPO_EVENTO + " TEXT NOT NULL, "
                + DATA + " TEXT, "
                + HORA + " TEXT, "
                + TAREFA + " TEXT "
                + " );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(sql);
        onCreate(db);
    }

    @NonNull
    private ContentValues pegaDados(CriarEvento evento) {
        ContentValues values = new ContentValues();
        values.put(TITULO, evento.getTitulo());
        values.put(DESCRICAO, evento.getDescricao());
        values.put(TIPO_EVENTO, evento.getTipoEvento());
        values.put(DATA, evento.getData());
        values.put(HORA, evento.getHora());
        values.put(TAREFA, evento.getAddTarefa());
        return values;
    }

    public void inserir(CriarEvento evento) {
        db = getWritableDatabase();

        ContentValues values = pegaDados(evento);
        db.insert(TABELA, null, values);
        db.close();
    }

    public void deletar(CriarEvento evento) {
        db = getWritableDatabase();

        String[] strings = {evento.getId().toString()};
        db.delete(TABELA, ID + " = ?", strings);
        db.close();
    }

    public void alterar(CriarEvento evento) {
        db = getWritableDatabase();
        try {
            ContentValues values = pegaDados(evento);
            String[] strings = {idEvento.toString()};
            db.update(TABELA, values, ID + " = ?", strings);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        db.close();
    }

    public List<CriarEvento> listar() {
        int flag = CalendarioFragment.flag;
        String dia = CalendarioFragment.Dia;
        if (flag == 1) {
            sql = "SELECT * FROM EVENTO WHERE DATA = '" + dia + "'";
        }else{
            sql = "SELECT * FROM EVENTO";
        }
        flag = 0;
        db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<CriarEvento> eventos = new ArrayList<>();
        while (c.moveToNext()) {
            CriarEvento criarEvento = new CriarEvento();
            criarEvento.setId(c.getLong(c.getColumnIndex(ID)));
            criarEvento.setTitulo(c.getString(c.getColumnIndex(TITULO)));
            criarEvento.setDescricao(c.getString(c.getColumnIndex(DESCRICAO)));
            criarEvento.setTipoEvento(c.getString(c.getColumnIndex(TIPO_EVENTO)));
            criarEvento.setData(c.getString(c.getColumnIndex(DATA)));
            criarEvento.setHora(c.getString(c.getColumnIndex(HORA)));
            criarEvento.setAddTarefa(c.getString(c.getColumnIndex(TAREFA)));
            eventos.add(criarEvento);
        }
        c.close();
        db.close();
        return eventos;
    }
}