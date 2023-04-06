package net.luanwilliam.cookbook.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import net.luanwilliam.cookbook.model.Categoria

@Dao
interface CategoriaDAO {
    @Insert
    fun insert(categoria: Categoria?): Long

    @Update
    fun update(categoria: Categoria?)

    @Delete
    fun delete(categoria: Categoria?)

    @Query(" SELECT * FROM categorias where idcat = :id ")
    fun queryIdCategoria(id: Long): Categoria?

    @Query(" SELECT * FROM categorias  ")
    fun queryAll(): List<Categoria?>?

    @Query(" SELECT * FROM categorias WHERE nome =  :nome ORDER BY  nome ASC ")
    fun buscaNome(nome: String?): List<Categoria?>?

    @Query(" SELECT idcat FROM categorias WHERE nome = :nome ")
    fun buscaByNome(nome: String?): Long

    @Query(" SELECT count(*) FROM categorias")
    fun qtdCategorias(): Int
}