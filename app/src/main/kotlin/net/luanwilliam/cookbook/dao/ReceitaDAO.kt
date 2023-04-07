package net.luanwilliam.cookbook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import net.luanwilliam.cookbook.model.Receita

@Dao
interface ReceitaDAO {
    @Insert
    fun insert(receita: Receita?): Long

    @Update
    fun update(receita: Receita?)

    @Delete
    fun delete(receita: Receita?)

    @Query(" DELETE FROM receitas WHERE id_categoria = :idcat ")
    fun deleteByCat(idcat: Long)

    @Query(" SELECT * FROM receitas where idrec = :id ")
    fun queryIdReceita(id: Long): Receita?

    @Query(" SELECT * FROM receitas ")
    fun queryAll(): List<Receita?>?

    @Query(" SELECT * FROM receitas WHERE nome =  :nome ORDER BY  nome ASC ")
    fun buscaReceita(nome: String?): List<Receita?>?

    @Query(" SELECT * FROM receitas WHERE id_categoria = :idcat ")
    fun listarReceitasIdCategoria(idcat: Long): List<Receita?>?

    @Query(" SELECT count(*) FROM receitas WHERE id_categoria = :id  ")
    fun receitasCategoria(id: Long): Int
}