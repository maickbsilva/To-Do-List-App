package br.senai.sp.cotia.todolistapp.fragment;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import br.senai.sp.cotia.todolistapp.R;
import br.senai.sp.cotia.todolistapp.database.AppDatabase;
import br.senai.sp.cotia.todolistapp.databinding.FragmentCadTarefaBinding;
import br.senai.sp.cotia.todolistapp.model.Tarefa;

public class CadTarefaFragment extends Fragment {
    private FragmentCadTarefaBinding binding;

    //variavel para o datePicker
    private DatePickerDialog datePicker;

    //variaveis para ano, mes e dia
    int year, month, day;

    //variavel para a data atual
    Calendar dataAtual;

    //variavel para a data formatada
    String dataFormatada = "";

    //variavel para a database
    AppDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //instancia a database
        database = AppDatabase.getDatabase(getContext());

        //instanciar o binding
        binding = FragmentCadTarefaBinding.inflate(getLayoutInflater(), container, false);

        //instancia a data atual
        dataAtual = Calendar.getInstance();

        //obter ano, mes e dia da data atual
        year = dataAtual.get(Calendar.YEAR);
        month = dataAtual.get(Calendar.MONTH);
        day = dataAtual.get(Calendar.DAY_OF_MONTH);

        //instancia o datePicker
        datePicker = new DatePickerDialog(getContext(), (datePicker, ano, mes, dia) -> {
            //ao escolher uma data no datePicker, cai aqui
            //passar para as variaveis globais
            year = ano;
            month = mes;
            day = dia;
            //formata a data
            dataFormatada = String.format("%02d/%02d/%04d", day, month + 1, year);
            //aplica a data formatada no botao
            binding.btDataPrevista.setText(dataFormatada);

        }, year, month, day);


        //ação do click do botao de seleção da data
        binding.btDataPrevista.setOnClickListener(v -> {
            datePicker.show();

        });

        //listener do botao salvar
        binding.btSalvar.setOnClickListener(v -> {
            if (dataFormatada.isEmpty()) {
                Toast.makeText(getContext(), "Data não informada", Toast.LENGTH_SHORT).show();
            } else if (binding.textNewTarefa.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Título não informado", Toast.LENGTH_SHORT).show();
            }else{
                //cria uma tarefa
                Tarefa tarefa = new Tarefa();
                //populando o objeto tarefa
                tarefa.setTitulo(binding.textNewTarefa.getText().toString());
                tarefa.setDescricao(binding.newDescricao.getText().toString());
                tarefa.setDataCriacao(dataAtual.getTimeInMillis());
                //cria um Calendar
                Calendar dataPrevista = Calendar.getInstance(); //vem com a data atual
                //muda a data para a data escolhida no datepicker
                dataPrevista.set(year, month, day);
                //passa os milisegundos da data para a data prevista
                tarefa.setDataPrevista(dataPrevista.getTimeInMillis());

                //salvar a tarefa
                //database.getTarefaDao().insert(tarefa);
                new InsertTarefa().execute(tarefa);

            }
        });

        //retorna a view raiz (root) do binding
        return binding.getRoot();
    }
    //AsyncTask para inserir Tarefa
    private class InsertTarefa extends AsyncTask<Tarefa, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Tarefa... tarefas) {
            //pegar a tarefa a partir do vetor
            Tarefa t = tarefas[0];
            try {


            //chamar o metodo para salvar a tarefa
            database.getTarefaDao().insert(t);
            //retornar
            return "ok";
            }catch (Exception erro){
                erro.printStackTrace();
                //retorna a mensagem e erro
                return erro.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("ok")){
                Log.w("RESULT", "IUUUUUUPI TAREFA INSERIDA COM SUCESSO");
                Toast.makeText(getContext(), "Tarefa inserida com sucesso!", Toast.LENGTH_SHORT).show();
                //voltar ao fragment anterior
                getActivity().onBackPressed();
            }else{
                Log.w("RESULT", result);
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}