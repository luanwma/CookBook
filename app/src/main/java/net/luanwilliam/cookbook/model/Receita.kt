package net.luanwilliam.cookbook.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


import java.io.Serializable;
import java.util.Comparator;


@Entity(tableName = "receitas"  ,foreignKeys = @ForeignKey(entity = Categoria.class, parentColumns = "idcat" , childColumns = "id_categoria"))
public class Receita  {
    @PrimaryKey(autoGenerate = true)
    private long idrec;

    @NonNull
    private String nome;

    private String descricao, listaIngredientes, modoPreparo, data;

    private boolean feitoAnteriormente;
    private String avaliacao;

    @ColumnInfo(index = true)
    private long id_categoria;



    public Receita( String nome, String descricao, String listaIngredientes, String modoPreparo, boolean feitoAnteriormente,
                    String avaliacao, long id_categoria) {

        this.nome = nome;
        this.descricao = descricao;
        this.listaIngredientes = listaIngredientes;
        this.modoPreparo = modoPreparo;
        this.feitoAnteriormente = feitoAnteriormente;
        this.avaliacao = avaliacao;
        this.id_categoria = id_categoria;
    }


    public Receita(Receita r){
        this.idrec = r.idrec;
        this.nome = r.nome;
        this.descricao = r.descricao;
        this.listaIngredientes = r.listaIngredientes;
        this.modoPreparo = r.modoPreparo;
        this.feitoAnteriormente = r.feitoAnteriormente;
        this.avaliacao = r.avaliacao;
        this.id_categoria = r.id_categoria;

    }
  /*  public Receita( String nome, String descricao, String listaIngredientes, String modoPreparo, boolean feitoAnteriormente,
                    String avaliacao  ) {

        this.nome = nome;
        this.descricao = descricao;
        this.listaIngredientes = listaIngredientes;
        this.modoPreparo = modoPreparo;
        this.feitoAnteriormente = feitoAnteriormente;
        this.avaliacao = avaliacao;

    } */

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getIdrec() {
        return idrec;
    }

    public void setIdrec(long idrec) {
        this.idrec = idrec;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(String listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public boolean isFeitoAnteriormente() {
        return feitoAnteriormente;
    }

    public void setFeitoAnteriormente(boolean feitoAnteriormente) {
        this.feitoAnteriormente = feitoAnteriormente;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String  avaliacao) {
        this.avaliacao = avaliacao;
    }



    public long getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(long id_categoria) {
        this.id_categoria = id_categoria;
    }

    public static Comparator<Receita> comparador = new Comparator<Receita>() {

        @Override
        public int compare(Receita rec1, Receita rec2) {

            int compAlfabetica = rec1.getNome().compareToIgnoreCase(rec2.getNome());

            if (compAlfabetica == 0){

                if (rec1.getIdrec() < rec2.getIdrec()){
                    return -1;
                }else
                if (rec1.getIdrec() > rec2.getIdrec()){
                    return 1;
                }else{
                    return 0;
                }
            }else{
                return compAlfabetica;
            }
        }
    };

  /*  @Override
    public String toString() {
        return "Receita{" +
                "id=" + idrec +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", listaIngredientes='" + listaIngredientes + '\'' +
                ", modoPreparo='" + modoPreparo + '\'' +
                ", feitoAnteriormente=" + feitoAnteriormente +
                ", avaliacao=" + avaliacao +
                ", id_categoria=" + id_categoria +
                '}';
    } */

    @Override
    public String toString(){
        return nome;
    }
}
