package net.luanwilliam.cookbook

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import net.luanwilliam.cookbook.DAO.ReceitaDatabase
import net.luanwilliam.cookbook.model.Categoria

class ListarCategorias : AppCompatActivity() {
    var id: Long = 0
    private var listaCategorias: List<Categoria>? = null
    private var listaCategoriasLV: ListView? = null
    private var adapterCategorias: ArrayAdapter<Categoria>? = null
    private var categoria: Categoria? = null
    private val MODO = "MODO"
    private val ALTERAR = 1
    private var posicaoSelecionada = -1
    private var viewSelecionada: View? = null
    private var actionMode: ActionMode? = null
    private val mActionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflate = mode.menuInflater
            inflate.inflate(R.menu.menu_contexto_categoria_delete, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.menuItem_cat_del -> {
                    deleteCategoria(categoria)
                    mode.finish()
                    true
                }
                R.id.menuItem_cat_alt -> {
                    alterarCategoria(categoria)
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            if (viewSelecionada != null) {
                viewSelecionada!!.setBackgroundColor(Color.TRANSPARENT)
            }
            actionMode = null
            viewSelecionada = null
            listaCategoriasLV!!.isEnabled = true
            carregaCategorias()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_categorias)
        listaCategoriasLV = findViewById(R.id.lista_categoria)
        listaCategoriasLV!!.choiceMode = ListView.CHOICE_MODE_SINGLE
        carregaCategorias()
        registerForContextMenu(listaCategoriasLV)
        listaCategoriasLV!!.onItemClickListener = OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id: Long ->
            val cat = parent.getItemAtPosition(position) as Categoria
            posicaoSelecionada = position
            val param = Bundle()
            param.putLong("id", cat.idcat)
            param.putString("nome", cat.nome)
            param.putString("descricao", cat.descCat)
            val intent = Intent(this@ListarCategorias, AbrirCategorias::class.java)
            intent.putExtras(param)
            startActivity(intent)
        }
        listaCategoriasLV!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            categoria = parent.getItemAtPosition(position) as Categoria
            if (actionMode != null) {
                return@OnItemLongClickListener false
            }
            posicaoSelecionada = position
            view.setBackgroundColor(Color.RED)
            viewSelecionada = view
            listaCategoriasLV!!.isEnabled = false
            actionMode = startSupportActionMode(mActionModeCallback)
            true
        }
    }

    fun carregaCategorias() {
        AsyncTask.execute {
            val db = ReceitaDatabase.getDatabase(this@ListarCategorias)
            listaCategorias = db!!.categoriaDAO().queryAll() as List<Categoria>?
            runOnUiThread {
                adapterCategorias = ArrayAdapter(
                    this@ListarCategorias, android.R.layout.simple_list_item_1, listaCategorias!!
                )
                listaCategoriasLV!!.adapter = adapterCategorias
            }
        }
    }

    fun alterarCategoria(categoria: Categoria?) {
        val msg = " Deseja mesmo alterar a categoria? "
        val caixa = AlertDialog.Builder(this@ListarCategorias)
        caixa.setTitle("Alterar : " + categoria!!.nome)
        caixa.setIcon(android.R.drawable.ic_menu_edit)
        caixa.setMessage(msg)
        caixa.setPositiveButton("Sim") { dialog, which ->
            val bundle = Bundle()
            bundle.putInt(MODO, ALTERAR)
            bundle.putLong("id", categoria.idcat)
            bundle.putString("nome", categoria.nome)
            bundle.putString("descricao", categoria.descCat)
            val intent = Intent(this@ListarCategorias, CadastroCategoria::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            finishActivity(1)
        }
        caixa.setNegativeButton("Não") { dialog, which -> Toast.makeText(this@ListarCategorias, "Categoria não deletada", Toast.LENGTH_LONG).show() }
        val dialog = caixa.create()
        caixa.show()
    }

    fun deleteCategoria(categoria: Categoria?) {
        val msg = "Ao deletar uma categoria deletará todas as receitas inclusas nessa categoria. Deseja mesmo deletar a categoria? "
        val caixa = AlertDialog.Builder(this@ListarCategorias)
        caixa.setTitle("Excluir")
        caixa.setIcon(android.R.drawable.ic_menu_delete)
        caixa.setMessage(msg)
        caixa.setPositiveButton("Sim") { dialog, which ->
            val db = ReceitaDatabase.getDatabase(this@ListarCategorias)
            AsyncTask.execute { db!!.receitaDAO()!!.deleteByCat(categoria!!.idcat) }
            AsyncTask.execute {
                //  Categoria categoria = db.categoriaDAO().queryIdCategoria(id);
                db!!.categoriaDAO().delete(categoria)
            }
            Toast.makeText(this@ListarCategorias, "Categoria  deletada", Toast.LENGTH_LONG).show()
            val resultIntent = Intent(this@ListarCategorias, MainActivity::class.java)
            finishActivity(1)
            startActivity(resultIntent)
            // finish();
        }
        caixa.setNegativeButton("Não") { dialog, which -> Toast.makeText(this@ListarCategorias, "Categoria não deletada", Toast.LENGTH_LONG).show() }
        val dialog = caixa.create()
        caixa.show()
    }
}