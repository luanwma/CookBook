package net.luanwilliam.cookbook.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DAO  {
 /*
    private static DAO instance ;
    private Context context;
    private static final String DB_NAME = "Banco_Dados";
    private static final int DB_VERSION = 1;

    public static final String TABELA_CATEGORIA = "CATEGORIA";
    public static final String NOME_CATEGORIA = "NOME_CATEGORIA";
    public static final String ID_CATEGORIA = "ID_CATEGORIA";
    public static final String DESCRICAO_CATEGORIA = "DESCRICAO_CATEGORIA";

    public static final String TABELA_RECEITA = "RECEITA";

    public static final String NOME_RECEITA = "NOME_RECEITA";
    public static final String ID_RECEITA = "ID_RECEITA";
    public static final String DESCRICAO_RECEITA = "DESCRICAO_RECEITA";
    public static final String LISTAINGREDIENTES = "INGREDIENTES_RECEITA";
    public static final String MODOPREPARO = "MODOPREPARO_RECEITA";
    public static final String AVALIACAO = "AVALIACAO_RECEITA";
    public static final String CHECK = "CHECK_RECEITA";
    public static final String CATEGORIA_ID = "CATEGORIA_ID";

    public List<Categoria> listaCategoria = new ArrayList<>();
    public List<Receita> listaReceita = new ArrayList<>();



    public static DAO getInstance(Context context){
        if(instance == null){
            instance = new DAO(context);
        }
        return instance;
    }

    public DAO(Context contexto){
        super(contexto, DB_NAME, null, DB_VERSION);
        context = contexto;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        criarTabelaCategoria(db);
        criarTabelaReceita(db);

    }
    public void iniciarBD(){
        SQLiteDatabase db = getWritableDatabase();
        criarTabelaReceita(db);
        criarTabelaCategoria(db);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTabelaCategoria(db);
        deleteTabelaReceita();
        onCreate(db);
    }

    public void criarTabelaCategoria(SQLiteDatabase db){
        String sqlCriarTabela =  " CREATE TABLE IF NOT EXISTS " + TABELA_CATEGORIA + " ( " + ID_CATEGORIA + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                NOME_CATEGORIA + " TEXT UNIQUE NOT NULL , " +
                DESCRICAO_CATEGORIA + " TEXT NOT NULL ); ";
        db.execSQL(sqlCriarTabela);
    }

    public void criarTabelaReceita(SQLiteDatabase db){
        String sqlCriarTabela = " CREATE TABLE IF NOT EXISTS " +TABELA_RECEITA + " ( " + ID_RECEITA + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " +
                NOME_RECEITA + " TEXT NOT NULL , " +
                DESCRICAO_RECEITA + " TEXT NOT NULL , " +
                LISTAINGREDIENTES + " TEXT NOT NULL , " +
                MODOPREPARO + " TEXT NOT NULL, "+
                AVALIACAO + " TEXT , " +
                CHECK + " boolean, " +
                " CATEGORIA_ID INTEGER NOT NULL , " +
                " FOREIGN KEY (CATEGORIA_ID) REFERENCES " + TABELA_CATEGORIA + " ( ID_CATEGORIA )  ) ;" ;
        db.execSQL(sqlCriarTabela);
    }
    public void deleteTabelaReceita(){
        SQLiteDatabase db = getWritableDatabase();
        String sqlDeleteTabela = " DROP TABLE IF EXISTS RECEITA  ; " ;
        db.execSQL(sqlDeleteTabela);
        iniciarBD();
    }
    public void deleteTabelaCategoria(SQLiteDatabase db){

        String sqlDeleteCategoria = " DROP TABLE IF EXISTS " + TABELA_CATEGORIA + " ; ";
        db.execSQL(sqlDeleteCategoria);
        iniciarBD();

    }

    public void inserirCategoria(Categoria cat){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //  values.put(ID,cat.getId());
        values.put(NOME_CATEGORIA, cat.getNome());
        values.put(DESCRICAO_CATEGORIA, cat.getDescCat());
        long id = db.insert(TABELA_CATEGORIA,
                null,
                values);
        cat.setIdcat(id);
        db.close();

    }


    public void ordenarListaCategoria(){
        Collections.sort(listaCategoria, Categoria.comparador);
    }

    public List<Categoria> getListaCategoria(){
        return listaCategoria;
    }
    public Categoria buscaCategoria(String nome){
       String busca = " select * from " + TABELA_CATEGORIA + " WHERE " + NOME_CATEGORIA + " = ' "  + nome + " '  ; "  ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(busca, null);
        int colID = cursor.getColumnIndex(ID_CATEGORIA);
        int colNOME = cursor.getColumnIndex(NOME_CATEGORIA);
        int colDESC = cursor.getColumnIndex(DESCRICAO_CATEGORIA);
        Categoria cat = new Categoria();
        while (cursor.moveToNext()){
        cat = new Categoria(cursor.getString(colNOME), cursor.getString(colDESC));
        cat.setIdcat(colID);

        }
        cursor.close();
        db.close();
        if(cat != null){
            return cat;
        }else
        return null;
    }

    public boolean alterarCategoria( String nome, String novoNome, String desc ){
        boolean status = false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        Categoria cat = buscaCategoria(nome);
        if(cat != null){
            String[] args = {String.valueOf(cat.getId())};
            cat.setNome(novoNome);
            cat.setDescCat(desc);
            values.put(NOME_CATEGORIA, cat.getNome());
            values.put(DESCRICAO_CATEGORIA, cat.getDescCat());
            db.update(TABELA_CATEGORIA, values, ID_CATEGORIA + " = ?", args);
            listaCategoria.clear();
            carregarCategoriaBD();
            status = true;
        }
        db.close();

        return status;
    }

    public void carregarCategoriaBD(){
        this.listaCategoria = new ArrayList<>();

        String sqlBusca = " SELECT * FROM " + TABELA_CATEGORIA + " ORDER BY " + NOME_CATEGORIA;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlBusca, null);
        int colID = cursor.getColumnIndex(ID_CATEGORIA);
        int colNOME = cursor.getColumnIndex(NOME_CATEGORIA);
        int colDESC = cursor.getColumnIndex(DESCRICAO_CATEGORIA);

        while (cursor.moveToNext()) {

            Categoria cat = new Categoria(cursor.getString(colNOME), cursor.getString(colDESC));
            cat.setIdcat(colID);
            this.listaCategoria.add(cat);


        }
        cursor.close();
        db.close();
        ordenarListaCategoria();
    }

    public List<Categoria> carregarCatBD(){
        List<Categoria> lista = new ArrayList<>();
        String sqlBusca = " SELECT * FROM " + TABELA_CATEGORIA + " ORDER BY " + NOME_CATEGORIA;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlBusca, null);
        int colID = cursor.getColumnIndex(ID_CATEGORIA);
        int colNOME = cursor.getColumnIndex(NOME_CATEGORIA);
        int colDESC = cursor.getColumnIndex(DESCRICAO_CATEGORIA);

        while (cursor.moveToNext()) {

            Categoria cat = new Categoria(cursor.getString(colNOME), cursor.getString(colDESC));
            cat.setIdcat(colID);
            lista.add(cat);


        }
        cursor.close();
        db.close();
        for(Categoria x : lista){
            System.out.println("teste "+x.getNome() );
            System.out.println("teste2 "+ x.getDescCat() );
        }
        return lista;
    }


    public boolean deletarCategoria(Categoria cat) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(cat.getId())};

        db.delete(TABELA_CATEGORIA,ID_CATEGORIA + " = ? ",
                args);
        listaCategoria.remove(cat);
        db.close();

        return true;
    }

    public void deletarReceita(long id){
        SQLiteDatabase db = getWritableDatabase();
        System.out.println(" id deletar "+id);
        Receita r = buscaReceitaID(id);
        String [] args = {String.valueOf(id)};
        String delReceita = " DELETE FROM " + TABELA_RECEITA + " WHERE " +ID_RECEITA + " = " + id + " ; ";
        db.delete(TABELA_RECEITA,ID_RECEITA + " = ? " , args);
        db.execSQL(delReceita);
        carregarRecBD();


    }



    public void ordenarListaReceitas(){
        Collections.sort(listaReceita, Receita.comparador);
    }

    public boolean inserirReceita(Receita receita){

        boolean status = false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( NOME_RECEITA, receita.getNome());
        // values.put(ID, receita.getId());
        values.put(DESCRICAO_RECEITA, receita.getDescricao());
        values.put(LISTAINGREDIENTES, receita.getListaIngredientes());
        values.put(MODOPREPARO, receita.getModoPreparo());
        values.put(AVALIACAO, receita.getAvaliacao());
        values.put(CHECK,receita.isFeitoAnteriormente());

        values.put(CATEGORIA_ID , receita.getId_categoria());
        long id = db.insert(TABELA_RECEITA,
                null,
                values);
       // receita.setId(id);

        db.close();
        status = true;

        ordenarListaReceitas();

        return status;
    }

    public boolean alterarReceita(long id, Receita novaRec){
        SQLiteDatabase db = getWritableDatabase();
        boolean status = false;
        Receita rec = buscaReceitaID(id);
        ContentValues values = new ContentValues();

        if(rec != null){
            String[] args = {String.valueOf(rec.getIdrec())};
            values.put( NOME_RECEITA, novaRec.getNome());
            // values.put(ID, receita.getId());
            values.put(DESCRICAO_RECEITA, novaRec.getDescricao());
            values.put(LISTAINGREDIENTES, novaRec.getListaIngredientes());
            values.put(MODOPREPARO, novaRec.getModoPreparo());
            values.put(AVALIACAO, novaRec.getAvaliacao());
            values.put(CHECK,novaRec.isFeitoAnteriormente());
            db.update(TABELA_RECEITA, values, ID_RECEITA + " = ? ", args);
            listaReceita.clear();
            carregarReceitasBD();
            status = true;

        }
        db.close();
        return status;

    }


    /*public Receita buscarReceita(String nome){
        for(Receita rec : listaReceita){
            if(rec.getNome() == nome){
                return rec;
            }
        }
        return null;
    }*/

    /*
    public Receita buscaReceitaID(long id){
        List<Receita> listTemp= carregarRecBD();

        for(Receita r : listTemp){
            if(r.getIdrec() == id){
                return r;
            }
        }
        return null;

    }


    public void carregarReceitasBD(){
        this.listaReceita = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sqlBusca = " SELECT * FROM " + TABELA_RECEITA + " ORDER BY " + NOME_RECEITA;
        Cursor cursor = db.rawQuery(sqlBusca, null);
        int colID = cursor.getColumnIndex(ID_RECEITA);
        int colNOME = cursor.getColumnIndex(NOME_RECEITA);
        int colDESC = cursor.getColumnIndex(DESCRICAO_RECEITA);
        int colING = cursor.getColumnIndex(LISTAINGREDIENTES);
        int colMODO = cursor.getColumnIndex(MODOPREPARO);
        int colAVAL = cursor.getColumnIndex(AVALIACAO);
        int colCHECK = cursor.getColumnIndex(CHECK);
        int colCAT = cursor.getColumnIndex(CATEGORIA_ID);

        while(cursor.moveToNext()){
            String nome, desc, listaIng, modoPre, aval, check;
            boolean bCheck;
            nome = cursor.getString(colNOME);
            desc = cursor.getString(colDESC);
            listaIng = cursor.getString(colING);
            modoPre = cursor.getString(colMODO);
            check = cursor.getString(colCHECK);
            if(check.contains("TRUE") || check.contains("T") || check.contains("true")){
                bCheck = true;
            }else{
                bCheck = false;
            }
            aval = cursor.getString(colAVAL);
            long id_cat = cursor.getLong(colCAT);
            Receita rec = new Receita(  nome,desc,listaIng, modoPre, bCheck,aval , id_cat);
            rec.setIdrec(colID);
            listaReceita.add(rec);
        }
        cursor.close();
        db.close();
        ordenarListaReceitas();


    }

    public List<Receita> carregarRecBD(){
        List<Receita> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        listaReceita.clear();
        String sqlBusca = " SELECT * FROM " + TABELA_RECEITA + " ORDER BY " + NOME_RECEITA;
        Cursor cursor = db.rawQuery(sqlBusca, null);
        int colID = cursor.getColumnIndex(ID_RECEITA);
        int colNOME = cursor.getColumnIndex(NOME_RECEITA);
        int colDESC = cursor.getColumnIndex(DESCRICAO_RECEITA);
        int colING = cursor.getColumnIndex(LISTAINGREDIENTES);
        int colMODO = cursor.getColumnIndex(MODOPREPARO);
        int colAVAL = cursor.getColumnIndex(AVALIACAO);
        int colCHECK = cursor.getColumnIndex(CHECK);
        int colCAT = cursor.getColumnIndex(CATEGORIA_ID);

        while(cursor.moveToNext()){
            String nome, desc, listaIng, modoPre, aval, check;
            boolean bCheck;
            nome = cursor.getString(colNOME);
            desc = cursor.getString(colDESC);
            listaIng = cursor.getString(colING);
            modoPre = cursor.getString(colMODO);
            check = cursor.getString(colCHECK);
            if(check.contains("TRUE") || check.contains("T") || check.contains("true")){
                bCheck = true;
            }else{
                bCheck = false;
            }
            aval = cursor.getString(colAVAL);
            long id_cat = cursor.getLong(colCAT);
            Receita rec = new Receita(  nome,desc,listaIng, modoPre, bCheck,aval, id_cat  );
            rec.setIdrec(colID);
            lista.add(rec);
            System.out.println("anterior dao carregarbd "+rec.getNome());
            System.out.println("anterior dao carregar bd "+rec.getIdrec());

        }
        cursor.close();


        return lista;


    }

    public List<Receita> getListaReceitas(){
        return listaReceita;
    }

*/

}
