package br.edu.ufcg.ccc.vv.functionalTests;

import br.edu.ufcg.ccc.vv.models.*;
import br.edu.ufcg.ccc.vv.repository.ShowRepository;
import br.edu.ufcg.ccc.vv.services.ShowServices;
import br.edu.ufcg.ccc.vv.utils.InMemoryShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticaoPorEquivalenciaRelatorio {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
    }

    @Nested
    class CriarRelatorio {
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
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal,
                    isDataEspecial, descontoLote, vip);

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
            assertTrue(relatorio.getValorTotal() < totalDespesas + cache);
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
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal,
                    isDataEspecial, descontoLote, vip);

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
            assertTrue(relatorio.getValorTotal() > totalDespesas + cache);
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
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal,
                    isDataEspecial, descontoLote, vip);

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
            assertEquals(relatorio.getValorTotal(), totalDespesas + cache);
            assertEquals(125.0, relatorio.getValorTotal());
            assertEquals(StatusEnum.ESTÁVEL, relatorio.getStatus());
        }
    }
}