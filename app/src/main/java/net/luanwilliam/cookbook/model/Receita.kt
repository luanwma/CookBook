package net.luanwilliam.cookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "receitas", foreignKeys = [ForeignKey(entity = Categoria::class, parentColumns = ["idcat"], childColumns = ["id_categoria"])])
class Receita {
    @PrimaryKey(autoGenerate = true)
    var idrec: Long = 0
    var nome: String
    var descricao: String
    var listaIngredientes: String
    var modoPreparo: String
    var data: String? = null
    var isFeitoAnteriormente: Boolean
    var avaliacao: String

    @ColumnInfo(index = true)
    var id_categoria: Long

    constructor(
        nome: String, descricao: String, listaIngredientes: String, modoPreparo: String, feitoAnteriormente: Boolean,
        avaliacao: String, id_categoria: Long
    ) {
        this.nome = nome
        this.descricao = descricao
        this.listaIngredientes = listaIngredientes
        this.modoPreparo = modoPreparo
        isFeitoAnteriormente = feitoAnteriormente
        this.avaliacao = avaliacao
        this.id_categoria = id_categoria
    }

    constructor(r: Receita) {
        idrec = r.idrec
        nome = r.nome
        descricao = r.descricao
        listaIngredientes = r.listaIngredientes
        modoPreparo = r.modoPreparo
        isFeitoAnteriormente = r.isFeitoAnteriormente
        avaliacao = r.avaliacao
        id_categoria = r.id_categoria
    }

    override fun toString(): String {
        return nome
    }
}