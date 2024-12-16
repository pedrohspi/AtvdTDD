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
import main.StatusPagamento;
import main.TipoPagamento;

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
	void ProcessandoContasDeUmaFaturaPendente() {

		this.fatura = new Fatura(new Date(2014, 12, 24), 300.00, "José");

		this.processadorDeContas.processarContas(fatura, contas);

		assertEquals(StatusPagamento.PENDENTE, this.fatura.getStatusPagamento());
	}

	@Test
	void ProcessandoContaAntesDe15Dias() {
		this.contas = new ArrayList<>();
		this.contas.add(new Conta("001", new Date(06, 12, 2024), 100.00, TipoPagamento.CARTAO_CREDITO));

		this.processadorDeContas.processarContas(fatura, contas);

		assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento());
	}

	@Test
	void PagandoContaDepoisDoPrazoComoBoleto() {

		this.contas = new ArrayList<>();
		Conta conta = new Conta("001", new Date(2024, 12, 10), 190.00, TipoPagamento.BOLETO);
		this.contas.add(conta);

		this.processadorDeContas.processarContas(fatura, contas);

		assertAll(
				() -> assertEquals(StatusPagamento.PAGA, fatura.getStatusPagamento()),
				() -> assertEquals(100.00, conta.getValorPago()));
	}

	@Test
	void PagandoUmaContaComFaturaInvalida() {

		this.contas = new ArrayList<>();
		Conta conta = new Conta("001", new Date(06, 12, 2024), 200.00, TipoPagamento.BOLETO);
		this.contas.add(conta);
		this.fatura = new Fatura(new Date(), -200.00, "Sergio");

		this.processadorDeContas.processarContas(fatura, contas);

		assertAll(
				() -> assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento()));
	}

	@Test
	void PagandoUmBoletoJaVencido() {

		this.contas = new ArrayList<>();
		Conta conta = new Conta("001", new Date(this.fatura.getDataVencimento().getTime() - (1000 * 60 * 60 * 24 * 20)),
				100.00, TipoPagamento.BOLETO);
		this.contas.add(conta);
		this.fatura = new Fatura(new Date(), -100.00, "Usuário 1");

		this.processadorDeContas.processarContas(fatura, contas);

		assertAll(
				() -> assertEquals(StatusPagamento.PENDENTE, fatura.getStatusPagamento()));
	}

	@Test
	void PagamentoValidoComBoletoEAtraso() {

		Date dataPagamento = new Date(this.fatura.getDataVencimento().getTime() - (1000 * 60 * 60 * 24 * 20));

		Conta contaBoleto = new Conta("002", dataPagamento, 100.00, TipoPagamento.BOLETO);
		this.contas.add(contaBoleto);

		this.processadorDeContas.processarContas(fatura, contas);

		assertEquals(StatusPagamento.PAGA, this.fatura.getStatusPagamento());
	}

	@Test
	void quandoRealizamosUmPagamentoDeUmaContaForaDoPrazpPagaComTransferenciaBancaria() {

		Conta contaBoleto = new Conta("002",
				new Date(this.fatura.getDataVencimento().getTime() - (1000 * 60 * 60 * 24 * 20)), 100.00,
				TipoPagamento.BOLETO);
		this.contas.add(contaBoleto);

		this.processadorDeContas.processarContas(fatura, contas);

		assertEquals(StatusPagamento.PAGA, this.fatura.getStatusPagamento());
	}
}
