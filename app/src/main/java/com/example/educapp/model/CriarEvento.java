package com.example.educapp.model;

import java.io.Serializable;
import java.util.Comparator;

public class CriarEvento implements Serializable {
    private Long id;
    private String titulo;
    private String descricao;
    private String tipoEvento;
    private String data;
    private String hora;
    private String addTarefa;
    //Variavel para memorizar a posição do item do recyclerview
    private int posicao;

    public CriarEvento() {
    }

    public static final Comparator<CriarEvento> EVENTO_COMPARATOR = new Comparator<CriarEvento>() {
        @Override
        public int compare(CriarEvento evento1, CriarEvento evento2) {
            return evento1.data.compareTo(evento2.data);
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public String getAddTarefa() {
        return addTarefa;
    }

    public void setAddTarefa(String addTarefa) {
        this.addTarefa = addTarefa;
    }
}