package main;

import java.util.Date;

public class Pagamento {
    private Double valorPago;
    private Date dataPagamento;
    private String tipoPagamento;

    public Pagamento(Double valorPago, Date dataPagamento, String tipoPagamento) {
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
        this.tipoPagamento = tipoPagamento;
    }

    public Double getValorPago() {
        return this.valorPago;
    }

    public Date getDataPagamento() {
        return this.dataPagamento;
    }

    public String getTipoPagamento() {
        return this.tipoPagamento;
    }
}
