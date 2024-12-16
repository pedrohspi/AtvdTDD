package test;

import org.junit.jupiter.api.Test;
import main.ProcessadorDeContas;
import main.TipoPagamento;
import static org.junit.jupiter.api.Assertions.*;

public class ValidarPagamentoTest {

    @Test
    public void PagamentoValidoCartaoCreditoValido() {

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Date dataPagamento = new Date(System.currentTimeMillis() - 16L * 24 * 60 * 60 * 1000);
        Date vencimentoFatura = new Date();
        Pagamento pagamento = new Pagamento(100.0, dataPagamento, TipoPagamento.CARTAO_CREDITO);

        boolean resultado = processador.pagamentoValido(pagamento, vencimentoFatura);

        assertTrue(resultado, "Pagamento com cartão de crédito deve ser válido.");
    }

    @Test
    public void PagamentoValidoCartaoCreditoInvalido() {

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Date dataPagamento = new Date(System.currentTimeMillis() - 15L * 24 * 60 * 60 * 1000); // 15 dias antes
        Date vencimentoFatura = new Date();
        Pagamento pagamento = new Pagamento(100.0, dataPagamento, TipoPagamento.CARTAO_CREDITO);

        boolean resultado = processador.pagamentoValido(pagamento, vencimentoFatura);

        assertFalse(resultado, "Pagamento com cartão de crédito deve ser inválido.");
    }

    @Test
    public void PagamentoValidoBoletoValido() {

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Date dataPagamento = new Date();
        Date vencimentoFatura = new Date(System.currentTimeMillis() + 1L * 24 * 60 * 60 * 1000);
        Pagamento pagamento = new Pagamento(100.0, dataPagamento, TipoPagamento.BOLETO);

        boolean resultado = processador.pagamentoValido(pagamento, vencimentoFatura);

        assertTrue(resultado, "Pagamento com boleto deve ser válido.");
    }

    @Test
    public void PagamentoValidoBoletoInvalido() {

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Date dataPagamento = new Date(System.currentTimeMillis() + 1L * 24 * 60 * 60 * 1000);
        Date vencimentoFatura = new Date();
        Pagamento pagamento = new Pagamento(100.0, dataPagamento, TipoPagamento.BOLETO);

        boolean resultado = processador.pagamentoValido(pagamento, vencimentoFatura);

        assertFalse(resultado, "Pagamento com boleto deve ser inválido.");
    }
}