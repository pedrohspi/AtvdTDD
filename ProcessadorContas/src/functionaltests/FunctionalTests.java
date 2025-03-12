package functionaltests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.Conta;
import main.Fatura;
import main.ProcessadorDeContas;
import main.StatusPagamento;
import main.TipoPagamento;
import java.time.LocalDate;
import java.time.ZoneId;

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

    // Suíte de testes para Análise de Valores Limites robusta
    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste1_ValorBoletoZero() {
        this.contas.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 0.00, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
    }

    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste2_ValorBoletoMinimoPermitido() {
        this.contas.add(new Conta("002", convertToDate(LocalDate.of(2024, 12, 24)), 0.01, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
    }

    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste3_ValorBoletoAcimaMinimo() {
        this.contas.add(new Conta("003", convertToDate(LocalDate.of(2024, 12, 24)), 0.02, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
    }

    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste4_ValorBoleto2002() {
        this.contas.add(new Conta("004", convertToDate(LocalDate.of(2024, 12, 24)), 2002.00, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
    }

    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste5_ValorBoleto4999_99() {
        this.contas.add(new Conta("005", convertToDate(LocalDate.of(2024, 12, 24)), 4999.99, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
    }

    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste6_ValorBoletoMaximoPermitido() {
        this.contas.add(new Conta("006", convertToDate(LocalDate.of(2024, 12, 24)), 5000.00, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
    }

    @Test
    void TecnicaAnaliseDeValoresLimitesRobusta_CasoDeTeste7_ValorBoletoAcimaDoLimite() {
        this.contas.add(new Conta("007", convertToDate(LocalDate.of(2024, 12, 24)), 5000.01, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(fatura, contas);
        assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento());
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
 // Suíte de testes para Partições de Equivalência
    @Test
    void TecnicaParticoesDeEquivalencia_CT_E_VF_Iguais() {
        // CT == VF
    	List<Conta> c = new ArrayList<>();
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 2000.00, "Pedro");
        c.add(new Conta("002", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }

    @Test
    void TecnicaParticoesDeEquivalencia_CT_Maior_Que_VF() {
        // CT > VF
    	List<Conta> c = new ArrayList<>();
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1800.00, "Pedro");
        c.add(new Conta("002", convertToDate(LocalDate.of(2024, 12, 24)), 2000, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }

    @Test
    void TecnicaParticoesDeEquivalencia_CT_Menor_Que_VF() {
        // CT < VF
    	List<Conta> c = new ArrayList<>();
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
        c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 300.00, TipoPagamento.BOLETO));
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    
    //Suíte de testes para tabela de decisão
    // TRANSFERENCIA_BANCARIA 
    @Test
    void R1() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 12, 24)), 1000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R2() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 200.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R3() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R4() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.TRANSFERENCIA_BANCARIA));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 3000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    // CARTAO_CREDITO
    
    @Test
    void R5() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 8)), 2000.00, TipoPagamento.CARTAO_CREDITO));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 12, 24)), 1000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R6() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 11)), 200.00, TipoPagamento.CARTAO_CREDITO));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 24)), 1000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R7() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.CARTAO_CREDITO));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 10)), 1000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R8() {
    	List<Conta> c = new ArrayList<>();
    	c.add(new Conta("001", convertToDate(LocalDate.of(2024, 12, 24)), 2000.00, TipoPagamento.CARTAO_CREDITO));
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 9)), 3000.00, "Pedro");
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    //BOLETO
    @Test
    void R9() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 30)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
        Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 9)),
                1100.00, TipoPagamento.BOLETO);
        c.add(conta);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R10() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                1100.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
                -1100.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R11() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 30)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7, 30)),
                1000.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R12() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
                900.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R13() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
                900.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R14() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
                -900.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R15() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,27)),
                800.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R16() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                900.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,27)),
               -90.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R17() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                900.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,30)),
               100.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R18() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                1000.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,30)),
               -100.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R19() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                1000.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
               100.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R20() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                1000.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
               -100.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PAGA, f.getStatusPagamento());
    }
    
    @Test
    void R21() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                100.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
               1.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R22() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                100.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,29)),
               -1.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    @Test
    void R23() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 1000.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                100.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,30)),
               1.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    
    @Test
    void R24() {
    	Fatura f = new Fatura(convertToDate(LocalDate.of(2024, 7, 28)), 400.00, "Pedro");
    	List<Conta> c = new ArrayList<>();
    	
    	Conta conta = new Conta("001",convertToDate(LocalDate.of(2024, 7, 28)),
                100.00, TipoPagamento.BOLETO);
    	c.add(conta);
    	this.processadorDeContas.processarContas(f, c);
    	
        Conta co = new Conta("001",convertToDate(LocalDate.of(2024, 7,30)),
               -1.00, TipoPagamento.BOLETO);
        c.add(co);
        this.processadorDeContas.processarContas(f, c);
        assertEquals(StatusPagamento.PENDENTE, f.getStatusPagamento());
    }
    
    
    
    

}

