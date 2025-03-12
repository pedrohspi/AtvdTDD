package br.edu.ufcg.ccc.vv.models;

import java.util.List;

/**
 * Um modelo que representa um lote de ingressos.
 * Contém informações sobre o ID do lote, a lista de ingressos e o desconto aplicado.
 */
public class LoteModel {
    private Long id;
    private List<IngressoModel> ingressos;
    private Double desconto;

    /**
     * Constrói um LoteModel com desconto, lista de ingressos e id especificados.
     *
     * @param desconto o desconto aplicado ao lote
     * @param ingressos a lista de ingressos no lote
     * @param id o ID do lote
     */
    public LoteModel(Double desconto, List<IngressoModel> ingressos, Long id) {
        this.desconto = desconto;
        this.ingressos = ingressos;
        this.id = id;
    }

    /**
     * Construtor padrão.
     */
    public LoteModel() {
    }

    /**
     * Retorna o ID do lote.
     *
     * @return o ID do lote
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do lote.
     *
     * @param id o ID do lote
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna a lista de ingressos no lote.
     *
     * @return a lista de ingressos no lote
     */
    public List<IngressoModel> getIngressos() {
        return ingressos;
    }

    /**
     * Define a lista de ingressos no lote.
     *
     * @param ingressos a lista de ingressos no lote
     */
    public void setIngressos(List<IngressoModel> ingressos) {
        this.ingressos = ingressos;
    }

    /**
     * Retorna o desconto aplicado ao lote.
     *
     * @return o desconto aplicado ao lote
     */
    public Double getDesconto() {
        return desconto;
    }

    /**
     * Define o desconto aplicado ao lote.
     *
     * @param desconto o desconto aplicado ao lote
     */
    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }
}