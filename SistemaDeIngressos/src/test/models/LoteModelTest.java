package br.edu.ufcg.ccc.vv.models;

import br.edu.ufcg.ccc.vv.models.IngressoModel;
import br.edu.ufcg.ccc.vv.models.LoteModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoteModelTest {

    private LoteModel lote;
    private IngressoModel ingresso;

    @BeforeEach
    public void setUp() {
        lote = new LoteModel();
        ingresso = new IngressoModel();
    }

    @Test
    public void testConstructor() {
        assertNull(lote.getId());
        assertNull(lote.getIngressos());
        assertNull(lote.getDesconto());
    }

    @Test
    public void testGetSetId() {
        Long id = 1L;
        lote.setId(id);
        assertEquals(id, lote.getId());

        lote.setId(null);
        assertNull(lote.getId());
    }

    @Test
    public void testGetSetIngressos() {
        List<IngressoModel> ingressos = new ArrayList<>();
        ingressos.add(ingresso);
        lote.setIngressos(ingressos);
        assertEquals(ingressos, lote.getIngressos());

        lote.setIngressos(null);
        assertNull(lote.getIngressos());

        ingressos = new ArrayList<>();
        lote.setIngressos(ingressos);
        assertTrue(lote.getIngressos().isEmpty());
    }

    @Test
    public void testGetSetDesconto() {
        Double desconto = 10.0;
        lote.setDesconto(desconto);
        assertEquals(desconto, lote.getDesconto(), 0.001);

        lote.setDesconto(null);
        assertNull(lote.getDesconto());

        desconto = -5.0;
        lote.setDesconto(desconto);
        assertEquals(desconto, lote.getDesconto(), 0.001);
    }
}