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
import android.widget.TextView;

import com.example.educapp.R;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.util.adapter_recyclerview.listener.TarefaNomeOnItemClickListener;

import java.util.Collections;
import java.util.List;

public class TarefaNomeAdapter extends RecyclerView.Adapter<TarefaNomeAdapter.TarefaNomeViewHolder> {
    private final Context context;
    private final Activity activity;
    private final List<TarefaNome> nomes;
    private TarefaNomeOnItemClickListener onItemClickListener;

    public TarefaNomeAdapter(Context context, Activity activity, List<TarefaNome> nomes) {
        this.context = context;
        this.activity = activity;
        this.nomes = nomes;
    }

    public void setOnItemClickListener(TarefaNomeOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Chama a view para a lista
    @NonNull
    @Override
    public TarefaNomeAdapter.TarefaNomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.linha_tarefa_nome, parent, false);
        return new TarefaNomeViewHolder(view);
    }

    //Pega as informações dos objetos pela posição e coloca na view desejada pelo viewHolder
    @Override
    public void onBindViewHolder(@NonNull TarefaNomeAdapter.TarefaNomeViewHolder holder, int position) {
        TarefaNome nome = nomes.get(position);
        holder.preencheCampo(nome);
    }

    //Devolve o tamanho da lista
    @Override
    public int getItemCount() {
        return nomes.size();
    }

    public void onItemMove(final int posicaoInicial, final int posicaoFinal) {
        if (posicaoInicial < nomes.size() && posicaoFinal < nomes.size()) {
            if (posicaoInicial < posicaoFinal) {
                for (int i = posicaoInicial; i < posicaoFinal; i++) {
                    Collections.swap(nomes, i, i + 1);
                }
            } else {
                for (int i = posicaoInicial; i < posicaoFinal; i--) {
                    Collections.swap(nomes, i, i - 1);
                }
            }
        }
        notifyItemMoved(posicaoInicial, posicaoFinal);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Obtem as views que foram movidas
                TarefaNome nome1 = nomes.get(posicaoFinal);
                TarefaNome nome2 = nomes.get(posicaoInicial);

                //Atualiza a posição da view no recyclerview
                nome1.setPosicao(posicaoFinal);
                nome2.setPosicao(posicaoInicial);

                //Persiste a modificação no banco
                TarefaNomeDAO.getInstancia(activity).alterar(nome1);
                TarefaNomeDAO.getInstancia(activity).alterar(nome2);
            }
        }).start();
    }

    public void removeItem(final int posicao) {
        Snackbar.make(activity.findViewById(R.id.fragment_tarefa_confirmada), "Por favor, confirme", 2500)
                .setAction("Excluir", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TarefaNomeDAO.getInstancia(activity).deletar(nomes.get(posicao));
                        nomes.remove(posicao);
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

    //Otimiza os passos do bind
    class TarefaNomeViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtNomeTarefaConfirmado;
        private TarefaNome nome;

        private TarefaNomeViewHolder(View itemView) {
            super(itemView);
            txtNomeTarefaConfirmado = itemView.findViewById(R.id.txtNomeTarefaConfirmado);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(nome, getAdapterPosition());
                }
            });
        }

        private void preencheCampo(TarefaNome nome) {
            this.nome = nome;
            txtNomeTarefaConfirmado.setText(nome.getNomeLista());
        }
    }
}