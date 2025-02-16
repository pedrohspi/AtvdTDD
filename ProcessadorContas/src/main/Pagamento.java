package main;

import java.util.Date;

public class Pagamento {
    private Double valorPago;
    private Date dataPagamento;
    private TipoPagamento tipoPagamento;

    public Pagamento(Double valorPago, Date dataPagamento, TipoPagamento tipoPagamento) {
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

    public TipoPagamento getTipoPagamento() {
        return this.tipoPagamento;
    }
}
