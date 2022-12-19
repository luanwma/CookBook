package net.luanwilliam.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AsyncNotedAppOp;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import net.luanwilliam.cookbook.DAO.DAO;
import net.luanwilliam.cookbook.DAO.ReceitaDatabase;
import net.luanwilliam.cookbook.controle.Controle;
import net.luanwilliam.cookbook.model.Categoria;
import net.luanwilliam.cookbook.model.Receita;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CadastroReceita extends AppCompatActivity {
    private Spinner spnGrupos;
    private EditText nome, descricao, ingredientes, modo_preparo;
    private RadioGroup grupoClassificacao;
    private CheckBox check;
    public String avaliacao;
    private Button limpar, cadastrar;
    private ImageButton btnCadCat, btnDelCat, btnAltCat;
    private List<Categoria> listaCategoria;

    private ArrayAdapter<Categoria> adapterCategorias;

 //   DAO dao = new DAO(this);
    private long id = 0;
    public static final int ALTERAR = 1;
    public static final int NOVO = 0;
    public static final String MODO = "MODO";
    public static final String ID_ALTERAR = "ID";
    private Receita rec ;
    private int modo;

    private DatePickerDialog datePickerDialog;
    private Button btnDatePicker;

    private String datePicked = getHoje();

    private long idAlterar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_receita);
        ativarElementos();




        btnDatePicker = findViewById(R.id.dataPicker);
        btnDatePicker.setText(getHoje());
        pushSpinner();
        iniciarDatePicker();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO, NOVO);

     //   rButton(grupoClassificacao);

        if(modo == ALTERAR){
            setTitle("Alterar ");
            ativarElementos();
            subirDados();

             idAlterar = bundle.getLong("id");


            cadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    atualizar();
                }
            });



        }else {
            setTitle("Nova Receita");

            cadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cadastro(NOVO, -1);
                }
            });




           /* AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);

                }
            }); */

        }







                limpar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        limparTela();
                    }
                });

                btnCadCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(MODO, NOVO);

                        Intent intencao = new Intent(CadastroReceita.this, CadastroCategoria.class);
                        intencao.putExtras(bundle);
                        startActivity(intencao);
                    }
                });



               /* btnAltCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Spinner x = findViewById(R.id.grupo_spinner);
                         String  nomec = (String) x.getSelectedItem();
                        System.out.println( "teste 2 nomec "+nomec);
                        String nomeCat = (String) spnGrupos.getSelectedItem();
                        ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);
                        long id = db.categoriaDAO().buscaByNome(nomeCat);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putLong(ID_ALTERAR, id);
                        bundle.putInt(MODO,ALTERAR);


                    }
                }); */





    }

    private void pushSpinner(){


        AsyncTask.execute( () -> {
            ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);
            listaCategoria = db.categoriaDAO().queryAll();
            // db.close();

            //  lista.add("Selecione a categoria");
           // for (Categoria c : listaCategoria) {
            //    lista.add(c.getNome());
           // }

            CadastroReceita.this.runOnUiThread(() -> {
                adapterCategorias = new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, listaCategoria);

              //  adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
                spnGrupos.setAdapter(adapterCategorias);
            });

        });
    }

    private void popularSpinner(){
        ReceitaDatabase bd = ReceitaDatabase.getDatabase(this);
        listaCategoria = bd.categoriaDAO().queryAll();
        bd.close();

        List<String> lista = new ArrayList<>();
       // lista.add("Selecione a categoria");
        for(Categoria c : listaCategoria){
            lista.add(c.getNome());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2 , lista);
        spnGrupos.setAdapter(adapter);
    }



    public void ativarElementos(){
        this.nome = findViewById(R.id.text_nome);
        this.descricao = findViewById(R.id.text_descricao);
        this.ingredientes = findViewById(R.id.ingredientes);
        this.modo_preparo = findViewById(R.id.text_preparo);
        this.grupoClassificacao = findViewById(R.id.radioGroup);
        this.check = findViewById(R.id.checkboxFez);
        rButton(grupoClassificacao);
        this.spnGrupos = findViewById(R.id.grupo_spinner);
        pushSpinner();
        this.limpar = findViewById(R.id.btnLimpar);
        this.cadastrar = findViewById(R.id.btnCadastrar);
        this.btnCadCat = findViewById(R.id.btnCadastrarCategoria);
        this.btnDelCat = findViewById(R.id.btnDeleteCategoria);
        this.btnAltCat = findViewById(R.id.btnAlterarCategoria);
        this.btnDatePicker = findViewById(R.id.dataPicker);
        this.btnDatePicker.setText(getHoje());


    }



    public void rButton(RadioGroup grupo){


        grupo.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener(){

            /**
             * <p>Called when the checked radio button has changed. When the
             * selection is cleared, checkedId is -1.</p>
             *
             * @param group     the group in which the checked radio button has changed
             * @param checkedId the unique identifier of the newly checked radio button
             */
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                String res = button.getText().toString();
                avaliacao = res;
            }
        });
    }

    private void limparTela(){
        this.nome.setText("");
        this.descricao.setText("");
        this.ingredientes.setText("");
        this.modo_preparo.setText("");
      //  grupoClassificacao.clearCheck();
       // popularSpinner(dao.carregarCatBD());




    }

    public static void alterarReceita(Activity act , int requestCode, Receita rec){
        Intent intent = new Intent(act, CadastroReceita.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID_ALTERAR, rec.getIdrec());

        act.startActivityForResult(intent, requestCode);

    }

    public void cadastro(int valor, long id){
        ativarElementos();
        System.out.println(" antes testes ");
        String nomeC = nome.getText().toString();
        String desc = descricao.getText().toString();
        String ing = ingredientes.getText().toString();
        String modo = modo_preparo.getText().toString();
        String cate = spnGrupos.getSelectedItem().toString();
        String data = getDate();
        System.out.println("teste 1 data "+data);
        rButton(grupoClassificacao);

        boolean checked = false;
        if(check.isChecked()){
            checked = true;
        }
       // System.out.println("teste cate "+cate);

        System.out.println("testes ");

        System.out.println("nome cadastros " + nomeC);
        System.out.println("desc "+desc);
        System.out.println("Categoria "+cate);
        System.out.println("Avaliaçao "+ avaliacao);
        System.out.println("teste check "+checked);
        Categoria cat = (Categoria)spnGrupos.getSelectedItem();
        System.out.println("teste categoria "+ cat.getNome());



        ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);

        if(valor == NOVO){
           // Categoria c = encontrarCat(cate);
          //  System.out.println("testar categoria "+c.getNome());
            Receita rec = new Receita(nomeC, desc, ing, modo, checked,avaliacao, cat.getIdcat() );
            rec.setData(data);
            AsyncTask.execute( () ->{
                //ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);
                db.receitaDAO().insert(rec);

              //  db.close();


            });

            Toast.makeText(CadastroReceita.this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show();



        }

        Intent intent = new Intent(CadastroReceita.this, MainActivity.class);
        startActivity(intent);
        finishActivity(RESULT_OK);



    }


    public void atualizar(){


        System.out.println(" antes testes ");
        String nomeC = nome.getText().toString();
        String desc = descricao.getText().toString();
        String ing = ingredientes.getText().toString();
        String modo = modo_preparo.getText().toString();
        String cate = spnGrupos.getSelectedItem().toString();
        String data = getDate();
        System.out.println("teste 1 data "+data);
        rButton(grupoClassificacao);

        boolean checked = false;
        if(check.isChecked()){
            checked = true;
        }
        // System.out.println("teste cate "+cate);

        System.out.println("testes ");

        System.out.println("nome cadastros " + nomeC);
        System.out.println("desc "+desc);
        System.out.println("Categoria "+cate);
        System.out.println("Avaliaçao "+ avaliacao);
        System.out.println("teste check "+checked);
        Categoria cat = (Categoria)spnGrupos.getSelectedItem();
        System.out.println("teste categoria "+ cat.getNome());



        ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);

        Receita upRec = new Receita(nomeC, desc, ing, modo, checked,avaliacao, cat.getIdcat());
        upRec.setIdrec(idAlterar);
        upRec.setData(data);


        AsyncTask.execute( () ->{

            db.receitaDAO().update(upRec);
        });

        Toast.makeText(CadastroReceita.this, "Alterado com Sucesso", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(CadastroReceita.this, MainActivity.class);
        startActivity(intent);
        finishActivity(RESULT_OK);



    }

    public void subirDados(){


        Intent intent = getIntent();

        Bundle param = intent.getExtras();
        long id = param.getLong("id");

        String nome = param.getString("nome");
        String desc = param.getString("descricao");
        String ingr = param.getString("ingredientes");
        String modoP = param.getString("modo_preparo");
        String data = param.getString("data");


        this.nome.setText(nome);
        this.descricao.setText(desc);
        this.ingredientes.setText(ingr);
        this.modo_preparo.setText(modoP);

    }

    public Categoria encontrarCat(String nome){


    AsyncTask.execute( () -> {
        ReceitaDatabase db = ReceitaDatabase.getDatabase(CadastroReceita.this);
        listaCategoria = db.categoriaDAO().queryAll();

    });

        for(Categoria c : listaCategoria){
            if(c.getNome().equals(nome)){
                return c;
            }
        }
        return null;



    }

    public void mostrarDatePicker(View view) {
        datePickerDialog.show();


    }

    public void iniciarDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;


                String data = makeDateString(dayOfMonth, month, year);

                btnDatePicker.setText(data);
                setDate(data);

            }
        };
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);

        int estilo = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog =  new DatePickerDialog(this, estilo, dateSetListener, dia, mes, ano);

    }

    private String makeDateString(int dayOfMonth, int month, int year) {

        return dayOfMonth + "/" + getMonthFormat(month )+ "/"+ year;
    }

    private String getMonthFormat(int month) {

        if(month == 1){
            return "JAN";
        }
        if(month == 2){
            return "FEV";
        }if(month == 3){
            return "MAR";
        }if(month == 4){
            return "ABR";
        }if(month == 5){
            return "MAIO";
        }if(month == 6){
            return "JUN";
        }if(month == 7){
            return "JUL";
        }if(month == 8){
            return "AGOS";
        }if(month == 9){
            return "SET";
        }if(month == 10){
            return "OUT";
        }if(month == 11){
            return "NOV";
        }if(month == 12){
            return "DEZ";
        }
        return "JAN";


    }


    private String getHoje() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        mes += 1;
        return makeDateString(dia, mes, ano);


    }

    public void setDate(String date){
        datePicked = date;
    }
    public String getDate(){
        return datePicked;
    }

}