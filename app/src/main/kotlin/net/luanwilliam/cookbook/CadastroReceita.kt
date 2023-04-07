package net.luanwilliam.cookbook

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.luanwilliam.cookbook.dao.ReceitaDatabase
import net.luanwilliam.cookbook.model.Categoria
import net.luanwilliam.cookbook.model.Receita
import java.util.*

class CadastroReceita : AppCompatActivity() {
    private var spnGrupos: Spinner? = null
    private var nome: EditText? = null
    private var descricao: EditText? = null
    private var ingredientes: EditText? = null
    private var modo_preparo: EditText? = null
    private var grupoClassificacao: RadioGroup? = null
    private var check: CheckBox? = null
    var avaliacao: String? = ""
    private var limpar: Button? = null
    private var cadastrar: Button? = null
    private var btnCadCat: ImageButton? = null
    private var btnDelCat: ImageButton? = null
    private var btnAltCat: ImageButton? = null
    private var listaCategoria: List<Categoria>? = null
    private var adapterCategorias: ArrayAdapter<Categoria>? = null

    private var modo = 0
    private var datePickerDialog: DatePickerDialog? = null
    private var btnDatePicker: Button? = null
    var date = hoje
    private var idAlterar: Long = 0

    companion object {
        const val ALTERAR = 1
        const val NOVO = 0
        const val MODO = "MODO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_receita)
        ativarElementos()
        iniciarDatePicker()
        btnDatePicker = findViewById(R.id.dataPicker)
        pushSpinner()
        val intent = intent
        val bundle = intent.extras
        modo = bundle!!.getInt(MODO, NOVO)

        if (modo == ALTERAR) {
            title = "Alterar "
            ativarElementos()
            subirDados()
            idAlterar = bundle.getLong("id")
            cadastrar!!.setOnClickListener { atualizar() }
        } else {
            title = "Nova Receita"
            cadastrar!!.setOnClickListener { cadastro() }
        }
        limpar!!.setOnClickListener { limparTela() }
        btnCadCat!!.setOnClickListener {
            val otherBundle = Bundle()
            otherBundle.putInt(MODO, NOVO)
            val intencao = Intent(this@CadastroReceita, CadastroCategoria::class.java)
            intencao.putExtras(otherBundle)
            startActivity(intencao)
        }
    }

    private fun pushSpinner() {
        AsyncTask.execute {
            val db = ReceitaDatabase.getDatabase(this@CadastroReceita)
            listaCategoria = db!!.categoriaDAO().queryAll() as List<Categoria>?
            runOnUiThread {
                adapterCategorias = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaCategoria!!)
                spnGrupos!!.adapter = adapterCategorias
            }
        }
    }

    private fun popularSpinner() {
        val bd = ReceitaDatabase.getDatabase(this)
        listaCategoria = bd!!.categoriaDAO().queryAll() as List<Categoria>?
        bd.close()
        val lista: MutableList<String> = ArrayList()
        for (c in listaCategoria!!) {
            lista.add(c.nome)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, lista)
        spnGrupos!!.adapter = adapter
    }

    private fun ativarElementos() {
        nome = findViewById(R.id.text_nome)
        descricao = findViewById(R.id.text_descricao)
        ingredientes = findViewById(R.id.ingredientes)
        modo_preparo = findViewById(R.id.text_preparo)
        grupoClassificacao = findViewById(R.id.radioGroup)
        check = findViewById(R.id.checkboxFez)
        rButton(grupoClassificacao)
        spnGrupos = findViewById(R.id.grupo_spinner)
        pushSpinner()
        limpar = findViewById(R.id.btnLimpar)
        cadastrar = findViewById(R.id.btnCadastrar)
        btnCadCat = findViewById(R.id.btnCadastrarCategoria)
        btnDelCat = findViewById(R.id.btnDeleteCategoria)
        btnAltCat = findViewById(R.id.btnAlterarCategoria)
        btnDatePicker = findViewById(R.id.dataPicker)
        btnDatePicker!!.text = hoje
    }

    private fun rButton(grupo: RadioGroup?) {
        grupo!!.setOnCheckedChangeListener { group, checkedId ->
            val button = group.findViewById<View>(checkedId) as RadioButton
            val res = button.text.toString()
            avaliacao = res
        }
    }

    private fun limparTela() {
        nome!!.setText("")
        descricao!!.setText("")
        ingredientes!!.setText("")
        modo_preparo!!.setText("")
    }

    private fun cadastro() {
        ativarElementos()
        println(" antes testes ")
        val nomeC = nome!!.text.toString()
        val desc = descricao!!.text.toString()
        val ing = ingredientes!!.text.toString()
        val modo = modo_preparo!!.text.toString()
        val cate = spnGrupos!!.selectedItem.toString()
        val data = date
        println("teste 1 data $data")
        rButton(grupoClassificacao)
        var checked = false
        if (check!!.isChecked) {
            checked = true
        }
        println("testes ")
        println("nome cadastros $nomeC")
        println("desc $desc")
        println("Categoria $cate")
        println("Avaliaçao $avaliacao")
        println("teste check $checked")
        val cat = spnGrupos!!.selectedItem as Categoria
        println("teste categoria " + cat.nome)
        val db = ReceitaDatabase.getDatabase(this@CadastroReceita)

        val rec = Receita(nomeC, desc, ing, modo, checked, avaliacao ?: "", cat.idcat)
        rec.data = data
        AsyncTask.execute {
            db!!.receitaDAO()!!.insert(rec)
        }
        Toast.makeText(this@CadastroReceita, "Cadastrado com sucesso", Toast.LENGTH_LONG).show()

        val intent = Intent(this@CadastroReceita, MainActivity::class.java)
        startActivity(intent)
        finishActivity(RESULT_OK)
    }

    fun atualizar() {
        println(" antes testes ")
        val nomeC = nome!!.text.toString()
        val desc = descricao!!.text.toString()
        val ing = ingredientes!!.text.toString()
        val modo = modo_preparo!!.text.toString()
        val cate = spnGrupos!!.selectedItem.toString()
        val data = date
        println("teste 1 data $data")
        rButton(grupoClassificacao)
        var checked = false
        if (check!!.isChecked) {
            checked = true
        }

        println("testes ")
        println("nome cadastros $nomeC")
        println("desc $desc")
        println("Categoria $cate")
        println("Avaliaçao $avaliacao")
        println("teste check $checked")
        val cat = spnGrupos!!.selectedItem as Categoria
        println("teste categoria " + cat.nome)
        val db = ReceitaDatabase.getDatabase(this@CadastroReceita)
        val upRec = Receita(nomeC, desc, ing, modo, checked, avaliacao!!, cat.idcat)
        upRec.idrec = idAlterar
        upRec.data = data
        AsyncTask.execute { db!!.receitaDAO()!!.update(upRec) }
        Toast.makeText(this@CadastroReceita, "Alterado com Sucesso", Toast.LENGTH_LONG).show()
        val intent = Intent(this@CadastroReceita, MainActivity::class.java)
        startActivity(intent)
        finishActivity(RESULT_OK)
    }

    private fun subirDados() {
        val intent = intent
        val param = intent.extras
        val id = param!!.getLong("id")
        val nome = param.getString("nome")
        val desc = param.getString("descricao")
        val ingr = param.getString("ingredientes")
        val modoP = param.getString("modo_preparo")
        val data = param.getString("data")
        this.nome!!.setText(nome)
        descricao!!.setText(desc)
        ingredientes!!.setText(ingr)
        modo_preparo!!.setText(modoP)
    }

    fun mostrarDatePicker(view: View?) {
        datePickerDialog!!.show()
    }

    private fun iniciarDatePicker() {
        btnDatePicker!!.text = date
        val dateSetListener = OnDateSetListener { view, year, month, dayOfMonth ->
            var month = month
            month += 1
            val data = makeDateString(dayOfMonth, month, year)
            btnDatePicker!!.text = data
            //this.datePicked = data //TODO: Is this needed ?
        }

        val calendar = Calendar.getInstance()
        val ano = calendar[Calendar.YEAR]
        val dia = calendar[Calendar.DAY_OF_MONTH]
        val mes = calendar[Calendar.MONTH]
        val estilo = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, estilo, dateSetListener, dia, mes, ano)
    }

    private fun makeDateString(dayOfMonth: Int, month: Int, year: Int): String {
        return dayOfMonth.toString() + "/" + getMonthFormat(month) + "/" + year
    }

    private fun getMonthFormat(month: Int): String { //TODO: :(
        if (month == 1) {
            return "JAN"
        }
        if (month == 2) {
            return "FEV"
        }
        if (month == 3) {
            return "MAR"
        }
        if (month == 4) {
            return "ABR"
        }
        if (month == 5) {
            return "MAIO"
        }
        if (month == 6) {
            return "JUN"
        }
        if (month == 7) {
            return "JUL"
        }
        if (month == 8) {
            return "AGOS"
        }
        if (month == 9) {
            return "SET"
        }
        if (month == 10) {
            return "OUT"
        }
        if (month == 11) {
            return "NOV"
        }
        return if (month == 12) {
            "DEZ"
        } else "JAN"
    }

    private val hoje: String
        get() {
            val calendar = Calendar.getInstance()
            val ano = calendar[Calendar.YEAR]
            val dia = calendar[Calendar.DAY_OF_MONTH]
            var mes = calendar[Calendar.MONTH]
            mes += 1
            return makeDateString(dia, mes, ano)
        }
}