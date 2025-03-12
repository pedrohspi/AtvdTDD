package br.edu.ufcg.ccc.vv.models;

/**
 * Um modelo que representa um relatório de vendas de ingressos.
 * Contém informações sobre o número de ingressos VIP, normais e meia-entrada,
 * o valor total arrecadado e o status do relatório.
 */
public class RelatorioModel {
    private Integer numIngressoVip;
    private Integer numIngressoNormal;
    private Integer numIngressoMeia;
    private Double valorTotal;
    private StatusEnum status;

    /**
     * Construtor padrão.
     */
    public RelatorioModel() {
    }

    /**
     * Retorna o número de ingressos VIP.
     *
     * @return o número de ingressos VIP
     */
    public Integer getNumIngressoVip() {
        return numIngressoVip;
    }

    /**
     * Define o número de ingressos VIP.
     *
     * @param numIngressoVip o número de ingressos VIP
     */
    public void setNumIngressoVip(Integer numIngressoVip) {
        this.numIngressoVip = numIngressoVip;
    }

    /**
     * Retorna o número de ingressos normais.
     *
     * @return o número de ingressos normais
     */
    public Integer getNumIngressoNormal() {
        return numIngressoNormal;
    }

    /**
     * Define o número de ingressos normais.
     *
     * @param numIngressoNormal o número de ingressos normais
     */
    public void setNumIngressoNormal(Integer numIngressoNormal) {
        this.numIngressoNormal = numIngressoNormal;
    }

    /**
     * Retorna o número de ingressos meia-entrada.
     *
     * @return o número de ingressos meia-entrada
     */
    public Integer getNumIngressoMeia() {
        return numIngressoMeia;
    }

    /**
     * Define o número de ingressos meia-entrada.
     *
     * @param numIngressoMeia o número de ingressos meia-entrada
     */
    public void setNumIngressoMeia(Integer numIngressoMeia) {
        this.numIngressoMeia = numIngressoMeia;
    }

    /**
     * Retorna o valor total arrecadado.
     *
     * @return o valor total arrecadado
     */
    public Double getValorTotal() {
        return valorTotal;
    }

    /**
     * Define o valor total arrecadado.
     *
     * @param valorTotal o valor total arrecadado
     */
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * Retorna o status do relatório.
     *
     * @return o status do relatório
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * Define o status do relatório.
     *
     * @param status o status do relatório
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}