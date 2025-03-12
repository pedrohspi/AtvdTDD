package junit5tests;

import models.*;
import repository.ShowRepository;
import services.ShowServices;
import utilstests.InMemoryShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Disabled;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Análise de Valor Limite para Porcentagem de Ingressos")
public class AnaliseValorLimitePorcentagemDeIngressos {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
    }

    @Nested
    @DisplayName("Testes para Valor de VIP")
    class TestesDoValorDeVip {

        @Test
        @DisplayName("Criar Show com VIP abaixo do limite inferior (19%)")
        @Tag("VIP")
        @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
        public void testCriarShowComVIPAbaixoLimiteInferior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 19;
            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            } catch (Exception e) {
                assertEquals("Limites de VIP estão inválidos", e.getMessage());
            }
        }

        @Test
        @DisplayName("Criar Show com VIP no limite inferior (20%)")
        @Tag("VIP")
        public void testCriarShowComVipLimiteInferior() {
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
        @DisplayName("Criar Show com VIP no limite inferior + 1 (21%)")
        @Tag("VIP")
        public void testCriarShowComVipLimiteInferiorMais1() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 21;

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
        @DisplayName("Criar Show com VIP no caso médio (25%)")
        @Tag("VIP")
        public void testCriarShowComVipCasoMedio() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 25;

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
        @DisplayName("Criar Show com VIP no limite superior - 1 (29%)")
        @Tag("VIP")
        public void testCriarShowComVipLimiteSuperiorMenos1() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 29;

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
        @DisplayName("Criar Show com VIP no limite superior (30%)")
        @Tag("VIP")
        public void testCriarShowComVipLimiteSuperior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 30;

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
        @DisplayName("Criar Show com VIP acima do limite superior (31%)")
        @Tag("VIP")
        public void testCriarShowComVIPAcimaDolimiteSuperior() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = 31;
            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            } catch (Exception e) {
                assertEquals("Limites de VIP estão inválidos", e.getMessage());
            }
        }

        @Test
        @DisplayName("Criar Show com VIP negativo (-31%)")
        @Tag("VIP")
        public void testCriarShowComVIPNegativo() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.00;
            double vip = -31;
            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            } catch (Exception e) {
                assertEquals("Limites de VIP estão inválidos", e.getMessage());
            }
        }
    }

}