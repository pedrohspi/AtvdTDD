package functionaltests;

import models.IngressoModel;
import models.LoteModel;
import models.ShowModel;
import models.TipoIngressoEnum;
import repository.ShowRepository;
import services.ShowServices;
import utilstests.InMemoryShowRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TabelaDecisoesEstadoIngresso {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
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

}
