package br.edu.ufcg.ccc.vv.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RelatorioModelTest {

    private RelatorioModel relatorio;

    @BeforeEach
    public void setUp() {
        relatorio = new RelatorioModel();
    }

    @Test
    public void testConstructor() {
        assertNull(relatorio.getNumIngressoMeia());
        assertNull(relatorio.getNumIngressoNormal());
        assertNull(relatorio.getNumIngressoVip());
        assertNull(relatorio.getValorTotal());
        assertNull(relatorio.getStatus());
    }

    @Test
    public void testGetSetNumIngressoMeia() {
        Integer numIngresso = 5;
        relatorio.setNumIngressoMeia(numIngresso);
        assertEquals(numIngresso, relatorio.getNumIngressoMeia());

        relatorio.setNumIngressoMeia(null);
        assertNull(relatorio.getNumIngressoMeia());

        numIngresso = -1;
        relatorio.setNumIngressoMeia(numIngresso);
        assertEquals(numIngresso, relatorio.getNumIngressoMeia());
    }

    @Test
    public void testGetSetNumIngressoNormal() {
        Integer numIngresso = 5;
        relatorio.setNumIngressoNormal(numIngresso);
        assertEquals(numIngresso, relatorio.getNumIngressoNormal());

        relatorio.setNumIngressoNormal(null);
        assertNull(relatorio.getNumIngressoNormal());

        numIngresso = -1;
        relatorio.setNumIngressoNormal(numIngresso);
        assertEquals(numIngresso, relatorio.getNumIngressoNormal());
    }

    @Test
    public void testGetSetNumIngressoVip() {
        Integer numIngresso = 5;
        relatorio.setNumIngressoVip(numIngresso);
        assertEquals(numIngresso, relatorio.getNumIngressoVip());

        relatorio.setNumIngressoVip(null);
        assertNull(relatorio.getNumIngressoVip());

        numIngresso = -1;
        relatorio.setNumIngressoVip(numIngresso);
        assertEquals(numIngresso, relatorio.getNumIngressoVip());
    }

    @Test
    public void testGetSetValorTotal() {
        Double valorTotal = 100.0;
        relatorio.setValorTotal(valorTotal);
        assertEquals(valorTotal, relatorio.getValorTotal(), 0.001);

        relatorio.setValorTotal(null);
        assertNull(relatorio.getValorTotal());

        valorTotal = -50.0;
        relatorio.setValorTotal(valorTotal);
        assertEquals(valorTotal, relatorio.getValorTotal(), 0.001);
    }

    @Test
    public void testGetSetStatus() {
        StatusEnum status = StatusEnum.ESTÁVEL;
        relatorio.setStatus(status);
        assertEquals(status, relatorio.getStatus());

        relatorio.setStatus(null);
        assertNull(relatorio.getStatus());

        status = StatusEnum.LUCRO;
        relatorio.setStatus(status);
        assertEquals(status, relatorio.getStatus());
    }
}