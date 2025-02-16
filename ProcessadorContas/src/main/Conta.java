package main;

import java.util.Date;

public class Conta {
    private String codigoConta;
    private Date data;
    private Double valorPago;
    private TipoPagamento tipoPagamento;

    public Conta(String codigoConta, Date data, double valorPago, TipoPagamento tipoPagamento) {
        this.codigoConta = codigoConta;
        this.data = data;
        this.valorPago = valorPago;
        this.tipoPagamento = tipoPagamento;
    }

    public String getCodigoConta() {
        return this.codigoConta;
    }

    public Date getData() {
        return this.data;
    }

    public Double getValorPago() {
        return this.valorPago;
    }

    public TipoPagamento getTipoPagamento() {
        return this.tipoPagamento;
    }
}
