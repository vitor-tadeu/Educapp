package com.example.educapp.util.adapter_recyclerview.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CalendarioItemTouch extends ItemTouchHelper.Callback {
    private CalendarioCallback callback;

    public CalendarioItemTouch(CalendarioCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    //Define o deslize do item selecionado
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(0, dragFlag);
    }

    //Uma chamada quando um elemento for arrastado
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        callback.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    //Um chamada quando o elemento for deslizado
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        callback.removeItem(viewHolder.getAdapterPosition());
    }

    public interface CalendarioCallback {
        void onItemMove(int posicaoInicial, int posicaoFinal);

        void removeItem(int posicao);
    }
}