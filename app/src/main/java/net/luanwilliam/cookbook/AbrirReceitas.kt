package net.luanwilliam.cookbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.luanwilliam.cookbook.DAO.ReceitaDatabase;
import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

public class AbrirReceitas extends AppCompatActivity {

    TextView title, descricao, ingredientes, modoPreparo, txtViewdata;

    private ImageButton btnAlterar, btnDeletar;

    private int ALTERAR = 1;

    private String MODO = "MODO";
    private long id ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_receitas);

        title = findViewById(R.id.title);
        descricao = findViewById(R.id.txtDescricao);
        ingredientes = findViewById(R.id.txtIngredientes);
        modoPreparo = findViewById(R.id.txtModoPreparo);
        btnAlterar = findViewById(R.id.btnAlterarReceita);
        btnDeletar = findViewById(R.id.btnDeleteReceita);

        txtViewdata = findViewById(R.id.textData);



        Intent intent = getIntent();

        Bundle param = intent.getExtras();
        long id = param.getLong("id");
        setId(id);
        String nome = param.getString("nome");
        String desc = param.getString("descricao");
        String ingr = param.getString("ingredientes");
        String modoP = param.getString("modo_preparo");
        String data = param.getString("data");


        title.setText(nome);
        descricao.setText(desc);
        ingredientes.setText(ingr);
        modoPreparo.setText(modoP);
        txtViewdata.setText("Feito em: "+data);








        btnDeletar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String msg = "Deseja mesmo excluir a receita? ";
                AlertDialog.Builder caixa = new AlertDialog.Builder(AbrirReceitas.this);

                caixa.setTitle("Excluir");
                caixa.setIcon(android.R.drawable.ic_menu_delete);
                caixa.setMessage(msg);

                caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        ReceitaDatabase db = ReceitaDatabase.getDatabase(AbrirReceitas.this);


                        AsyncTask.execute(() -> {
                            Receita rec = db.receitaDAO().queryIdReceita(id);

                            db.receitaDAO().delete(rec);

                        });


                        Toast.makeText(AbrirReceitas.this, "Receita  deletada", Toast.LENGTH_LONG).show();

                        Intent resultIntent =  new Intent(AbrirReceitas.this, MainActivity.class);

                        finishActivity(1);
                       startActivity(resultIntent );
                       finish();

                    }
                });

                caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AbrirReceitas.this, "Receita não deletada", Toast.LENGTH_LONG).show();

                        }
                });


            AlertDialog dialog = caixa.create();
            caixa.show();

            }


        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String msg = "Deseja mesmo alterar a receita? ";
                AlertDialog.Builder caixa = new AlertDialog.Builder(AbrirReceitas.this);

                caixa.setTitle("Alterar");
                caixa.setIcon(android.R.drawable.ic_menu_edit);
                caixa.setMessage(msg);


                caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which){

                   //     ReceitaDatabase db = ReceitaDatabase.getDatabase(AbrirReceitas.this);


                     //   Receita rec = db.receitaDAO().queryIdReceita(id);
                        Bundle param = new Bundle();

                        param.putInt(MODO, ALTERAR);
                        param.putLong("id", id);
                        System.out.println("inserindo id para alterar "+id);
                        param.putString("title", nome);
                        param.putString("descricao", desc);
                        param.putString("ingredientes", ingr);
                        param.putString("modo_preparo", modoP);
                        param.putString("data", data);

                        Intent intent = new Intent(AbrirReceitas.this, CadastroReceita.class);
                        intent.putExtras(param);
                        startActivity(intent);





                    }
                });

                caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(AbrirReceitas.this, "Receita não deletada", Toast.LENGTH_LONG).show();

                        }
                });

                AlertDialog dialog = caixa.create();
                caixa.show();



                //selecionar receita, encontrar receita, enviar strings
            /*    Intent intencao = new Intent(AbrirReceitas.this, CadastroReceita.class);
                Bundle bundle = new Bundle();
                bundle.putInt(MODO, ALTERAR);
                //  bundle.putLong(ID_ALTERAR, receita.getId());
                intencao.putExtras(bundle);
                startActivity(intencao);*/



            }
        });


    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return this.id;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_contexto_receitas, menu);

        return true;
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        switch (item.getItemId()){
            case R.id.menuItem_receitas_alterar:r:



                return true;
            case R.id.menuItem_receitas_excluir:


                functionDelete(getId());



                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void functionDelete( long id){
        System.out.println("teste dentro do function "+id);

        String msg = "Deseja mesmo excluir a receita? ";
        AlertDialog.Builder caixa = new AlertDialog.Builder(AbrirReceitas.this);

        caixa.setTitle("Excluir");
        caixa.setIcon(android.R.drawable.ic_menu_delete);
        caixa.setMessage(msg);

        caixa.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                ReceitaDatabase db = ReceitaDatabase.getDatabase(AbrirReceitas.this);


                AsyncTask.execute(() -> {
                    Receita rec = db.receitaDAO().queryIdReceita(id);

                    db.receitaDAO().delete(rec);


                });


                Toast.makeText(AbrirReceitas.this, "Receita  deletada", Toast.LENGTH_LONG).show();

               Intent resultIntent =  new Intent(AbrirReceitas.this, MainActivity.class);

                finishActivity(1);
                startActivity(resultIntent );
               // finish();

            }


        });

        caixa.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AbrirReceitas.this, "Receita não deletada", Toast.LENGTH_LONG).show();

            }
        });


        AlertDialog dialog = caixa.create();
        caixa.show();

    }




}
