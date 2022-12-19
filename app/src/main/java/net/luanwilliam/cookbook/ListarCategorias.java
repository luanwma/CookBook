package net.luanwilliam.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.luanwilliam.cookbook.DAO.ReceitaDatabase;
import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class ListarCategorias extends AppCompatActivity {


    private long id;
    private List<Categoria> listaCategorias;
    private ListView listaCategoriasLV;
    private ArrayAdapter<Categoria> adapterCategorias;

    private Categoria categoria;

    private int NOVO = 0;
    private String MODO = "MODO";
    private int ALTERAR = 1;

    private int        posicaoSelecionada = -1;
    private View       viewSelecionada;


    private androidx.appcompat.view.ActionMode actionMode;

    private androidx.appcompat.view.ActionMode.Callback mActionModeCallback = new androidx.appcompat.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.menu_contexto_categoria_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
            switch(item.getItemId()){
                case R.id.menuItem_cat_del:
                      deleteCategoria(categoria);


                    mode.finish();
                    return true;

                case R.id.menuItem_cat_alt:
                    alterarCategoria(categoria);
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode         = null;
            viewSelecionada    = null;

            listaCategoriasLV.setEnabled(true);
            carregaCategorias();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_categorias);


        listaCategoriasLV = findViewById(R.id.lista_categoria);
        listaCategoriasLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        carregaCategorias();
        registerForContextMenu(listaCategoriasLV);

        listaCategoriasLV.setOnItemClickListener(((parent, view, position, id) -> {

            Categoria cat = (Categoria) parent.getItemAtPosition(position);

            posicaoSelecionada = position;



            Bundle param = new Bundle();
            param.putLong("id", cat.getIdcat());
            param.putString("nome", cat.getNome());
            param.putString("descricao", cat.getDescCat());


            Intent intent = new Intent(ListarCategorias.this, AbrirCategorias.class);

            intent.putExtras(param);

            startActivity(intent);


        }));

        listaCategoriasLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                 categoria = (Categoria) parent.getItemAtPosition(position);


                if(actionMode != null){
                    return false;
                }

                posicaoSelecionada = position;
                view.setBackgroundColor(Color.RED);
                viewSelecionada = view;
                listaCategoriasLV.setEnabled(false);
                actionMode =  startSupportActionMode(mActionModeCallback);
                return true;

            }
        });





    }


    public void carregaCategorias(){

        AsyncTask.execute( () ->{
            ReceitaDatabase db = ReceitaDatabase.getDatabase(ListarCategorias.this);
            listaCategorias = db.categoriaDAO().queryAll();



            ListarCategorias.this.runOnUiThread( () ->{
                adapterCategorias = new ArrayAdapter<>(ListarCategorias.this,
                        android.R.layout.simple_list_item_1,listaCategorias );

                listaCategoriasLV.setAdapter(adapterCategorias);

            });




        });




    }




    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return this.id;
    }


/*

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        getMenuInflater().inflate(R.menu.menu_contexto_categorias, menu);



    }
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;
        Categoria cat = adapterCategorias.getItem(pos);




        switch (item.getItemId()){
            case R.id.itemMenu_cat_alterar:
               // alterarReceita(car);


                return  true;


            default:
                return super.onContextItemSelected(item);

        }






    }
    */

    public void alterarCategoria(Categoria categoria){
        String msg = " Deseja mesmo alterar a categoria? ";
        AlertDialog.Builder caixa = new AlertDialog.Builder(ListarCategorias.this);

        caixa.setTitle("Alterar : "+categoria.getNome());
        caixa.setIcon(android.R.drawable.ic_menu_edit);
        caixa.setMessage(msg);

        caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                Bundle bundle = new Bundle();
                bundle.putInt(MODO, ALTERAR);
                bundle.putLong("id", categoria.getIdcat());
                bundle.putString("nome", categoria.getNome());
                bundle.putString("descricao", categoria.getDescCat());

                Intent intent = new Intent(ListarCategorias.this, CadastroCategoria.class);
                intent.putExtras(bundle);
                startActivity(intent);



                finishActivity(1);



            }


        });

        caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListarCategorias.this, "Categoria não deletada", Toast.LENGTH_LONG).show();

            }
        });


        AlertDialog dialog = caixa.create();
        caixa.show();
    }

    public void deleteCategoria(Categoria categoria){
        String msg = "Ao deletar uma categoria deletará todas as receitas inclusas nessa categoria. Deseja mesmo deletar a categoria? ";
        AlertDialog.Builder caixa = new AlertDialog.Builder(ListarCategorias.this);

        caixa.setTitle("Excluir");
        caixa.setIcon(android.R.drawable.ic_menu_delete);
        caixa.setMessage(msg);

        caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                ReceitaDatabase db = ReceitaDatabase.getDatabase(ListarCategorias.this);


                AsyncTask.execute( () -> {
                    db.receitaDAO().deleteByCat(categoria.getIdcat());
                });

                AsyncTask.execute(() -> {
                 //  Categoria categoria = db.categoriaDAO().queryIdCategoria(id);

                    db.categoriaDAO().delete(categoria);



                });


                Toast.makeText(ListarCategorias.this, "Categoria  deletada", Toast.LENGTH_LONG).show();

                Intent resultIntent =  new Intent(ListarCategorias.this, MainActivity.class);

                finishActivity(1);
                startActivity(resultIntent );
                // finish();

            }


        });

        caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListarCategorias.this, "Categoria não deletada", Toast.LENGTH_LONG).show();

            }
        });


        AlertDialog dialog = caixa.create();
        caixa.show();
    }



    public void functionDelete( long id){
        System.out.println("teste dentro do function "+id);

        String msg = "Deseja mesmo excluir a Categoria? ";
        AlertDialog.Builder caixa = new AlertDialog.Builder(ListarCategorias.this);

        caixa.setTitle("Excluir");
        caixa.setIcon(android.R.drawable.ic_menu_delete);
        caixa.setMessage(msg);

        caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                ReceitaDatabase db = ReceitaDatabase.getDatabase(ListarCategorias.this);


                AsyncTask.execute(() -> {
                    Categoria categoria = db.categoriaDAO().queryIdCategoria(id);

                    db.categoriaDAO().delete(categoria);



                });


                Toast.makeText(ListarCategorias.this, "Categoria  deletada", Toast.LENGTH_LONG).show();

                Intent resultIntent =  new Intent(ListarCategorias.this, MainActivity.class);

                finishActivity(1);
                startActivity(resultIntent );
                // finish();

            }


        });

        caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListarCategorias.this, "Categoria não deletada", Toast.LENGTH_LONG).show();

            }
        });


        AlertDialog dialog = caixa.create();
        caixa.show();

    }



}