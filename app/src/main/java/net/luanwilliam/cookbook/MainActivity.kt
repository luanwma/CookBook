package net.luanwilliam.cookbook

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import net.luanwilliam.cookbook.DAO.ReceitaDatabase
import net.luanwilliam.cookbook.model.Categoria
import net.luanwilliam.cookbook.model.Receita

class MainActivity : AppCompatActivity() {
    private var listaReceitasLV: ListView? = null
    private var listaReceitas: List<Receita>? = null
    private var adapterReceitas: ArrayAdapter<Receita>? = null
    private var listaCat: List<Categoria>? = null
    private var listaImpressao: MutableList<String> = ArrayList()
    private var btnNovaReceita: ImageButton? = null
    private val receita: Receita? = null
    private val NOVO = 0
    private val MODO = "MODO"
    private val ALTERAR = 1
    private val ID_ALTERAR = "ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        carregaReceitas()
        btnNovaReceita = findViewById(R.id.btnNovaReceita)
        listaReceitasLV = findViewById(R.id.listaReceitas)
        registerForContextMenu(listaReceitasLV)
        listaReceitasLV!!.onItemClickListener = OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id: Long ->
            val rec = parent.getItemAtPosition(position) as Receita

            val param = Bundle()
            param.putLong("id", rec.idrec)
            param.putString("nome", rec.nome)
            param.putString("descricao", rec.descricao)
            param.putString("ingredientes", rec.listaIngredientes)
            param.putString("modo_preparo", rec.modoPreparo)
            param.putString("data", rec.data)

            val intent = Intent(this@MainActivity, AbrirReceitas::class.java)
            intent.putExtras(param)
            startActivity(intent)
        }

        btnNovaReceita!!.setOnClickListener {
            val intencao = Intent(this@MainActivity, CadastroReceita::class.java)
            intencao.putExtra(MODO, NOVO)
            startActivity(intencao)
            finishActivity(RESULT_OK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_2_REQUEST) {
            if (resultCode == RESULT_OK) {
                carregaReceitas()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_categorias, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listar_categorias -> {
                val intent1 = Intent(this@MainActivity, ListarCategorias::class.java)
                startActivity(intent1)
                finishActivity(1)
                true
            }

            R.id.new_categoria -> {
                val bundle2 = Bundle()
                bundle2.putInt(MODO, NOVO)
                val intent = Intent(this@MainActivity, CadastroCategoria::class.java)
                intent.putExtras(bundle2)
                startActivity(intent)
                finishActivity(1)
                true
            }
            R.id.itemAutoria -> {
                val intent3 = Intent(this@MainActivity, Autoria::class.java)
                startActivity(intent3)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context_flutuante, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info: AdapterContextMenuInfo? = item.menuInfo as AdapterContextMenuInfo?
        val pos = info!!.position
        val r = adapterReceitas!!.getItem(pos)

        when (item.itemId) {
            R.id.menuFly_alterar -> {
                alterarReceita(r)
                return true
            }

            R.id.menuFly_excluir -> return true
        }

        return super.onContextItemSelected(item)
    }

    private fun carregaReceitas() {
        AsyncTask.execute {
            val db = ReceitaDatabase.getDatabase(this@MainActivity)
            listaReceitas = db!!.receitaDAO()!!.queryAll() as List<Receita>?
            listaCat = db.categoriaDAO().queryAll() as List<Categoria>?
            listaImpressao = ArrayList()

            for (r in listaReceitas!!) {
                listaImpressao.add(r.toString())
            }

            runOnUiThread {
                adapterReceitas = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, listaReceitas!!)
                listaReceitasLV!!.adapter = adapterReceitas
            }
        }
    }

    private fun alterarReceita(r: Receita?) {
        val msg = "Deseja mesmo alterar a receita? "
        val caixa = AlertDialog.Builder(this@MainActivity)
        caixa.setTitle("Alterar")
        caixa.setIcon(android.R.drawable.ic_menu_edit)
        caixa.setMessage(msg)
        caixa.setPositiveButton("Sim") { dialog, which ->
            val param = Bundle()
            param.putInt(MODO, ALTERAR)
            param.putLong("id", r!!.idrec)
            println("inserindo id para alterar " + r.idrec)
            param.putString("nome", r.nome)
            param.putString("descricao", r.descricao)
            param.putString("ingredientes", r.listaIngredientes)
            param.putString("modo_preparo", r.modoPreparo)
            param.putString("data", r.data)
            val intent = Intent(this@MainActivity, CadastroReceita::class.java)
            intent.putExtras(param)
            startActivity(intent)
        }
        caixa.setNegativeButton("Não") { dialog, which -> Toast.makeText(this@MainActivity, "Receita não deletada", Toast.LENGTH_LONG).show() }
        val dialog = caixa.create()
        caixa.show()
    }

    companion object {
        const val ACTIVITY_2_REQUEST = 1
    }
}