package br.edu.ufcg.ccc.vv.functionalTests;

import br.edu.ufcg.ccc.vv.models.LoteModel;
import br.edu.ufcg.ccc.vv.models.ShowModel;
import br.edu.ufcg.ccc.vv.models.TipoIngressoEnum;
import br.edu.ufcg.ccc.vv.repository.ShowRepository;
import br.edu.ufcg.ccc.vv.services.ShowServices;
import br.edu.ufcg.ccc.vv.utils.InMemoryShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    class TestesDoValorDeDesconto{
        @Test
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
            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Desconto inválidos", e.getMessage());
            }
        }

        @Test
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
            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Desconto inválidos", e.getMessage());
            }
        }

        @Test
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
            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Desconto inválidos", e.getMessage());
            }
        }
    }
}