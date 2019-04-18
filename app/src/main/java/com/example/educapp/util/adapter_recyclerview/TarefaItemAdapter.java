package com.example.educapp.util.adapter_recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educapp.R;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.CriarTarefa;
import com.example.educapp.util.adapter_recyclerview.listener.TarefaItemOnItemClickListener;

import java.util.Collections;
import java.util.List;

public class TarefaItemAdapter extends RecyclerView.Adapter<TarefaItemAdapter.TarefaItemViewHolder> {
    private static final String CHAVE_CHECKE = "CHECK";
    private static final String CHAVE_TAREFA = "TAREFA";
    private static final String CHAVE_CHECKBOX = "CHECKBOX";

    private final Context context;
    private final Activity activity;
    private final List<CriarTarefa> tarefas;
    private TarefaItemOnItemClickListener onItemClickListener;

    public TarefaItemAdapter(Context context, Activity activity, List<CriarTarefa> tarefas) {
        this.context = context;
        this.activity = activity;
        this.tarefas = tarefas;
    }

    public void setOnItemClickListener(TarefaItemOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //Chama a view para a lista
    @NonNull
    @Override
    public TarefaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.linha_tarefa_item, parent, false);

        SharedPreferences preferences = context.getSharedPreferences(CHAVE_TAREFA, android.content.Context.MODE_PRIVATE);
        boolean valueBoolean = preferences.getBoolean(CHAVE_CHECKE, false);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CHAVE_CHECKE, valueBoolean);
        editor.apply();

        return new TarefaItemViewHolder(view);
    }

    //Pega as informações dos objetos pela posição e coloca na view desejada pelo viewHolder
    @Override
    public void onBindViewHolder(@NonNull final TarefaItemViewHolder holder, int position) {
        CriarTarefa tarefa = tarefas.get(position);
        holder.preencheCampo(tarefa);
    }

    //Devolve o tamanho da lista
    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public void onItemMove(final int posicaoInicial, final int posicaoFinal) {
        if (posicaoInicial < tarefas.size() && posicaoFinal < tarefas.size()) {
            if (posicaoInicial < posicaoFinal) {
                for (int i = posicaoInicial; i < posicaoFinal; i++) {
                    Collections.swap(tarefas, i, i + 1);
                }
            } else {
                for (int i = posicaoInicial; i < posicaoFinal; i--) {
                    Collections.swap(tarefas, i, i - 1);
                }
            }
        }
        notifyItemMoved(posicaoInicial, posicaoFinal);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Obtem as views que foram movidas
                CriarTarefa tarefa1 = tarefas.get(posicaoFinal);
                CriarTarefa tarefa2 = tarefas.get(posicaoInicial);

                //Atualiza a posição da view no recyclerview
                tarefa1.setPosicao(posicaoFinal);
                tarefa2.setPosicao(posicaoInicial);

                //Persiste a modificação no banco
                TarefaNomeDAO.getInstancia(activity).alterarItem(tarefa1);
                TarefaNomeDAO.getInstancia(activity).alterarItem(tarefa2);
            }
        }).start();
    }

    public void removeItem(final int posicao) {
        Snackbar.make(activity.findViewById(R.id.layout_tarefa_item), "Por favor, confirme", 2500)
                .setAction("Excluir", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TarefaNomeDAO.getInstancia(activity).deletarItem(tarefas.get(posicao));
                        tarefas.remove(posicao);
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
    class TarefaItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txtItemLista;
        private CheckBox checkBox;
        private CriarTarefa item;

        private TarefaItemViewHolder(View itemView) {
            super(itemView);
            txtItemLista = itemView.findViewById(R.id.txtItemLista);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(item, getAdapterPosition());
                }
            });
        }

        private void preencheCampo(CriarTarefa tarefa) {
            this.item = tarefa;
            txtItemLista.setText(tarefa.getItemLista());

            checkBox.setChecked(getFromSP(CHAVE_CHECKBOX + getAdapterPosition()));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    tarefas.get(getAdapterPosition()).setCheck(isChecked);
                    saveInSp(CHAVE_CHECKBOX + getAdapterPosition(), isChecked);

                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        Toast.makeText(context, "Tarefa " + txtItemLista.getText() + " Concluída", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(true);
                    } else if (!checkBox.isChecked()) {
                        Toast.makeText(context, "Tarefa " + txtItemLista.getText() + " Pendente", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(false);
                    }
                }
            });
        }

        private boolean getFromSP(String key) {
            SharedPreferences preferences = context.getSharedPreferences(CHAVE_TAREFA, android.content.Context.MODE_PRIVATE);
            return preferences.getBoolean(key, false);
        }

        private void saveInSp(String key, boolean value) {
            SharedPreferences preferences = context.getSharedPreferences(CHAVE_TAREFA, android.content.Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }
}