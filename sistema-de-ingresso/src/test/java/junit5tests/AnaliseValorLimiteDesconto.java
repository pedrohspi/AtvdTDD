package junit5tests;

import models.LoteModel;
import models.ShowModel;
import models.TipoIngressoEnum;
import repository.ShowRepository;
import services.ShowServices;
import utilstests.InMemoryShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Compartilha a instância da classe entre testes
@Tag("AnaliseValorLimiteDesconto") // Categoria para execução seletiva
public class AnaliseValorLimiteDesconto {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
    }

    @Nested
    @DisplayName("Testes do Valor de Desconto")
    @Tag("DescontoTests") // Categoria para execução seletiva
    class TestesDoValorDeDesconto {

        @Test
        @DisplayName("Criar show com desconto abaixo do limite inferior - Deve lançar exceção")
        public void testCriarShowComDescontoAbaixoLimiteInferior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = -1.00;
            double vip = 20;

            Exception exception = assertThrows(Exception.class, () -> {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
            });

            assertEquals("Desconto inválidos", exception.getMessage());
        }

        @Test
        @DisplayName("Criar show com desconto no limite inferior - Deve funcionar")
        public void testCriarShowComDescontoLimiteInferior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 20;

            // Execute o método criarShow
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Verificar se o show foi salvo corretamente
            ShowModel show = showRepository.findById(data, artista).get();
            assertNotNull(show);

            List<LoteModel> lotes = show.getLotes();
            assertEquals(quantLotes, lotes.size());
            LoteModel lote = lotes.getFirst();
            assertEquals(quantIngressosPorLote, lote.getIngressos().size());

            // Verificar o desconto
            assertEquals(descontoLote, lote.getDesconto());
        }

        @Test
        @DisplayName("Criar show com desconto no limite inferior + 1 - Deve funcionar")
        public void testCriarShowComDescontoLimiteInferiorMais1() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.01;
            double vip = 20;

            // Execute o método criarShow
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Verificar se o show foi salvo corretamente
            ShowModel show = showRepository.findById(data, artista).get();
            assertNotNull(show);

            List<LoteModel> lotes = show.getLotes();
            assertEquals(quantLotes, lotes.size());
            LoteModel lote = lotes.getFirst();
            assertEquals(quantIngressosPorLote, lote.getIngressos().size());

            // Verificar o desconto
            assertEquals(descontoLote, lote.getDesconto());
        }

        @Test
        @DisplayName("Criar show com desconto no caso médio - Deve funcionar")
        public void testCriarShowComDescontoCasoMedio() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 10.00;
            double vip = 20;

            // Execute o método criarShow
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Verificar se o show foi salvo corretamente
            ShowModel show = showRepository.findById(data, artista).get();
            assertNotNull(show);

            List<LoteModel> lotes = show.getLotes();
            assertEquals(quantLotes, lotes.size());
            LoteModel lote = lotes.getFirst();
            assertEquals(quantIngressosPorLote, lote.getIngressos().size());

            // Verificar o desconto
            assertEquals(descontoLote, lote.getDesconto());
        }

        @Test
        @DisplayName("Criar show com desconto no limite superior - 1 - Deve funcionar")
        public void testCriarShowComDescontoLimiteSuperiorMenos1() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 24.99;
            double vip = 20;

            // Execute o método criarShow
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Verificar se o show foi salvo corretamente
            ShowModel show = showRepository.findById(data, artista).get();
            assertNotNull(show);

            List<LoteModel> lotes = show.getLotes();
            assertEquals(quantLotes, lotes.size());
            LoteModel lote = lotes.getFirst();
            assertEquals(quantIngressosPorLote, lote.getIngressos().size());

            // Verificar o desconto
            assertEquals(descontoLote, lote.getDesconto());

            // Verificar a distribuição dos ingressos
            int totalIngressos = lote.getIngressos().size();
            int expectedVip = (int) (totalIngressos * vip / 100);
            int expectedMeiaEntrada = (int) (totalIngressos * 0.10);
            int expectedNormal = totalIngressos - expectedVip - expectedMeiaEntrada;

            long countVip = lote.getIngressos().stream().filter(i -> i.getTipoIngresso() == TipoIngressoEnum.VIP).count();
            long countMeiaEntrada = lote.getIngressos().stream().filter(i -> i.getTipoIngresso() == TipoIngressoEnum.MEIA_ENTRADA).count();
            long countNormal = lote.getIngressos().stream().filter(i -> i.getTipoIngresso() == TipoIngressoEnum.NORMAL).count();

            assertEquals(expectedVip, countVip);
            assertEquals(expectedMeiaEntrada, countMeiaEntrada);
            assertEquals(expectedNormal, countNormal);
        }

        @Test
        @DisplayName("Criar show com desconto no limite superior - Deve funcionar")
        public void testCriarShowComDescontoLimiteSuperior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 25.00;
            double vip = 20;

            // Execute o método criarShow
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Verificar se o show foi salvo corretamente
            ShowModel show = showRepository.findById(data, artista).get();
            assertNotNull(show);

            List<LoteModel> lotes = show.getLotes();
            assertEquals(quantLotes, lotes.size());
            LoteModel lote = lotes.getFirst();
            assertEquals(quantIngressosPorLote, lote.getIngressos().size());

            // Verificar o desconto
            assertEquals(descontoLote, lote.getDesconto());
        }

        @Test
        @DisplayName("Criar show com desconto acima do limite superior - Deve lançar exceção")
        public void testCriarShowComDescontoAcimaDolimiteSuperior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 25.01;
            double vip = 20;

            Exception exception = assertThrows(Exception.class, () -> {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
            });

            assertEquals("Desconto inválidos", exception.getMessage());
        }

        @Test
        @DisplayName("Criar show com desconto negativo - Deve lançar exceção")
        public void testCriarShowComDescontoNegativo() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = -1.00;
            double vip = 20;

            Exception exception = assertThrows(Exception.class, () -> {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
            });

            assertEquals("Desconto inválidos", exception.getMessage());
        }
    }
}