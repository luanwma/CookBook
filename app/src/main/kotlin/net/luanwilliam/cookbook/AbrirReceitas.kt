package net.luanwilliam.cookbook

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import net.luanwilliam.cookbook.dao.ReceitaDatabase

class AbrirReceitas : AppCompatActivity() {
    var title: TextView? = null
    var descricao: TextView? = null
    var ingredientes: TextView? = null
    var modoPreparo: TextView? = null
    var txtViewdata: TextView? = null
    private var btnAlterar: ImageButton? = null
    private var btnDeletar: ImageButton? = null
    private val ALTERAR = 1
    private val MODO = "MODO"
    var id: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abrir_receitas)
        title = findViewById(R.id.title)
        descricao = findViewById(R.id.txtDescricao)
        ingredientes = findViewById(R.id.txtIngredientes)
        modoPreparo = findViewById(R.id.txtModoPreparo)
        btnAlterar = findViewById(R.id.btnAlterarReceita)
        btnDeletar = findViewById(R.id.btnDeleteReceita)
        txtViewdata = findViewById(R.id.textData)
        val intent = intent
        val param = intent.extras
        val id = param!!.getLong("id")
        this.id = id
        val nome = param.getString("nome")
        val desc = param.getString("descricao")
        val ingr = param.getString("ingredientes")
        val modoP = param.getString("modo_preparo")
        val data = param.getString("data")
        title!!.text = nome
        descricao!!.text = desc
        ingredientes!!.text = ingr
        modoPreparo!!.text = modoP
        txtViewdata!!.text = "Feito em: $data"
        btnDeletar!!.setOnClickListener {
            val msg = "Deseja mesmo excluir a receita? "
            val caixa = AlertDialog.Builder(this@AbrirReceitas)
            caixa.setTitle("Excluir")
            caixa.setIcon(android.R.drawable.ic_menu_delete)
            caixa.setMessage(msg)
            caixa.setPositiveButton("Sim") { dialog, which ->
                val db = ReceitaDatabase.getDatabase(this@AbrirReceitas)
                AsyncTask.execute {
                    val rec = db!!.receitaDAO()!!.queryIdReceita(id)
                    db.receitaDAO()!!.delete(rec)
                }
                Toast.makeText(this@AbrirReceitas, "Receita  deletada", Toast.LENGTH_LONG).show()
                val resultIntent = Intent(this@AbrirReceitas, MainActivity::class.java)
                finishActivity(1)
                startActivity(resultIntent)
                finish()
            }
            caixa.setNegativeButton("Não") { dialog, which -> Toast.makeText(this@AbrirReceitas, "Receita não deletada", Toast.LENGTH_LONG).show() }
            caixa.show()
        }
        btnAlterar!!.setOnClickListener(View.OnClickListener {
            val msg = "Deseja mesmo alterar a receita? "
            val caixa = AlertDialog.Builder(this@AbrirReceitas)
            caixa.setTitle("Alterar")
            caixa.setIcon(android.R.drawable.ic_menu_edit)
            caixa.setMessage(msg)
            caixa.setPositiveButton("Sim") { dialog, which ->

                val param = Bundle()
                param.putInt(MODO, ALTERAR)
                param.putLong("id", id)
                println("inserindo id para alterar $id")
                param.putString("title", nome)
                param.putString("descricao", desc)
                param.putString("ingredientes", ingr)
                param.putString("modo_preparo", modoP)
                param.putString("data", data)
                val intent = Intent(this@AbrirReceitas, CadastroReceita::class.java)
                intent.putExtras(param)
                startActivity(intent)
            }
            caixa.setNegativeButton("Não") { dialog, which -> Toast.makeText(this@AbrirReceitas, "Receita não deletada", Toast.LENGTH_LONG).show() }
            caixa.show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_contexto_receitas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItem_receitas_alterar -> {
                functionDelete(id)
                true
            }
            R.id.menuItem_receitas_excluir -> {
                functionDelete(id)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun functionDelete(id: Long) {
        println("teste dentro do function $id")
        val msg = "Deseja mesmo excluir a receita? "
        val caixa = AlertDialog.Builder(this@AbrirReceitas)
        caixa.setTitle("Excluir")
        caixa.setIcon(android.R.drawable.ic_menu_delete)
        caixa.setMessage(msg)
        caixa.setPositiveButton("Sim") { dialog, which ->
            val db = ReceitaDatabase.getDatabase(this@AbrirReceitas)
            AsyncTask.execute {
                val rec = db!!.receitaDAO()!!.queryIdReceita(id)
                db.receitaDAO()!!.delete(rec)
            }
            Toast.makeText(this@AbrirReceitas, "Receita  deletada", Toast.LENGTH_LONG).show()
            val resultIntent = Intent(this@AbrirReceitas, MainActivity::class.java)
            finishActivity(1)
            startActivity(resultIntent)
        }
        caixa.setNegativeButton("Não") { dialog, which -> Toast.makeText(this@AbrirReceitas, "Receita não deletada", Toast.LENGTH_LONG).show() }
        caixa.show()
    }
}