package functionaltests;

import models.IngressoModel;
import models.LoteModel;
import models.ShowModel;
import models.TipoIngressoEnum;
import repository.ShowRepository;
import services.ShowServices;
import utilstests.InMemoryShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TabelaDecisoesDesconto {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
    }

    @Nested
    class ComprarIngresso{
        @Test
        public void testComprarIngressoVIPComDesconto() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 10.;
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
            assertEquals(comprado.getValor(), 2 * precoNormal * 0.9);
            assertTrue(lote.getIngressos().getFirst().isVendido());
        }

        @Test
        public void testComprarIngressoVIPSemDesconto() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            double precoNormal = 10.0;
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
            assertEquals(comprado.getValor(), 2 * precoNormal);
            assertTrue(lote.getIngressos().getFirst().isVendido());
        }

        @Test
        public void testComprarIngressoNormalComDesconto() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 15.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo
            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();

            // Compra um ingresso
            IngressoModel comprado = showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.NORMAL);

            assertNotNull(comprado);
            assertEquals(comprado.getValor(), precoNormal * 0.85);
        }

        @Test
        public void testComprarIngressoNormalSemDesconto() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo
            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();

            // Compra um ingresso
            IngressoModel comprado = showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.NORMAL);

            assertNotNull(comprado);
            assertEquals(comprado.getValor(), precoNormal);
        }

        @Test
        public void testComprarIngressoMeiaComDesconto() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 10.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo
            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();

            // Compra um ingresso
            IngressoModel comprado = showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.MEIA_ENTRADA);

            assertNotNull(comprado);
            assertEquals(comprado.getValor(), precoNormal * 0.5);
        }

        @Test
        public void testComprarIngressoMeiaSemDesconto() {
            Date data = new Date();
            String artista = "Artista Teste";
            Double cache = 1000.0;
            Double totalDespesas = 2000.0;
            Integer quantLotes = 1;
            Integer quantIngressosPorLote = 10;
            double precoNormal = 10.0;
            Boolean isDataEspecial = false;
            Double descontoLote = 0.;
            double vip = 30;

            // Cria o show
            showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal, isDataEspecial, descontoLote, vip);

            // Recupera o show salvo
            ShowModel show = showRepository.findById(data, artista).get();
            LoteModel lote = show.getLotes().getFirst();

            // Compra um ingresso
            IngressoModel comprado = showServices.comprarIngresso(data, artista, lote.getId(), TipoIngressoEnum.MEIA_ENTRADA);

            assertNotNull(comprado);
            assertEquals(comprado.getValor(), precoNormal * 0.5);
        }
    }
}
