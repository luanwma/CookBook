package net.luanwilliam.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.luanwilliam.cookbook.DAO.ReceitaDatabase;
import net.luanwilliam.cookbook.model.Receita;

import java.util.ArrayList;
import java.util.List;

public class AbrirCategorias extends AppCompatActivity {


    private  ListView listviewReceitas;
    private TextView txtNome, txtDesc;
    private long idCat, idRec;
    private List<Receita> listaReceitas;
    private ArrayAdapter<Receita> adapterReceitas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_categorias);
        Intent intent = getIntent();

        Bundle param = intent.getExtras();
        long ide = param.getLong("id");
        setIdCat(ide);

        listviewReceitas = findViewById(R.id.listaReceitas);

        String nome = param.getString("nome");
        txtNome = findViewById(R.id.title);

        txtNome.setText("Receitas cadastradas na categoria: "+nome);

        String desc = param.getString("descricao");
        carregaReceitas(getIdCat());




     listviewReceitas.setOnItemClickListener(((parent, view, position, id) -> {

            Receita rec = (Receita) parent.getItemAtPosition(position);


         System.out.println("Teste id "+rec.getIdrec());

          /*  System.out.println("TESTE POSITION NOME "+rec.getNome());
            System.out.println("rec descricao "+rec.getDescricao());
            System.out.println("ingredientes "+rec.getListaIngredientes());
            System.out.println("modo de preparoo "+rec.getModoPreparo()); */


            Bundle params = new Bundle();
            params.putLong("id", rec.getIdrec());
            params.putString("nome", rec.getNome());
            params.putString("descricao", rec.getDescricao());
            params.putString("ingredientes", rec.getListaIngredientes());
            params.putString("modo_preparo", rec.getModoPreparo());
            params.putString("data", rec.getData());

            Intent intencao = new Intent(AbrirCategorias.this, AbrirReceitas.class);

            intencao.putExtras(params);

            startActivity(intencao);
            finish();


        }));


    }

    public long getIdCat() {
        return idCat;
    }

    public void setIdCat(long idCat) {
        this.idCat = idCat;
    }

    public long getIdRec() {
        return idRec;
    }

    public void setIdRec(long idRec) {
        this.idRec = idRec;
    }

    public void carregaReceitas(long idCat){

        AsyncTask.execute( () ->{
            ReceitaDatabase db = ReceitaDatabase.getDatabase(AbrirCategorias.this);
            listaReceitas = db.receitaDAO().listarReceitasIdCategoria(idCat);




            AbrirCategorias.this.runOnUiThread( () ->{
                adapterReceitas = new ArrayAdapter<>(AbrirCategorias.this,
                        android.R.layout.simple_list_item_1,listaReceitas );

                listviewReceitas.setAdapter(adapterReceitas);

            });




        });




    }


}