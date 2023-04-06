package net.luanwilliam.cookbook.DAO;


import android.net.wifi.hotspot2.pps.Credential;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import net.luanwilliam.cookbook.model.Categoria;

import java.util.List;


@Dao
public interface CategoriaDAO {

    @Insert
    long insert(Categoria categoria);

    @Update
    void update(Categoria categoria);


    @Delete
    void delete(Categoria categoria);

    @Query(" SELECT * FROM categorias where idcat = :id ") Categoria queryIdCategoria(long id);

    @Query(" SELECT * FROM categorias  " )
    List<Categoria> queryAll();

    @Query(" SELECT * FROM categorias WHERE nome =  :nome ORDER BY  nome ASC ")
    List<Categoria> buscaNome(String nome);

    @Query(" SELECT idcat FROM categorias WHERE nome = :nome ") long buscaByNome(String nome);

    @Query(" SELECT count(*) FROM categorias") int qtdCategorias();





}
