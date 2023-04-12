package net.luanwilliam.cookbook

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.luanwilliam.cookbook.dao.ReceitaDatabase
import net.luanwilliam.cookbook.databinding.ActivityMainBinding
import net.luanwilliam.cookbook.model.Receita

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var listaImpressao: MutableList<String> = mutableListOf()

    private val NOVO = 0
    private val MODO = "MODO"
    //private val ALTERAR = 1

    companion object {
        const val ACTIVITY_2_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerForContextMenu(binding.listaReceitas)
        setupViews()
        carregaReceitas()
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
                val intent = Intent(this@MainActivity, ListarCategorias::class.java)
                startActivity(intent)
                finishActivity(1)
                true
            }

            R.id.new_categoria -> {
                val bundle = Bundle().apply {
                    putInt(MODO, NOVO)
                }
                val intent = Intent(this@MainActivity, CadastroCategoria::class.java).apply {
                    putExtras(bundle)
                }
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

    private fun setupViews() {
        binding.listaReceitas.setOnItemClickListener { parent, _, position, _ ->
            val intent = Intent(this@MainActivity, AbrirReceitas::class.java).apply {
                val rec = parent.getItemAtPosition(position) as Receita

                putExtras(Bundle().apply {
                    putLong("id", rec.idrec)
                    putString("nome", rec.nome)
                    putString("descricao", rec.descricao)
                    putString("ingredientes", rec.listaIngredientes)
                    putString("modo_preparo", rec.modoPreparo)
                    putString("data", rec.data)
                })
            }

            startActivity(intent)
        }

        binding.btnNovaReceita.setOnClickListener {
            val intent = Intent(this@MainActivity, CadastroReceita::class.java).apply {
                putExtra(MODO, NOVO)
            }

            startActivity(intent)
            finishActivity(RESULT_OK)
        }
    }

    private fun carregaReceitas() {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            val db = ReceitaDatabase.getDatabase(this@MainActivity)

            val listaReceitas = db!!.receitaDAO()!!.queryAll()
            listaReceitas?.forEach { listaImpressao.add(it.toString()) }

            withContext(Dispatchers.Main) { //RunOnUiThread
                binding.listaReceitas.adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    listaReceitas!!
                )
            }
        }
    }
}
/* private fun alterarReceita(r: Receita?) {
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
 }*/