package net.luanwilliam.cookbook.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.util.concurrent.Executors;


@Database(entities = {Receita.class, Categoria.class}, version = 1)
public abstract class ReceitaDatabase extends RoomDatabase {

    public abstract ReceitaDAO receitaDAO();

    public abstract CategoriaDAO categoriaDAO();

    private static ReceitaDatabase instance;

    public static ReceitaDatabase getDatabase(final Context context) {

        if (instance == null) {
            synchronized (ReceitaDatabase.class) {
                if (instance == null) {
                    Builder<ReceitaDatabase> builder = Room.databaseBuilder(context, ReceitaDatabase.class,
                            "cookbook.db");
                    builder.fallbackToDestructiveMigration();
                    builder.addCallback(new Callback() {

                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    carregaCategorias(context);
                                }
                            });
                        }
                    });
                    instance = builder.build();
                }
            }
        }
        return instance;
    }

    //não vou usar o contexto -> entao nao deixa no codigo <3 kkk
    private static void carregaCategorias(final Context context) {
        Categoria cat1 = new Categoria("Doces de Festa", "Doces pequenos");
        instance.categoriaDAO().insert(cat1);
        Categoria cat2 = new Categoria("Salgados de festa", "Salgados pequenos");
        instance.categoriaDAO().insert(cat2);
    }
}
