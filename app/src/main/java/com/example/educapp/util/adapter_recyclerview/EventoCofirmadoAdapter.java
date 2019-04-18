package com.example.educapp.util.adapter_recyclerview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.dao.CriarEventoDAO;
import com.example.educapp.model.CriarEvento;
import com.example.educapp.util.adapter_recyclerview.listener.CriarEventoOnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventoCofirmadoAdapter extends RecyclerView.Adapter<EventoCofirmadoAdapter.EventoViewHolder> implements Filterable{
    private final Context context;
    private final Activity activity;
    private final List<CriarEvento> eventos;
    private final List<CriarEvento> eventosFull;
    private CriarEventoOnItemClickListener onItemClickListener;

    public EventoCofirmadoAdapter(Context context, Activity activity, List<CriarEvento> eventos) {
        this.context = context;
        this.activity = activity;
        this.eventos = eventos;
        eventosFull = new ArrayList<>(eventos);
    }

    public void setOnItemClickListener(CriarEventoOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Chama a view para a lista
    @NonNull
    @Override
    public EventoCofirmadoAdapter.EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.linha_evento, parent, false);
        return new EventoViewHolder(view);
    }

    //Pega as informações dos objetos pela posição e coloca na view desejada pelo viewHolder
    @Override
    public void onBindViewHolder(@NonNull EventoCofirmadoAdapter.EventoViewHolder holder, int position) {
        CriarEvento evento = eventos.get(position);
        holder.preencheCampo(evento);
    }

    //Devolve o tamanho da lista
    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public void onItemMove(final int posicaoInicial, final int posicaoFinal) {
        if (posicaoInicial < eventos.size() && posicaoFinal < eventos.size()) {
            if (posicaoInicial < posicaoFinal) {
                for (int i = posicaoInicial; i < posicaoFinal; i++) {
                    Collections.swap(eventos, i, i + 1);
                }
            } else {
                for (int i = posicaoInicial; i < posicaoFinal; i--) {
                    Collections.swap(eventos, i, i - 1);
                }
            }
        }
        notifyItemMoved(posicaoInicial, posicaoFinal);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Obtem as views que foram movidas
                CriarEvento evento1 = eventos.get(posicaoFinal);
                CriarEvento evento2 = eventos.get(posicaoInicial);

                //Atualiza a posição da view no recyclerview
                evento1.setPosicao(posicaoFinal);
                evento2.setPosicao(posicaoInicial);

                //Persiste a modificação no banco
                CriarEventoDAO.getInstancia(activity).alterar(evento1);
                CriarEventoDAO.getInstancia(activity).alterar(evento2);
            }
        }).start();
    }

    public void removeItem(final int posicao) {
        Snackbar.make(activity.findViewById(R.id.fragment_evento_confirmado), "Por favor, confirme", 2500)
                .setAction("Excluir", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CriarEventoDAO.getInstancia(activity).deletar(eventos.get(posicao));
                        eventos.remove(posicao);
                        notifyItemRemoved(posicao);
                    }
                }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                notifyItemChanged(posicao);
            }
        }).show();
    }

    @Override
    public Filter getFilter() {
        return eventosFilter;
    }

    private Filter eventosFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CriarEvento> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(eventosFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CriarEvento evento : eventosFull) {
                    if (evento.getTitulo().toLowerCase().contains(filterPattern)) {
                        filteredList.add(evento);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eventos.clear();
            eventos.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    //Otimiza os passos do bind
    class EventoViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTituloEvento;
        private final TextView txtTipoEvento;
        private final TextView txtDataEvento;
        private CriarEvento evento;

        private EventoViewHolder(View itemView) {
            super(itemView);
            txtTituloEvento = itemView.findViewById(R.id.txtTituloEvento);
            txtTipoEvento = itemView.findViewById(R.id.txtTipoEvento);
            txtDataEvento = itemView.findViewById(R.id.txtDataEvento);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(evento, getAdapterPosition());
                }
            });
        }

        private void preencheCampo(CriarEvento evento) {
            this.evento = evento;
            txtTituloEvento.setText(evento.getTitulo());
            txtTipoEvento.setText(evento.getTipoEvento());
            txtDataEvento.setText(evento.getData());
        }
    }
}