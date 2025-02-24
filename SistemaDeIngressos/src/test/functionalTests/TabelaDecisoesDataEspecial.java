package br.edu.ufcg.ccc.vv.functionalTests;

import br.edu.ufcg.ccc.vv.models.LoteModel;
import br.edu.ufcg.ccc.vv.models.ShowModel;
import br.edu.ufcg.ccc.vv.models.TipoIngressoEnum;
import br.edu.ufcg.ccc.vv.repository.ShowRepository;
import br.edu.ufcg.ccc.vv.services.ShowServices;
import br.edu.ufcg.ccc.vv.utils.InMemoryShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TabelaDecisoesDataEspecial {
    private ShowRepository showRepository;
    private ShowServices showServices;

    @BeforeEach
    public void setUp() {
        // Instanciando o repositório em memória
        showRepository = new InMemoryShowRepository();
        showServices = new ShowServices(showRepository);
    }

    @Test
    public void testCriarShowEmDataEspecial() {
        Date data = new Date();
        String artista = "Artista Teste";
        Double cache = 1000.0;
        Double totalDespesas = 2000.0;
        Integer quantLotes = 1;
        Integer quantIngressosPorLote = 100;
        Double precoNormal = 10.0;
        Boolean isDataEspecial = true;
        Double descontoLote = 0.00;
        double vip = 20;

        // Execute o método criarShow
        showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal,
                isDataEspecial, descontoLote, vip);

        // Verificar se o show foi salvo corretamente
        ShowModel show = showRepository.findById(data, artista).get();
        assertNotNull(show);

        assertEquals(show.getDespesasInfra(), totalDespesas * 1.15);
    }

    @Test
    public void testCriarShowEmDataNormal() {
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
        showServices.criarShow(data, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal,
                isDataEspecial, descontoLote, vip);

        // Verificar se o show foi salvo corretamente
        ShowModel show = showRepository.findById(data, artista).get();
        assertNotNull(show);

        assertEquals(show.getDespesasInfra(), totalDespesas);
    }
}