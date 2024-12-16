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

class ProcessadorDeContasTest {

	private ProcessadorDeContas processadorDeContas;
	private List<Conta> contas;
	private Fatura fatura;

	@BeforeEach
	void setUp() {
		this.processadorDeContas = new ProcessadorDeContas();
		this.contas = new ArrayList<>();
		this.contas.add(new Conta("001", new Date(2024, 12, 16), 150.00, TipoPagamento.CARTAO_CREDITO));

		this.fatura = new Fatura(new Date(2024, 07, 24), .00, "Pedro");
	}

	@Test
	void ProcessandoContasDeUmaFaturaPaga() {

		this.processadorDeContas.processarContas(fatura, contas);

		assertEquals(StatusPagamento.PAGA, this.fatura.getStatusPagamento());
	}

	
	@Test
	void ProcessandoContasDeUmaFataturaPendente() {

		this.fatura = new Fatura(new Date(16, 12, 2024), 300.00, "José");
		
		this.processadorDeContas.processarContas(fatura, contas);
		
		assertEquals(StatusPagamento.PENDENTE, this.fatura.getStatusPagamento());
	}
	}
}
