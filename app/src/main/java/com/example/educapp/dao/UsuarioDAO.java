package com.example.educapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.educapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Usuario";
    private static final String TABELA = "USUARIO";

    private static final String ID = "ID";
    private static final String NOME = "NOME";
    private static final String EMAIL = "EMAIL";
    private static final String SENHA = "SENHA";
    private static final String CONFIRMA_SENHA = "CONFIRMA_SENHA";
    private static final String FOTO = "FOTO";

    public UsuarioDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS " + TABELA
                + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOME + " TEXT NOT NULL, "
                + EMAIL + " TEXT NOT NULL, "
                + SENHA + " TEXT NOT NULL, "
                + CONFIRMA_SENHA + " TEXT NOT NULL, "
                + FOTO + " BLOB "
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
    private ContentValues pegaDados(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(NOME, usuario.getNome());
        values.put(EMAIL, usuario.getEmail());
        values.put(SENHA, usuario.getSenha());
        values.put(CONFIRMA_SENHA, usuario.getConfirmaSenha());
        values.put(FOTO, usuario.getFoto());
        return values;
    }

    private ContentValues pegaDadosSenha(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put(EMAIL, usuario.getEmail());
        values.put(SENHA, usuario.getSenha());
        values.put(CONFIRMA_SENHA, usuario.getConfirmaSenha());
        return values;
    }

    public void inserir(Usuario usuario) {
        db = getWritableDatabase();

        ContentValues values = pegaDados(usuario);
        db.insert(TABELA, null, values);
        db.close();
    }

    public void deletar(Usuario usuario) {
        db = getWritableDatabase();
        try {
            String[] strings = {usuario.getEmail().toString()};
            db.delete(TABELA, EMAIL + " = ?", strings);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        db.close();
    }

    public void alterar(Usuario usuario) {
        db = getWritableDatabase();
        try {
            ContentValues values = pegaDadosSenha(usuario);
            String[] strings = {usuario.getEmail().toString()};
            db.update(TABELA, values, EMAIL + " = ?", strings);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        db.close();
    }

    public List<Usuario> listar() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABELA, null);

        List<Usuario> tarefas = new ArrayList<>();
        while (c.moveToNext()) {
            Usuario usuario = new Usuario();
            usuario.setId(c.getLong(c.getColumnIndex(ID)));
            usuario.setNome(c.getString(c.getColumnIndex(NOME)));
            usuario.setEmail(c.getString(c.getColumnIndex(EMAIL)));
            usuario.setSenha(c.getString(c.getColumnIndex(SENHA)));
            usuario.setConfirmaSenha(c.getString(c.getColumnIndex(CONFIRMA_SENHA)));
            usuario.setFoto(c.getBlob(c.getColumnIndex(FOTO)));
            tarefas.add(usuario);
        }
        c.close();
        db.close();
        return tarefas;
    }
}