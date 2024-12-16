package main;

import java.util.Date;

public class Fatura {
    private String numeroFatura;
    private Date dataPagamento;
    private Double valorPago;
    private String metodoPagamento;

    public Fatura(String numeroFatura, Date dataPagamento, double valorPago, String metodoPagamento) {
        this.numeroFatura = numeroFatura;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
        this.metodoPagamento = metodoPagamento;
    }

    public String getNumeroFatura() {
        return this.numeroFatura;
    }

    public Date getDataPagamento() {
        return this.dataPagamento;
    }

    public Double getValorPago() {
        return this.valorPago;
    }

    public String getMetodoPagamento() {
        return this.metodoPagamento;
    }
}
