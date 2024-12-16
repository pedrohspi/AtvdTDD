package main;

import java.util.Date;

public class Conta {
    private String codigoConta;
    private Date data;
    private Double valorPago;
    private String tipoPagamento;

    public Conta(String codigoConta, Date data, double valorPago, String tipoPagamento) {
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

    public String getTipoPagamento() {
        return this.tipoPagamento;
    }
}
