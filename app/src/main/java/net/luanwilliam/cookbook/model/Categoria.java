package net.luanwilliam.cookbook.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity( tableName = "categorias" , indices = @Index(value = {"nome"}, unique = true))
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    private long idcat;

    @NonNull
    private String nome;

    private String descCat;

    public Categoria(String nome, String descCat) {
        this.nome = nome;
        this.descCat = descCat;
    }



    public long getIdcat() {
        return idcat;
    }

    public void setIdcat(long idcat) {
        this.idcat = idcat;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescCat() {
        return descCat;
    }

    public void setDescCat(String descCat) {
        this.descCat = descCat;
    }

    public static Comparator<Categoria> comparador = new Comparator<Categoria>() {

        @Override
        public int compare(Categoria cat1, Categoria cat2) {

            int compAlfabetica = cat1.getNome().compareToIgnoreCase(cat2.getNome());

            if (compAlfabetica == 0){

                if (cat1.getIdcat() < cat2.getIdcat()){
                    return -1;
                }else
                if (cat1.getIdcat() > cat2.getIdcat()){
                    return 1;
                }else{
                    return 0;
                }
            }else{
                return compAlfabetica;
            }
        }
    };


    @Override
    public String toString() {
        return nome;
    }
}
