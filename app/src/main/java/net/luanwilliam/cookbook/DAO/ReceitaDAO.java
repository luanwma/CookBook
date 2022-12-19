package net.luanwilliam.cookbook.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.util.List;


@Dao
public interface ReceitaDAO {

    @Insert
    long insert(Receita receita);


    @Update
    void update(Receita receita);

    @Delete
    void delete(Receita receita);

    @Query(" DELETE FROM receitas WHERE id_categoria = :idcat ") void deleteByCat(long idcat);

    @Query(" SELECT * FROM receitas where idrec = :id ") Receita queryIdReceita(long id);

    @Query(" SELECT * FROM receitas " )
    List<Receita> queryAll();

    @Query(" SELECT * FROM receitas WHERE nome =  :nome ORDER BY  nome ASC ")
    List<Receita> buscaReceita(String nome);

    @Query(" SELECT * FROM receitas WHERE id_categoria = :idcat ")
    List<Receita> listarReceitasIdCategoria(long idcat);

    @Query(" SELECT count(*) FROM receitas WHERE id_categoria = :id  ")
   int receitasCategoria(long id);





}
