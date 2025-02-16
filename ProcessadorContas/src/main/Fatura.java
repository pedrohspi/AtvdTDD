package main;

import java.util.Date;

public class Fatura {
    private String nomeDevedor;
    private Date dataVencimento;
    private Double valor;
    private StatusPagamento statusPagamento;

    public Fatura(Date dataPagamento, double valor,String nomeDevedor) {
        this.nomeDevedor = nomeDevedor;
        this.dataVencimento = dataPagamento;
        this.valor = valor;
    }

    public String getNomeDevedor() {
        return this.nomeDevedor;
    }

    public Date getDataVencimento() {
        return this.dataVencimento;
    }
    
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(StatusPagamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
