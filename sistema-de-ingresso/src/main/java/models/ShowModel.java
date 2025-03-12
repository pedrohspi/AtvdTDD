package models;

import java.util.Date;
import java.util.List;

/**
 * Um modelo que representa um show.
 * Contém informações sobre a data do show, o artista, o cachê, as despesas de infraestrutura, os lotes de ingressos e se o show é especial.
 */
public class ShowModel {
    private Date data;
    private String artista;
    private Double cache;
    private Double despesasInfra;
    private List<LoteModel> lotes;
    private Boolean isEspecial;

    /**
     * Constrói um ShowModel com data, artista, cachê, despesas de infraestrutura, lotes de ingressos e se é especial especificados.
     *
     * @param data a data do show
     * @param artista o artista do show
     * @param cache o cachê do artista
     * @param despesasInfra as despesas de infraestrutura do show
     * @param lotes os lotes de ingressos do show
     * @param isEspecial se o show é especial
     */
    public ShowModel(Date data, String artista, Double cache, Double despesasInfra, List<LoteModel> lotes, Boolean isEspecial) {
        this.data = data;
        this.artista = artista;
        this.cache = cache;
        this.despesasInfra = despesasInfra;
        this.lotes = lotes;
        this.isEspecial = isEspecial;
    }

    /**
     * Construtor padrão.
     */
    public ShowModel() {
    }

    /**
     * Retorna a data do show.
     *
     * @return a data do show
     */
    public Date getData() {
        return data;
    }

    /**
     * Define a data do show.
     *
     * @param data a data do show
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Retorna o artista do show.
     *
     * @return o artista do show
     */
    public String getArtista() {
        return artista;
    }

    /**
     * Define o artista do show.
     *
     * @param artista o artista do show
     */
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /**
     * Retorna o cachê do artista.
     *
     * @return o cachê do artista
     */
    public Double getCache() {
        return cache;
    }

    /**
     * Define o cachê do artista.
     *
     * @param cache o cachê do artista
     */
    public void setCache(Double cache) {
        this.cache = cache;
    }

    /**
     * Retorna as despesas de infraestrutura do show.
     *
     * @return as despesas de infraestrutura do show
     */
    public Double getDespesasInfra() {
        return despesasInfra;
    }

    /**
     * Define as despesas de infraestrutura do show.
     *
     * @param despesasInfra as despesas de infraestrutura do show
     */
    public void setDespesasInfra(Double despesasInfra) {
        this.despesasInfra = despesasInfra;
    }

    /**
     * Retorna os lotes de ingressos do show.
     *
     * @return os lotes de ingressos do show
     */
    public List<LoteModel> getLotes() {
        return lotes;
    }

    /**
     * Define os lotes de ingressos do show.
     *
     * @param lotes os lotes de ingressos do show
     */
    public void setLotes(List<LoteModel> lotes) {
        this.lotes = lotes;
    }

    /**
     * Retorna se o show é especial.
     *
     * @return true se o show é especial, false caso contrário
     */
    public Boolean isEspecial() {
        return isEspecial;
    }

    /**
     * Define se o show é especial.
     *
     * @param especial se o show é especial
     */
    public void setEspecial(Boolean especial) {
        isEspecial = especial;
    }

    /**
     * Retorna uma representação em string do show.
     *
     * @return uma representação em string do show
     */
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
