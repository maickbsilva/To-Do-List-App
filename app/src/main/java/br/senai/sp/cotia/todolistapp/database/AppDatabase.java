package br.senai.sp.cotia.todolistapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.senai.sp.cotia.todolistapp.model.Tarefa;


@Database(entities = {Tarefa.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    //variavel para acessar a database
    private static AppDatabase database;
    //metodo para tarefa dao
    public abstract TarefaDao getTarefaDao();

    public static AppDatabase getDatabase(Context context){
        //verifica se a database é null
        if (database == null){
            //se null, instancia a database e cria conexao
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "todolist").build();
        }
        //retorna a database
        return database;
    }
}
