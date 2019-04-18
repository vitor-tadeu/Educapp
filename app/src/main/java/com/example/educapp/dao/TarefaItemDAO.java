package com.example.educapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.educapp.model.CriarTarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaItemDAO extends SQLiteOpenHelper {
    private Context contextItem;
    private SQLiteDatabase db;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Tarefa_Item_Fake";
    private static final String TABELA = "TAREFA_ITEM_FAKE";

    private static final String ID_ITEM = "ID";
    private static final String ITEM_TAREFA = "ITEM_TAREFA";

    public TarefaItemDAO(Context contextItem) {
        super(contextItem, DB_NAME, null, DB_VERSION);
        this.contextItem = contextItem;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS " + TABELA
                + " ("
                + ID_ITEM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_TAREFA + " TEXT NOT NULL "
                + " );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlItem = "DROP TABLE IF EXISTS " + TABELA;
        db.execSQL(sqlItem);
        onCreate(db);
    }

    @NonNull
    private ContentValues pegaDadosItemFake(CriarTarefa tarefa) {
        ContentValues valuesItem = new ContentValues();
        valuesItem.put(ITEM_TAREFA, tarefa.getItemLista());
        return valuesItem;
    }

    public void inserirItemFake(CriarTarefa tarefa) {
        db = getWritableDatabase();

        ContentValues valuesItem = pegaDadosItemFake(tarefa);
        db.insert(TABELA, null, valuesItem);
        db.close();
    }

    public void deletarItemFake(CriarTarefa tarefa) {
        db = getWritableDatabase();

        db.delete(TABELA, null, null);
        db.close();
    }

    public void alterarItemFake(CriarTarefa tarefa) {
        db = getWritableDatabase();

        ContentValues valuesItem = pegaDadosItemFake(tarefa);
        String[] strings = {tarefa.getId().toString()};
        db.update(TABELA, valuesItem, ID_ITEM + " = ?", strings);
        db.close();
    }

    public List<CriarTarefa> listarItemFake() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABELA, null);

        List<CriarTarefa> tarefas = new ArrayList<>();
        while (c.moveToNext()) {
            CriarTarefa criarTarefa = new CriarTarefa();
            criarTarefa.setId(c.getLong(c.getColumnIndex(ID_ITEM)));
            criarTarefa.setItemLista(c.getString(c.getColumnIndex(ITEM_TAREFA)));
            tarefas.add(criarTarefa);
        }
        c.close();
        db.close();
        return tarefas;
    }
}