package com.example.educapp.model;

import java.io.Serializable;
import java.util.Comparator;

public class TarefaNome implements Serializable {
    private Long id;
    private String nomeLista;
    private int posicao;

    public TarefaNome() {
    }

    public static final Comparator<TarefaNome> NOME_COMPARATOR = new Comparator<TarefaNome>() {
        @Override
        public int compare(TarefaNome nome1, TarefaNome nome2) {
            return nome1.nomeLista.compareTo(nome2.nomeLista);
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}