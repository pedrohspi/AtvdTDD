package functionaltests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import main.Conta;
import main.Fatura;
import main.ProcessadorDeContas;
import main.StatusPagamento;
import main.TipoPagamento;
import java.time.LocalDate;
import java.time.ZoneId;



@DisplayName("Testes Funcionais para Processador de Contas")
@TestMethodOrder(MethodOrderer.DisplayName.class) // Ordena os testes pelo nome de exibição
class FunctionalTests {

    private ProcessadorDeContas processadorDeContas;
    private List<Conta> contas;
    private Fatura fatura;

    @BeforeEach
    void setUp() {
        this.processadorDeContas = new ProcessadorDeContas();
        this.contas = new ArrayList<>();
        this.fatura = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 200.00, "Pedro");
    }

    @AfterEach
    void tearDown() {
        // Limpeza de recursos, se necessário
    }

    // Método utilitário para converter LocalDate para Date
    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Suíte de testes para Análise de Valores Limites robusta
    @Nested
    @DisplayName("Análise de Valores Limites Robusta")
    @Tag("AnaliseValoresLimites")
    class AnaliseValoresLimitesRobusta {

        @Test
        @DisplayName("Caso de Teste 1 - Valor Boleto Zero")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste1_ValorBoletoZero() {
            contas.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 0.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
        }

        @Test
        @DisplayName("Caso de Teste 2 - Valor Boleto Mínimo Permitido")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste2_ValorBoletoMinimoPermitido() {
            contas.add(new Conta("002", convertToDate(LocalDate.of(2024, 12, 24)), 0.01, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
        }

        @Test
        @DisplayName("Caso de Teste 3 - Valor Boleto Acima do Mínimo")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste3_ValorBoletoAcimaMinimo() {
            contas.add(new Conta("003", convertToDate(LocalDate.of(2024, 12, 24)), 0.02, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
        }

        @Test
        @DisplayName("Caso de Teste 4 - Valor Boleto 2002.00")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste4_ValorBoleto2002() {
            contas.add(new Conta("004", convertToDate(LocalDate.of(2024, 12, 24)), 2002.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
        }

        @Test
        @DisplayName("Caso de Teste 5 - Valor Boleto 4999.99")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste5_ValorBoleto4999_99() {
            contas.add(new Conta("005", convertToDate(LocalDate.of(2024, 12, 24)), 4999.99, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
        }

        @Test
        @DisplayName("Caso de Teste 6 - Valor Boleto Máximo Permitido")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste6_ValorBoletoMaximoPermitido() {
            contas.add(new Conta("006", convertToDate(LocalDate.of(2024, 12, 24)), 5000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
        }

        @Test
        @DisplayName("Caso de Teste 7 - Valor Boleto Acima do Limite")
        void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste7_ValorBoletoAcimaDoLimite() {
            contas.add(new Conta("007", convertToDate(LocalDate.of(2024, 12, 24)), 5000.01, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(fatura, contas);
            assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
        }
    }

    // Suíte de testes para Partições de Equivalência
    @Nested
    @DisplayName("Partições de Equivalência")
    @Tag("ParticoesEquivalencia")
    class ParticoesEquivalencia {

        @Test
        @DisplayName("CT == VF")
        void TecnicaParticoesDeEquivalencia_CT_E_VF_Iguais() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 2000.00, "Pedro");
            c.add(new Conta("002", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("CT > VF")
        void TecnicaParticoesDeEquivalencia_CT_Maior_Que_VF() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1800.00, "Pedro");
            c.add(new Conta("002", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("CT < VF")
        void TecnicaParticoesDeEquivalencia_CT_Menor_Que_VF() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 300.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }
        
        @Test
        @DisplayName("VF == 0")
        @Timeout(2) // 2 segundos de limite
        void TecnicaParticoesDeEquivalencia_VF_Igual_Zero() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 0.00, "Pedro");
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("CT == VF == 0")
        @Timeout(2) // 2 segundos de limite
        void TecnicaParticoesDeEquivalencia_CT_E_VF_Iguais_Zero() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 0.00, "Pedro");
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 0.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("CT negativo")
        @Timeout(2) // 2 segundos de limite
        void TecnicaParticoesDeEquivalencia_CT_Negativo() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), -100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("VF negativo")
        @Timeout(2) // 2 segundos de limite
        void TecnicaParticoesDeEquivalencia_VF_Negativo() {
            List<Conta> c = new ArrayList<>();
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), -100.00, "Pedro");
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }
    }
    
    // Suíte de testes para tabela de decisão

    @Nested
    @DisplayName("Tabela de Decisão")
    @Tag("TabelaDecisao")
    class TabelaDecisao {

        @Test
        @DisplayName("R1 - Transferência Bancária com valor suficiente")
        void R1() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 12, 24)), 1000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R2 - Transferência Bancária com valor insuficiente")
        void R2() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 200.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R3 - Transferência Bancária com valor suficiente, mas data inválida")
        void R3() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R4 - Transferência Bancária com valor insuficiente e data inválida")
        void R4() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 3000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R5 - Cartão de Crédito com valor suficiente")
        void R5() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 8)), 2000.00, TipoPagamento.CARTAO_CREDITO));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 12, 24)), 1000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R6 - Cartão de Crédito com valor insuficiente")
        void R6() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 11)), 200.00, TipoPagamento.CARTAO_CREDITO));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R7 - Cartão de Crédito com valor suficiente, mas data inválida")
        void R7() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.CARTAO_CREDITO));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 10)), 1000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R8 - Cartão de Crédito com valor insuficiente e data inválida")
        void R8() {
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.CARTAO_CREDITO));
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 9)), 3000.00, "Pedro");
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R9 - Boleto com valor suficiente")
        void R9() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 30)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 9)), 1100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R10 - Boleto com valor suficiente e estorno")
        void R10() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 1100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), -1100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R11 - Boleto com valor exato")
        void R11() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 30)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 30)), 1000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R12 - Boleto com valor insuficiente")
        void R12() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), 900.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R13 - Boleto com valor insuficiente e estorno")
        void R13() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), 900.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }
        
        @Test
        @DisplayName("R14 - Boleto com estorno maior que o valor pago")
        void R14() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), -900.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R15 - Boleto com valor insuficiente e data anterior")
        void R15() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 27)), 800.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R16 - Boleto com valor parcial e estorno")
        void R16() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 900.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 27)), -90.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R17 - Boleto com valor parcial e complemento")
        void R17() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 900.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 30)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R18 - Boleto com valor exato e estorno")
        void R18() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 30)), -100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R19 - Boleto com valor exato e complemento")
        void R19() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R20 - Boleto com valor exato e estorno parcial")
        void R20() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), -100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R21 - Boleto com valor parcial e complemento mínimo")
        void R21() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), 1.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R22 - Boleto com valor parcial e estorno mínimo")
        void R22() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 29)), -1.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R23 - Boleto com valor parcial e complemento mínimo após data")
        void R23() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 30)), 1.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }

        @Test
        @DisplayName("R24 - Boleto com valor parcial e estorno mínimo após data")
        void R24() {
            Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 400.00, "Pedro");
            List<Conta> c = new ArrayList<>();
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 28)), 100.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            c.add(new Conta("001", convertToDate(LocalDate.of(2024, 7, 30)), -1.00, TipoPagamento.BOLETO));
            processadorDeContas.processarContas(f, c);
            assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
        }
    }
}

