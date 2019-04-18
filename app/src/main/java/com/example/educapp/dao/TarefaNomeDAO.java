package com.example.educapp.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.educapp.model.CriarTarefa;
import com.example.educapp.model.TarefaNome;

import java.util.ArrayList;
import java.util.List;

public class TarefaNomeDAO extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db;

    @SuppressLint("StaticFieldLeak")
    private static TarefaNomeDAO instancia;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Tarefa";

    private static final String TABELA = "TAREFA_NOME";
    private static final String ID = "ID";
    private static final String NOME_TAREFA = "NOME_TAREFA";

    private static final String TABELA_ITEM = "TAREFA_ITEM";
    private static final String ID_ITEM = "ID";
    private static final String ID_NOME = "ID_NOME";
    private static final String ITEM_TAREFA = "ITEM_TAREFA";

    public TarefaNomeDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        db = getWritableDatabase();
    }

    public static TarefaNomeDAO getInstancia(Context context) {
        if (instancia == null) instancia = new TarefaNomeDAO(context);
        return instancia;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS " + TABELA
                + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOME_TAREFA + " TEXT NOT NULL "
                + " );";
        db.execSQL(sql);

        String sqlItem = " CREATE TABLE IF NOT EXISTS " + TABELA_ITEM
                + " ("
                + ID_ITEM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_NOME + " INTEGER NOT NULL, "
                + ITEM_TAREFA + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + ID_NOME + ") REFERENCES " + TABELA + "(" + ID + ")"
                + " );";
        db.execSQL(sqlItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(sql);
        onCreate(db);

        String sqlItem = "DROP TABLE IF EXISTS " + TABELA_ITEM;
        db.execSQL(sqlItem);
        onCreate(db);
    }

    @NonNull
    private ContentValues pegaDados(TarefaNome tarefa) {
        ContentValues values = new ContentValues();
        values.put(NOME_TAREFA, tarefa.getNomeLista());
        return values;
    }

    public void inserir(TarefaNome tarefa) {
        db = getWritableDatabase();

        ContentValues values = pegaDados(tarefa);
        db.insert(TABELA, null, values);
        db.close();
    }

    public void deletar(TarefaNome tarefa) {
        db = getWritableDatabase();

        String[] strings = {tarefa.getId().toString()};
        db.delete(TABELA, ID + " = ?", strings);
        db.close();
    }

    public void alterar(TarefaNome tarefa) {
        db = getWritableDatabase();

        ContentValues values = pegaDados(tarefa);
        String[] strings = {tarefa.getId().toString()};
        db.update(TABELA, values, ID + " = ?", strings);
        db.close();
    }

    public List<TarefaNome> listar() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABELA, null);

        List<TarefaNome> tarefas = new ArrayList<>();
        while (c.moveToNext()) {
            TarefaNome nomeTarefa = new TarefaNome();
            nomeTarefa.setId(c.getLong(c.getColumnIndex(ID)));
            nomeTarefa.setNomeLista(c.getString(c.getColumnIndex(NOME_TAREFA)));
            tarefas.add(nomeTarefa);
        }
        c.close();
        db.close();
        return tarefas;
    }

    @NonNull
    private ContentValues pegaDadosItem(CriarTarefa tarefa) {
        ContentValues valuesItem = new ContentValues();

        valuesItem.put(ID_NOME, tarefa.getIdNome());
        valuesItem.put(ITEM_TAREFA, tarefa.getItemLista());

        return valuesItem;
    }

    public void inserirItem(CriarTarefa tarefa) {
        db = getWritableDatabase();

        ContentValues valuesItem = pegaDadosItem(tarefa);
        db.insert(TABELA_ITEM, null, valuesItem);
        db.close();
    }

    public void deletarItem(CriarTarefa tarefa) {
        db = getWritableDatabase();

        String[] strings = {tarefa.getId().toString()};
        db.delete(TABELA_ITEM, ID + " = ?", strings);
        db.close();
    }

    public void alterarItem(CriarTarefa tarefa) {
        db = getWritableDatabase();

        ContentValues valuesItem = pegaDadosItem(tarefa);
        String[] strings = {tarefa.getId().toString()};
        db.update(TABELA_ITEM, valuesItem, ID + " = ?", strings);
        db.close();
    }

    public List<CriarTarefa> listarItens(String nomeTarefa) {
        db = getReadableDatabase();
        String[] strings = {nomeTarefa};
        Cursor c = db.rawQuery("SELECT * FROM TAREFA_ITEM,TAREFA_NOME WHERE TAREFA_NOME.ID = TAREFA_ITEM.ID_NOME AND NOME_TAREFA = ?", strings);

        List<CriarTarefa> tarefas = new ArrayList<>();
        while (c.moveToNext()) {
            CriarTarefa criarTarefa = new CriarTarefa();
//            criarTarefa.setId(c.getLong(c.getColumnIndex(ID_ITEM)));
            criarTarefa.setItemLista(c.getString(c.getColumnIndex(ITEM_TAREFA)));
            tarefas.add(criarTarefa);
        }
        c.close();
        db.close();
        return tarefas;
    }
}