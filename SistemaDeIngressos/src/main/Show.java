
package main;

import java.util.Date;
import java.util.List;

public class Show {
    private Date data;
    private String artista;
    private Double cache;
    private Double despesasInfra;
    private List<Tier> lotes;
    private Boolean isEspecial;

    public Show(Date data, String artista, Double cache, Double despesasInfra, List<Tier> lotes, Boolean isEspecial) {
        this.data = data;
        this.artista = artista;
        this.cache = cache;
        this.despesasInfra = despesasInfra;
        this.lotes = lotes;
        this.isEspecial = isEspecial;
    }

    public Show() {
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getArtista() {
        return this.artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public Double getCache() {
        return this.cache;
    }

    public void setCache(Double cache) {
        this.cache = cache;
    }

    public Double getDespesasInfra() {
        return this.despesasInfra;
    }

    public void setDespesasInfra(Double despesasInfra) {
        this.despesasInfra = despesasInfra;
    }

    public List<Tier> getLotes() {
        return this.lotes;
    }

    public void setLotes(List<Tier> lotes) {
        this.lotes = lotes;
    }

    public Boolean isEspecial() {
        return this.isEspecial;
    }

    public void setEspecial(Boolean especial) {
        this.isEspecial = especial;
    }

    @Override
    public String toString() {
        return "data=" + data +
                ", artista='" + artista + '\'' +
                ", cache=" + cache +
                ", despesasInfra=" + despesasInfra +
                ", lotes=" + lotes +
                ", isEspecial=" + isEspecial;
    }
}
