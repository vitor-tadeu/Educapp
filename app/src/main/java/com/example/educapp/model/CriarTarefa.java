package com.example.educapp.model;

import java.io.Serializable;
import java.util.Comparator;

public class CriarTarefa implements Serializable {
    private Long id;
    private Integer idNome;
    private String itemLista;
    private Boolean check;
    private int posicao;

    public CriarTarefa() {
    }

    public static final Comparator<CriarTarefa> NOME_COMPARATOR = new Comparator<CriarTarefa>() {
        @Override
        public int compare(CriarTarefa nome1, CriarTarefa nome2) {
            return nome1.itemLista.compareTo(nome2.itemLista);
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdNome() { return idNome;}

    public void setIdNome(Integer idNome) { this.idNome = idNome;}

    public String getItemLista() {
        return itemLista;
    }

    public void setItemLista(String itemLista) {
        this.itemLista = itemLista;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}