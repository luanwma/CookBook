package net.luanwilliam.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import net.luanwilliam.cookbook.DAO.ReceitaDatabase;
import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listaReceitasLV;
    private List<Receita> listaReceitas;
    private ArrayAdapter<Receita> adapterReceitas;
    //private ArrayAdapter<String> adapterListaString;
   // private ArrayAdapter<Object> adapterlistaObjeto;
    //private List<Object> listaObjeto = new ArrayList<>();
    private List<Categoria> listaCat;

    private List<String> listaImpressao = new ArrayList<>();
   private ImageButton btnNovaReceita;
  // public Controle control  ;
  //  DAO dao = new DAO(this);
    Receita receita;

    private int NOVO = 0;
    private String MODO = "MODO";
    private int ALTERAR = 1;
    private String ID_ALTERAR = "ID";

    static final int ACTIVITY_2_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



       // dao.iniciarBD();
        setContentView(R.layout.activity_main);
        carregaReceitas();
        btnNovaReceita = findViewById(R.id.btnNovaReceita);
     //   btnDeletaReceita = findViewById(R.id.btnDeleteReceita);
      //  btnAlteraReceita = findViewById(R.id.btnAlterarReceita);
        listaReceitasLV = findViewById(R.id.listaReceitas);



        registerForContextMenu(listaReceitasLV);




        listaReceitasLV.setOnItemClickListener(((parent, view, position, id) -> {

            Receita rec = (Receita) parent.getItemAtPosition(position);

          /*  System.out.println("TESTE POSITION NOME "+rec.getNome());
            System.out.println("rec descricao "+rec.getDescricao());
            System.out.println("ingredientes "+rec.getListaIngredientes());
            System.out.println("modo de preparoo "+rec.getModoPreparo()); */


            Bundle param = new Bundle();
            param.putLong("id", rec.getIdrec());
            param.putString("nome", rec.getNome());
            param.putString("descricao", rec.getDescricao());
            param.putString("ingredientes", rec.getListaIngredientes());
            param.putString("modo_preparo", rec.getModoPreparo());
            param.putString("data", rec.getData());

            Intent intent = new Intent(MainActivity.this, AbrirReceitas.class);

            intent.putExtras(param);

            startActivity(intent);


        }));



        btnNovaReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencao = new Intent(MainActivity.this, CadastroReceita.class);
                //Bundle bundle = new Bundle();
                intencao.putExtra(MODO, NOVO);
                startActivity(intencao) ;
                finishActivity(RESULT_OK);

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_2_REQUEST) {
            if (resultCode == RESULT_OK) {

                    carregaReceitas();


            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_categorias, menu);
        return true;

     //   return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.listar_categorias:

                Intent intent1 = new Intent(MainActivity.this, ListarCategorias.class);
                startActivity(intent1);
                finishActivity(1);



                return true;
            case R.id.new_categoria:

                Bundle bundle2 = new Bundle();
                bundle2.putInt(MODO, NOVO);

                Intent intent = new Intent(MainActivity.this, CadastroCategoria.class);
                intent.putExtras(bundle2);
                startActivity(intent );
                finishActivity(1);



          //   Toast.makeText(this, "apareceu", Toast.LENGTH_LONG).show();
                return true;


            case R.id.itemAutoria:

                Intent intent3 = new Intent(MainActivity.this, Autoria.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }





       // return super.onOptionsItemSelected(item);
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        getMenuInflater().inflate(R.menu.menu_context_flutuante, menu);



    }


    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;
        Receita r = adapterReceitas.getItem(pos);




        switch (item.getItemId()){
            case R.id.menuFly_alterar:
                alterarReceita(r);

                return  true;
            case R.id.menuFly_excluir:



                return true;
        }


        return super.onContextItemSelected(item);



    }

    public void carregaReceitas(){

        AsyncTask.execute( () ->{
            ReceitaDatabase db = ReceitaDatabase.getDatabase(MainActivity.this);
            listaReceitas = db.receitaDAO().queryAll();
            listaCat = db.categoriaDAO().queryAll();
           // listaObjeto = new ArrayList<>();
          //  db.close();
            String [] dados ;
           listaImpressao  = new ArrayList<>();

            for(Receita r : listaReceitas  ){
                listaImpressao.add(r.toString());
            //    dados = new String[2];
            //    dados[0] = r.getNome().toString();
            //    dados[1] = String.valueOf(r.getIdrec());
             //   listaObjeto.add(dados);
            }

            MainActivity.this.runOnUiThread( () ->{
                adapterReceitas = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1,listaReceitas );

                listaReceitasLV.setAdapter(adapterReceitas);

            });




        });




    }

    public void alterarReceita(Receita r){
        String msg = "Deseja mesmo alterar a receita? ";
        AlertDialog.Builder caixa = new AlertDialog.Builder(MainActivity.this);

        caixa.setTitle("Alterar");
        caixa.setIcon(android.R.drawable.ic_menu_edit);
        caixa.setMessage(msg);


        caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){


               Bundle param = new Bundle();

                param.putInt(MODO, ALTERAR);
                param.putLong("id", r.getIdrec());
                System.out.println("inserindo id para alterar "+r.getIdrec());
                param.putString("nome",r.getNome() );
                param.putString("descricao", r.getDescricao());
                param.putString("ingredientes", r.getListaIngredientes());
                param.putString("modo_preparo", r.getModoPreparo());
                param.putString("data", r.getData());

                Intent intent = new Intent(MainActivity.this, CadastroReceita.class);
                intent.putExtras(param);
                startActivity(intent);







            }
        });

        caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Receita não deletada", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog dialog = caixa.create();
        caixa.show();





    }






}