package main;

import java.util.Date;
import java.util.List;

public class ProcessadorDeContas {

    public void processarContas(Fatura fatura, List<Conta> contas) {
        Double valorTotalPagar = 0.0;

        for (Conta conta : contas) {
            Pagamento pagamento = this.realizarPagamento(conta, fatura.getDataVencimento());

            if (pagamento != null) {
                if (this.pagamentoValido(pagamento, fatura.getDataVencimento())) {
                    valorTotalPagar += pagamento.getValorPago();
                }
            }
        }

        validarPagamento(valorTotalPagar, fatura);
    }

    public void validarPagamento(Double valorTotalPagar, Fatura fatura) {
        if (valorTotalPagar >= fatura.getValorFatura()) {
            fatura.setStatus(StatusPagamento.PAGA);
        } else {
            fatura.setStatus(StatusPagamento.PENDENTE);
        }
    }

    public boolean pagamentoValido(Pagamento pagamento, Date vencimentoFatura) {
        if (pagamento.getTipoPagamento().equals(TipoPagamento.CARTAO_CREDITO)) {
            Long diasEmSegundos = Math.abs(vencimentoFatura.getTime() - pagamento.getDataPagamento().getTime());
            Long dias = diasEmSegundos / (1000 * 60 * 60 * 24);
            return dias > 15;
        } else {
            return !pagamento.getDataPagamento().after(vencimentoFatura);
        }
    }

    public Pagamento realizarPagamento(Conta conta, Date dataPagamento) {
        Double valorPago = conta.getValorPago();

        if (conta.getTipoPagamento().equals(TipoPagamento.BOLETO)) {
            if (dataPagamento.after(conta.getData())) {
                valorPago *= 1.1;
            }
        }

        return new Pagamento(valorPago, new Date(), conta.getTipoPagamento());
    }

}