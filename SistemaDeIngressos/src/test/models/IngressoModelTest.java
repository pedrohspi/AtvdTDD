package br.edu.ufcg.ccc.vv.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IngressoModelTest {

    private IngressoModel ingresso;

    @BeforeEach
    public void setUp() {
        ingresso = new IngressoModel();
    }

    @Test
    public void testConstructor() {
        assertNull(ingresso.getId());
        assertNull(ingresso.getTipoIngresso());
        assertNull(ingresso.isVendido());
        assertNull(ingresso.getValor());
    }

    @Test
    public void testGetSetId() {
        Long id = 1L;
        ingresso.setId(id);
        assertEquals(id, ingresso.getId());

        ingresso.setId(null);
        assertNull(ingresso.getId());
    }
    @Test
    public void testToString() {
        TipoIngressoEnum tipoIngresso = TipoIngressoEnum.NORMAL;

        IngressoModel ingresso = new IngressoModel(1L, tipoIngresso, true, 150.00);

        String expectedString = "IngressoModel{valor=150.0, isVendido=true, tipoIngresso=NORMAL, id=1}";

        // Verificando se o resultado do toString está correto
        assertEquals(expectedString, ingresso.toString());
    }

    @Test
    public void testGetSetTipoIngresso() {
        TipoIngressoEnum tipo = TipoIngressoEnum.VIP;
        ingresso.setTipoIngresso(tipo);
        assertEquals(tipo, ingresso.getTipoIngresso());

        ingresso.setTipoIngresso(null);
        assertNull(ingresso.getTipoIngresso());
    }

    @Test
    public void testGetSetVendido() {
        Boolean isVendido = true;
        ingresso.setVendido(isVendido);
        assertEquals(isVendido, ingresso.isVendido());

        ingresso.setVendido(null);
        assertNull(ingresso.isVendido());

        ingresso.setVendido(false);
        assertFalse(ingresso.isVendido());
    }

    @Test
    public void testGetSetValor() {
        Double valor = 100.0;
        ingresso.setValor(valor);
        assertEquals(valor, ingresso.getValor(), 0.001);

        ingresso.setValor(null);
        assertNull(ingresso.getValor());

        valor = -50.0;
        ingresso.setValor(valor);
        assertEquals(valor, ingresso.getValor(), 0.001);
    }
}