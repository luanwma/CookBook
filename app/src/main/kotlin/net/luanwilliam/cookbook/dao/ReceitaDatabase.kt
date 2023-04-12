package net.luanwilliam.cookbook.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import net.luanwilliam.cookbook.model.Categoria
import net.luanwilliam.cookbook.model.Receita
import java.util.concurrent.Executors

@Database(entities = [Receita::class, Categoria::class], version = 1)
abstract class ReceitaDatabase : RoomDatabase() {
    abstract fun receitaDAO(): ReceitaDAO?
    abstract fun categoriaDAO(): CategoriaDAO

    companion object {
        private var instance: ReceitaDatabase? = null
        fun getDatabase(context: Context): ReceitaDatabase? {
            if (instance == null) {
                synchronized(ReceitaDatabase::class.java) {
                    if (instance == null) {
                        val builder = databaseBuilder(
                            context, ReceitaDatabase::class.java,
                            "cookbook.db"
                        )
                        //builder.fallbackToDestructiveMigration()
                        /*builder.addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Executors.newSingleThreadScheduledExecutor().execute { carregaCategorias(context) }
                            }
                        })*/
                        instance = builder.build()
                        carregaCategorias(context)
                    }
                }
            }
            return instance
        }

        //não vou usar o contexto -> entao nao deixa no codigo <3 kkk
        private fun carregaCategorias(context: Context) {
            val cat1 = Categoria("Doces de Festa", "Doces pequenos")
            instance!!.categoriaDAO().insert(cat1)
            val cat2 = Categoria("Salgados de festa", "Salgados pequenos")
            instance!!.categoriaDAO().insert(cat2)
        }
    }
}