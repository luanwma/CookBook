package net.luanwilliam.cookbook.DAO;

import net.luanwilliam.cookbook.model.Receita;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReceitaDao2 implements Serializable {
    public static final String TABELA = "RECEITA";
    public static final String NOME = "NOME_RECEITA";
    public static final String ID = "ID_RECEITA";
    public static final String DESCRICAO = "DESCRICAO_RECEITA";
    public static final String LISTAINGREDIENTES = "INGREDIENTES_RECEITA";
    public static final String MODOPREPARO = "MODOPREPARO_RECEITA";
    public static final String AVALIACAO = "AVALIACAO_RECEITA";
    public static final String CHECK = "CHECK_RECEITA";

    private ReceitaDatabase conexao;
    public List<Receita> listaReceitas;

    public ReceitaDao2(ReceitaDatabase receitaDB ){
        conexao = receitaDB;
        listaReceitas = new ArrayList<>();

    }





/*
    public boolean deletarReceita(Receita rec){
        String[] args = {String.valueOf(rec.getId())};

        conexao.getWritableDatabase().delete(TABELA,
                ID + " = ? ",
                args);
        listaReceitas.remove(rec);

        return true;

    } */


    public void ordenarListaReceitas(){
        Collections.sort(listaReceitas, Receita.comparador);
    }


}
