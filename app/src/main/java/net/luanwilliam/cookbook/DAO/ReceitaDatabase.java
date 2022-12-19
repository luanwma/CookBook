package net.luanwilliam.cookbook.DAO;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.util.List;
import java.util.concurrent.Executors;


@Database(entities = {Receita.class, Categoria.class}, version =2)
public abstract class ReceitaDatabase extends RoomDatabase {

    public  abstract ReceitaDAO receitaDAO();
    public abstract CategoriaDAO categoriaDAO();

    private static ReceitaDatabase instance;

    public static ReceitaDatabase getDatabase(final Context context){

        if(instance == null){
            synchronized (ReceitaDatabase.class){
                if(instance == null){
                    RoomDatabase.Builder builder = Room.databaseBuilder(context, ReceitaDatabase.class,
                            "cookbook.db");

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
                    instance = (ReceitaDatabase) builder.build();



                }
            }
        }
        return instance;
    }

    //não vou usar o contexto
    private static void carregaCategorias(final Context context){
        Categoria cat1 = new Categoria("Doces de Festa", "Doces pequenos");
        instance.categoriaDAO().insert(cat1);
        Categoria cat2 = new Categoria("Salgados de festa", "Salgados pequenos");
        instance.categoriaDAO().insert(cat2);
    }


}
