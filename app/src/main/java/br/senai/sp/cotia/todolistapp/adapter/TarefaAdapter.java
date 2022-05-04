package br.senai.sp.cotia.todolistapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.senai.sp.cotia.todolistapp.R;
import br.senai.sp.cotia.todolistapp.model.Tarefa;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {
    //variavel para a lista de tarefas
    private List<Tarefa> tarefas;
    //variavel para o Context
    private Context context;

    //construtor que recebe os parametros para o Aadapter
    public TarefaAdapter(List<Tarefa> lista, Context contexto) {
        this.tarefas = lista;
        this.context = contexto;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflar a view no viewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_tarefa, parent, false);

        //retorna uma viewHolder
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        //obter a tarefa atraves da position
        Tarefa t = tarefas.get(position);
        //transportar a informacao da tarefa para o holder
        holder.tvTitulo.setText(t.getTitulo());
        //formata a data e exibe no textview
        SimpleDateFormat formatador = new SimpleDateFormat(("dd/MM/yyyy"));
        holder.tvData.setText(formatador.format(t.getDataPrevista()));

        //verifica se está concluida
        if (t.isConcluida()) {
            holder.tvStatus.setText(R.string.tarefaConcluida);
            holder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (t.getDataPrevista() > Calendar.getInstance().getTimeInMillis()) {
            holder.tvStatus.setText(R.string.atrasado);
            holder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.red));

        } else {
            holder.tvStatus.setText(R.string.aberto);
            holder.tvStatus.setBackgroundColor(context.getResources().getColor(R.color.amarela));
        }
    }

    @Override
    public int getItemCount() {
        if (tarefas != null) {
            return tarefas.size();
        }
        return 0;
    }

    class TarefaViewHolder extends RecyclerView.ViewHolder {
        //variaveis para os componentes do layout
        TextView tvTitulo, tvData, tvStatus;

        public TarefaViewHolder(View view) {
            super(view);
            //passar da view para os componentes
            tvTitulo = view.findViewById(R.id.tituloTarefa);
            tvData = view.findViewById(R.id.dataTarefa);
            tvStatus = view.findViewById(R.id.statusTarefa);
        }
    }

}
