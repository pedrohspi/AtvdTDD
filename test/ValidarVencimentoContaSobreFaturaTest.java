package test;

import org.junit.jupiter.api.Test;
import main.Fatura;
import main.ProcessadorDeContas;
import main.StatusPagamento;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;

public class ValidarVencimentoContaSobreFaturaTest {
    private ProcessadorDeContas processadorDeContas;

    @BeforeEach
    void setUp() {
        processadorDeContas = new ProcessadorDeContas();
    }

    @Test
    void testValidarPagamentoFaturaPaga() {

        Fatura fatura = new Fatura(new Date(), 100.00, "Usuario 1");
        Double valorTotalPagar = 150.00;

        processadorDeContas.validarPagamento(valorTotalPagar, fatura);

        assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento(),
                "A fatura deve ser marcada como PAGA quando o valor total pago é maior ou igual ao valor da fatura");
    }

    @Test
    void testValidarPagamentoFaturaPendente() {

        Fatura fatura = new Fatura(new Date(), 100.00, "Usuario 1");
        Double valorTotalPagar = 50.00;

        processadorDeContas.validarPagamento(valorTotalPagar, fatura);

        assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento(),
                "A fatura deve ser marcada como PENDENTE quando o valor total pago é menor que o valor da fatura");
    }
}