package net.luanwilliam.cookbook

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.luanwilliam.cookbook.dao.ReceitaDatabase
import net.luanwilliam.cookbook.model.Receita

class AbrirCategorias : AppCompatActivity() {
    private var listviewReceitas: ListView? = null
    private var txtNome: TextView? = null
    private val txtDesc: TextView? = null
    var idCat: Long = 0
    var idRec: Long = 0
    private var listaReceitas: List<Receita>? = null
    private var adapterReceitas: ArrayAdapter<Receita>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abrir_categorias)
        val intent = intent
        val param = intent.extras
        val ide = param!!.getLong("id")
        idCat = ide
        listviewReceitas = findViewById(R.id.listaReceitas)
        val nome = param.getString("nome")
        txtNome = findViewById(R.id.title)
        txtNome!!.text = "Receitas cadastradas na categoria: $nome"
        carregaReceitas(idCat)
        listviewReceitas!!.setOnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id: Long ->
            val rec = parent.getItemAtPosition(position) as Receita
            println("Teste id " + rec.idrec)
            val params = Bundle()
            params.putLong("id", rec.idrec)
            params.putString("nome", rec.nome)
            params.putString("descricao", rec.descricao)
            params.putString("ingredientes", rec.listaIngredientes)
            params.putString("modo_preparo", rec.modoPreparo)
            params.putString("data", rec.data)
            val intencao = Intent(this@AbrirCategorias, AbrirReceitas::class.java)
            intencao.putExtras(params)
            startActivity(intencao)
            finish()
        }
    }

    fun carregaReceitas(idCat: Long) {
        AsyncTask.execute {
            val db = ReceitaDatabase.getDatabase(this@AbrirCategorias)
            listaReceitas = db!!.receitaDAO()!!.listarReceitasIdCategoria(idCat) as List<Receita>?
            runOnUiThread {
                adapterReceitas = ArrayAdapter(
                    this@AbrirCategorias,
                    android.R.layout.simple_list_item_1, listaReceitas!!
                )
                listviewReceitas!!.adapter = adapterReceitas
            }
        }
    }
}