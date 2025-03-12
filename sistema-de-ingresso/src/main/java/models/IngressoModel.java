package models;

/**
 * Um modelo que representa um ingresso.
 * Contém informações sobre o ID do ingresso, tipo, status de venda e preço.
 */
public class IngressoModel {
    private Long id;
    private TipoIngressoEnum tipoIngresso;
    private Boolean isVendido;
    private Double valor;

    /**
     * Construtor padrão.
     */
    public IngressoModel() {
    }

    /**
     * Constrói um IngressoModel com id, tipo, status de venda e preço especificados.
     *
     * @param id o ID do ingresso
     * @param tipoIngresso o tipo do ingresso
     * @param isVendido o status de venda do ingresso
     * @param valor o preço do ingresso
     */
    public IngressoModel(Long id, TipoIngressoEnum tipoIngresso, Boolean isVendido, Double valor) {
        this.id = id;
        this.tipoIngresso = tipoIngresso;
        this.isVendido = isVendido;
        this.valor = valor;
    }

    /**
     * Retorna o ID do ingresso.
     *
     * @return o ID do ingresso
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do ingresso.
     *
     * @param id o ID do ingresso
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o tipo do ingresso.
     *
     * @return o tipo do ingresso
     */
    public TipoIngressoEnum getTipoIngresso() {
        return tipoIngresso;
    }

    /**
     * Define o tipo do ingresso.
     *
     * @param tipoIngresso o tipo do ingresso
     */
    public void setTipoIngresso(TipoIngressoEnum tipoIngresso) {
        this.tipoIngresso = tipoIngresso;
    }

    /**
     * Retorna o status de venda do ingresso.
     *
     * @return true se o ingresso está vendido, false caso contrário
     */
    public Boolean isVendido() {
        return isVendido;
    }

    /**
     * Define o status de venda do ingresso.
     *
     * @param vendido o status de venda do ingresso
     */
    public void setVendido(Boolean vendido) {
        isVendido = vendido;
    }

    /**
     * Retorna o preço do ingresso.
     *
     * @return o preço do ingresso
     */
    public Double getValor() {
        return valor;
    }

    /**
     * Define o preço do ingresso.
     *
     * @param valor o preço do ingresso
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * Retorna uma representação em string do ingresso.
     *
     * @return uma representação em string do ingresso
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IngressoModel{");
        sb.append("valor=").append(valor);
        sb.append(", isVendido=").append(isVendido);
        sb.append(", tipoIngresso=").append(tipoIngresso);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
