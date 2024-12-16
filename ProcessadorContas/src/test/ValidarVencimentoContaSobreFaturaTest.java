package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.Conta;
import main.Fatura;
import main.ProcessadorDeContas;

class ProcessadorContasTest {

    private ProcessadorContas processadorContas;
    private List<Conta> contas;
    private Fatura fatura;

    @BeforeEach
    void setUp() {
        this.processadorDeContas = new ProcessadorContas();
        this.contas = new ArrayList<>();

        // Alterando os valores passados nas contas
        this.contas.add(new Conta("001", new Date(16, 12, 2024), 150.00, "CARTAO_CREDITO"));
        this.contas.add(new Conta("002", new Date(16, 12, 2024), 200.00, "BOLETO"));
        this.contas.add(new Conta("003", new Date(16, 12, 2024), 50.00, "TRANSFERENCIA_BANCARIA"));

        // Alterando o valor da fatura
        this.fatura = new Fatura(new Date(16, 12, 2024), 500.00, "Pedro");
    }

    @Test
    void quandoProcessamosContasDeUmaFaturaPendente() {
        this.processadorContas.processarContas(fatura, contas);

        assertEquals(this.fatura.getStatus(), "PENDENTE");
    }
}
