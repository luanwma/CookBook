package net.luanwilliam.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.luanwilliam.cookbook.DAO.DAO;
import net.luanwilliam.cookbook.DAO.ReceitaDatabase;

import net.luanwilliam.cookbook.model.Categoria;

public class CadastroCategoria extends AppCompatActivity {


    public TextView textNome, textDesc;
    public Button btnCadastrar;
    public static final int NOVO = 0;
    public static final int ALTERAR = 1;
    public static final String ID_ALTERAR = "ID";
    public static final String MODO = "MODO";

    private long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_categoria);
        Intent intent = getIntent();
       Bundle bundle = intent.getExtras();
        int modo = bundle.getInt(MODO);




        textNome = findViewById(R.id.text_nome);
        textDesc = findViewById(R.id.text_descricao);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroCategoria.this);
       // db.categoriaDAO().queryIdCategoria(id);


        if(modo == ALTERAR){
          //  ativarComponentes();
            subirDados();
           // String nome = textNome.getText().toString();
           // String desc = textDesc.getText().toString();
           // Categoria nova = new Categoria(nome, desc);
           // nova.setIdcat(id);
            btnCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   atualizarCategoria();

                }
            });


        }else{

            String nome = textNome.getText().toString();
            String desc = textDesc.getText().toString();
            Categoria nova = new Categoria(nome, desc);

            btnCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cadastrarCat();
                }
            });
        }





       // CategoriaDatabase data = CategoriaDatabase.getInstance(this);
       // control = (Controle) getIntent().getSerializableExtra("controle");

    }

/*
    public void ativarComponentes(){
        textNome = findViewById(R.id.text_nome);
        textDesc = findViewById(R.id.text_descricao);
        btnCadastrar = findViewById(R.id.btnCadastrar);

    } */

    public void cadastrarCat(){
        String sNome, sDesc;
        sNome = textNome.getText().toString();
        sDesc = textDesc.getText().toString();
        Categoria cat = new Categoria(sNome, sDesc);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroCategoria.this);

                db.categoriaDAO().insert(cat);
             /*   if(modo == NOVO){
                    db.categoriaDAO().insert(cat);
                }else {
                    db.categoriaDAO().update(cat);
                }
                setResult(Activity.RESULT_OK);*/

            }
        });
        Toast.makeText(CadastroCategoria.this, "Sucesso", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CadastroCategoria.this, MainActivity.class);
        startActivity(intent);
        finish();



    }

    public void atualizarCategoria(){
        String nome = textNome.getText().toString();
         String desc = textDesc.getText().toString();
         Categoria nova = new Categoria(nome, desc);
         nova.setIdcat(getId());

         AsyncTask.execute( () ->{
             ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroCategoria.this);
             db.categoriaDAO().update(nova);

         });


        Toast.makeText(CadastroCategoria.this, "Alterado com Sucesso", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CadastroCategoria.this, MainActivity.class);
        startActivity(intent);

        finish();
    }




    public void subirDados(){
        Intent intent = getIntent();

        Bundle param = intent.getExtras();
        long id = param.getLong("id");
        setId(id);
        String nome = param.getString("nome");
        String desc = param.getString("descricao");

        this.textNome.setText(nome);
        this.textDesc.setText(desc);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}