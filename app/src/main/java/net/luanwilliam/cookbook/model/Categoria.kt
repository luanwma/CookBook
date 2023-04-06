package net.luanwilliam.cookbook.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categorias", indices = [Index(value = ["nome"], unique = true)])
class Categoria(var nome: String, var descCat: String) {
    @PrimaryKey(autoGenerate = true)
    var idcat: Long = 0

    override fun toString(): String {
        return nome
    }
}