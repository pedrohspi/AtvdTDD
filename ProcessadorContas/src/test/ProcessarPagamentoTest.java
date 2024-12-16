package test;

import org.junit.jupiter.api.Test;
import main.ProcessadorDeContas;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ProcessarPagamentoTest {
    private ProcessadorDeContas processadorDeContas;
    private Date dataPagamento;

    @BeforeEach
    void setUp() {
        processadorDeContas = new ProcessadorDeContas();
        dataPagamento = new Date();
    }

    @Test
    void RealizarPagamentoBoletoPagoAposVencimento() {

        Date dataVencimento = new Date(dataPagamento.getTime() - (1000 * 60 * 60 * 24 * 1));
        Conta conta = new Conta("001", dataVencimento, 100.00, TipoPagamento.BOLETO);

        Pagamento pagamento = processadorContas.realizarPagamento(conta, dataPagamento);

        assertNotNull(pagamento, "Pagamento não deve ser nulo");

        Double valorEsperado = 110.00;
        Double valorPago = pagamento.getValorPago();

        double margemErro = 0.01;

        assertEquals(valorEsperado, valorPago, margemErro, "O valor pago deve ter 10% de acréscimo");
        assertEquals(TipoPagamento.BOLETO, pagamento.getTipoPagamento(), "O tipo de pagamento deve ser BOLETO");
    }

    @Test
    void RealizarPagamentoBoletoPagoAntesDoVencimento() {

        Date dataVencimento = new Date(dataPagamento.getTime() + (1000 * 60 * 60 * 24 * 1));
        Conta conta = new Conta("001", dataVencimento, 100.00, TipoPagamento.BOLETO);

        Pagamento pagamento = processadorDeContas.realizarPagamento(conta, dataPagamento);

        assertNotNull(pagamento, "Pagamento não deve ser nulo");
        double valorEsperado = 100.00;
        double margemErro = 0.01;
        assertEquals(valorEsperado, pagamento.getValorPago(), margemErro,
                "O valor pago deve ser o mesmo da conta, sem acréscimo");
        assertEquals(TipoPagamento.BOLETO, pagamento.getTipoPagamento(), "O tipo de pagamento deve ser BOLETO");
    }

    @Test
    void RealizarPagamentoCartaoDeCreditoComoTipoDePagamento() {

        Date dataVencimento = new Date();
        Conta conta = new Conta("001", dataVencimento, 100.00, TipoPagamento.CARTAO_CREDITO);

        Pagamento pagamento = processadorDeContas.realizarPagamento(conta, dataPagamento);

        assertNotNull(pagamento, "Pagamento não deve ser nulo");
        double valorEsperado = 100.00;
        double margemErro = 0.01;
        assertEquals(valorEsperado, pagamento.getValorPago(), margemErro,
                "O valor pago deve ser o mesmo da conta, sem acréscimo");
        assertEquals(TipoPagamento.CARTAO_CREDITO, pagamento.getTipoPagamento(),
                "O tipo de pagamento deve ser CARTAO_CREDITO");
    }

    @Test
    void RealizarPagamentoTransferenciaComoTipoDePagamento() {

        Date dataVencimento = new Date();
        Conta conta = new Conta("001", dataVencimento, 100.00, TipoPagamento.TRANSFERENCIA_BANCARIA);

        Pagamento pagamento = processadorDeContas.realizarPagamento(conta, dataPagamento);

        assertNotNull(pagamento, "Pagamento não deve ser nulo");
        double valorEsperado = 100.00;
        double margemErro = 0.01;
        assertEquals(valorEsperado, pagamento.getValorPago(), margemErro,
                "O valor pago deve ser o mesmo da conta, sem acréscimo");
        assertEquals(TipoPagamento.TRANSFERENCIA_BANCARIA, pagamento.getTipoPagamento(),
                "O tipo de pagamento deve ser CARTAO_CREDITO");
    }
}
