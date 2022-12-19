package net.luanwilliam.cookbook.controle;

import android.content.Context;

import net.luanwilliam.cookbook.DAO.ReceitaDAO;
import net.luanwilliam.cookbook.DAO.ReceitaDatabase;
import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.util.List;

public class Controle    {
    private List<Receita> listaReceita ;
    private List<Categoria> listaCategoria ;
    Context contexto ;
/*
   public  CategoriaDatabase conexaoBDCategoria;
    public ReceitaDatabase conexaoBDReceita ;
    ReceitaDAO receitaDAO;
    CategoriaDAO categoriaDAO;

    public Controle(Context context){
        conexaoBDCategoria = CategoriaDatabase.getInstance(context);

        conexaoBDReceita = ReceitaDatabase.getInstance(context);
       // CategoriaDatabase data = CategoriaDatabase.getInstance(context);
        receitaDAO = new ReceitaDAO(conexaoBDReceita);
        categoriaDAO = new CategoriaDAO(conexaoBDCategoria);
        listaReceita = receitaDAO.getListaReceitas();
        listaCategoria =  categoriaDAO.getListaCategoria();

    }







    public boolean criarCategoria(String nome, String descricaoCat){
        boolean status = false;
        if(nome != null){
            if(buscarCategoria(nome) != null){
                status = false;
            }else {
                Categoria newCat = new Categoria(nome, descricaoCat);
                categoriaDAO.inserirCategoria(newCat);
                listaCategoria.add(newCat);
                status = true;
            }
        }
        listaCategoria.clear();
        listaCategoria = categoriaDAO.getListaCategoria();
        for(Categoria c : listaCategoria){
            System.out.println("dado "+c.getNome());

        }
        return status;

    }
    public boolean criarReceita(String nome, String descricao, String ingredientes , String modoPreparo, boolean check ,
                                String avaliacao, String categoria ){
        boolean status = false;

        Categoria cat = buscarCategoria(categoria);
        if(cat != null){
            Receita newRec = new Receita(nome, descricao, ingredientes, modoPreparo, check, avaliacao, cat);
            receitaDAO.inserirReceita(newRec);
            listaReceita.add(newRec);
            status = true;
        }

        return status;


    }
    public Categoria buscarCategoria(String nome){
        for(Categoria c : listaCategoria){
            if(c.getNome() == nome){
                return c;
            }
        }
        return null;
    }

    public boolean inserirReceita(Receita rec){
        if(this.listaReceita.add(rec) ){
            return true;
        }else return false;
    }

    public boolean inserirCategoria(Categoria cat){
        if(this.listaCategoria.add(cat)){
            return true;
        }else return false;
    }

    public List<Receita> listarReceitas(){
        return listaReceita;
    }
    public List<Categoria> listarCategorias(){
        return listaCategoria;
    }



 */


}
