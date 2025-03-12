package servicestest;

import models.*;
import repository.ShowRepository;
import services.ShowServices;
import utilstests.InMemoryShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShowServicesTest {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
    }

    @Nested
    class CriarShow{
        @Test
        public void testCriarShowComDescontoZero() {
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
        public void testCriarShowComDescontoAcimaDoPermitido() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 30.; // Desconto acima do máximo permitido
            double vip = 30;

            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Desconto inválidos", e.getMessage());
            }
        }

        @Test
        public void testCriarShowSemIngressos() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 0; // Nenhum ingresso
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 10.;
            double vip = 30;

            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Quantidade de ingressos inválidos", e.getMessage());
            }
        }

        @Test
        public void testCriarShowComDataEspecial() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = true; // Data especial
            Double descontoLote = 10.;
            double vip = 30;

            // Execute o método criarShow
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Verificar se o show foi salvo corretamente
            ShowModel show = showRepository.findById(data, artista).get();
            assertNotNull(show);
            assertTrue(show.getDespesasInfra() > 2000.0); // Verifica se as despesas foram ajustadas para data especial
            assertEquals(2300.0, show.getDespesasInfra());
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
            Double descontoLote = -5.; // Desconto negativo
            double vip = 30;

            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Desconto inválidos", e.getMessage());
            }
        }
    }

    @Nested
    class ComprarIngresso{
        @Test
        public void testComprarIngressoComLoteDisponivel() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo
            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();
            IngressoModel ingresso = lote.getIngressos().getFirst();

            // Compra um ingresso
            IngressoModel comprado = showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.VIP);

            assertNotNull(comprado);
            assertEquals(ingresso, comprado);
            assertTrue(lote.getIngressos().getFirst().isVendido());
        }

        @Test
        public void testComprarIngressoComLoteTodosComprados() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 34;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo
            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();
            for (int i = 0; i < 10; i++) {
                // Compra um ingresso
                showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.VIP);
            }


            // Tenta comprar um ingresso
            Exception exception = assertThrows(IllegalStateException.class, () -> showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.VIP));

            assertEquals("Nenhum ingresso disponível para o lote", exception.getMessage());
        }

        @Test
        public void testComprarIngressoComLoteNaoDisponivel() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 0; // Nenhum ingresso
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Quantidade de ingressos inválidos", e.getMessage());
            }
        }

        @Test
        public void testComprarIngressoComLoteInexistente() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo

            // Tenta comprar um ingresso de um lote inexistente
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                showServices.comprarIngresso(data, artista, 999L, TipoIngressoEnum.VIP); // ID de lote inexistente
            });

            assertEquals("Lote não encontrado", exception.getMessage());
        }
    }

    @Nested
    class CriarRelatorio{
        @Test
        public void testCriarRelatorioComPrejuizo() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 100;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();
            for (int i = 0; i < 10; i++) {
                // Compra um ingresso
                showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.VIP);
            }

            // Cria o relatório
            RelatorioModel relatorio = showServices.criarRelatorio(data, artista);

            assertNotNull(relatorio);
            assertEquals(10, relatorio.getNumIngressoVip());
            assertEquals(0, relatorio.getNumIngressoMeia());
            assertEquals(0, relatorio.getNumIngressoNormal());
            assertEquals(200.0, relatorio.getValorTotal());
            assertEquals(StatusEnum.PREJUÍZO, relatorio.getStatus());
        }

        @Test
        public void testCriarRelatorioComIngressosComLucro() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 10.0;
            Double totalDespesas = 2.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();
            for (int i = 0; i < 10; i++) {
                // Compra um ingresso
                showServices.comprarIngresso(data, artista, lote.getId(), null);
            }

            // Cria o relatório
            RelatorioModel relatorio = showServices.criarRelatorio(data, artista);

            assertNotNull(relatorio);
            assertEquals(3, relatorio.getNumIngressoVip());
            assertEquals(1, relatorio.getNumIngressoMeia());
            assertEquals(6, relatorio.getNumIngressoNormal());
            assertEquals(125.0, relatorio.getValorTotal());
            assertEquals(StatusEnum.LUCRO, relatorio.getStatus());
        }

        @Test
        public void testCriarRelatorioComIngressosComEquilibrio() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 100.0;
            Double totalDespesas = 25.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();
            for (int i = 0; i < 10; i++) {
                // Compra um ingresso
                showServices.comprarIngresso(data, artista, lote.getId(), null);
            }

            // Cria o relatório
            RelatorioModel relatorio = showServices.criarRelatorio(data, artista);

            assertNotNull(relatorio);
            assertEquals(3, relatorio.getNumIngressoVip());
            assertEquals(1, relatorio.getNumIngressoMeia());
            assertEquals(6, relatorio.getNumIngressoNormal());
            assertEquals(125.0, relatorio.getValorTotal());
            assertEquals(StatusEnum.ESTÁVEL, relatorio.getStatus());
        }

        @Test
        public void testCriarRelatorioComNenhumIngresso() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 0; // Nenhum ingresso
            Double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            try {
                showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);
                fail("Era esperado que isso não funcionasse");
            }catch (Exception e){
                assertEquals("Quantidade de ingressos inválidos", e.getMessage());
            }
        }

        @Test
        public void testCriarRelatorioComShowNaoEncontrado() {
            Date data = new Date();
            String artista = "Artista Inexistente";

            // Tenta criar o relatório
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                showServices.criarRelatorio(data, artista);
            });

            assertEquals("Show não encontrado", exception.getMessage());
        }
    }
}
