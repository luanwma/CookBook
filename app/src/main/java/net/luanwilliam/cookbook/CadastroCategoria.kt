package net.luanwilliam.cookbook

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.luanwilliam.cookbook.dao.ReceitaDatabase
import net.luanwilliam.cookbook.model.Categoria

class CadastroCategoria : AppCompatActivity() {
    var textNome: TextView? = null
    var textDesc: TextView? = null
    var btnCadastrar: Button? = null
    var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_categoria)
        val intent = intent
        val bundle = intent.extras
        val modo = bundle!!.getInt(MODO)
        textNome = findViewById(R.id.text_nome)
        textDesc = findViewById(R.id.text_descricao)
        btnCadastrar = findViewById(R.id.btnCadastrar)
        if (modo == ALTERAR) {
            subirDados()
            btnCadastrar!!.setOnClickListener { atualizarCategoria() }
        } else {
            btnCadastrar!!.setOnClickListener { cadastrarCat() }
        }
    }

    private fun cadastrarCat() {
        val sNome: String = textNome!!.text.toString()
        val sDesc: String = textDesc!!.text.toString()
        val cat = Categoria(sNome, sDesc)
        AsyncTask.execute {
            val db = ReceitaDatabase.getDatabase(this@CadastroCategoria)
            db!!.categoriaDAO().insert(cat)
        }
        Toast.makeText(this@CadastroCategoria, "Sucesso", Toast.LENGTH_LONG).show()
        val intent = Intent(this@CadastroCategoria, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun atualizarCategoria() {
        val nome = textNome!!.text.toString()
        val desc = textDesc!!.text.toString()
        val nova = Categoria(nome, desc)
        nova.idcat = id
        AsyncTask.execute {
            val db = ReceitaDatabase.getDatabase(this@CadastroCategoria)
            db!!.categoriaDAO().update(nova)
        }
        Toast.makeText(this@CadastroCategoria, "Alterado com Sucesso", Toast.LENGTH_LONG).show()
        val intent = Intent(this@CadastroCategoria, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun subirDados() {
        val intent = intent
        val param = intent.extras
        val id = param!!.getLong("id")
        this.id = id
        val nome = param.getString("nome")
        val desc = param.getString("descricao")
        textNome!!.text = nome
        textDesc!!.text = desc
    }

    companion object {
        const val ALTERAR = 1
        const val MODO = "MODO"
    }
}