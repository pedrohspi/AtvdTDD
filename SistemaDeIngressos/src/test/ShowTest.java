package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShowTest {
    private Date date;
    private String NOME_ARTISTA = "Frei Gilson";
    private Double CACHE = 100.0;
    private Double DESPESAS_INFRA = 12000.00;
    private List<Tier> LOTES = new ArrayList<>();
    private Boolean IS_DATA_ESPECIAL = Boolean.TRUE;

    @BeforeEach
    void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 25);

        date = calendar.getTime();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getData() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, null, IS_DATA_ESPECIAL);
        assertEquals(this.date, show.getData(), "Data diferente");
    }

    @Test
    void getArtista() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, null, IS_DATA_ESPECIAL);
        assertEquals(this.NOME_ARTISTA, show.getArtista());
    }

    @Test
    void getCache() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, null, IS_DATA_ESPECIAL);
        assertEquals(this.CACHE, show.getCache(), "Cache diferente");
    }

    @Test
    void getDespesasInfra() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, null, IS_DATA_ESPECIAL);
        assertEquals(this.DESPESAS_INFRA, show.getDespesasInfra());
    }

    @Test
    void getLotes() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, new ArrayList<>(), IS_DATA_ESPECIAL);
        assertEquals(this.LOTES.size(), show.getLotes().size(), "Lotes diferente");
    }

    @Test
    void getIsEspecial() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, null, IS_DATA_ESPECIAL);
        assertEquals(this.IS_DATA_ESPECIAL, show.isEspecial());
    }

    @Test
    void setData() {
        Show show = new Show();
        show.setData(date);
        assertEquals(this.date, show.getData(), "Data diferente");
    }

    @Test
    void setArtista() {
        Show show = new Show();
        show.setArtista(NOME_ARTISTA);
        assertEquals(this.NOME_ARTISTA, show.getArtista());
    }

    @Test
    void setCache() {
        Show show = new Show();
        show.setCache(CACHE);
        assertEquals(CACHE, show.getCache(), "Cache diferente");
    }

    @Test
    void setDespesasInfra() {
        Show show = new Show();
        show.setDespesasInfra(DESPESAS_INFRA);
        assertEquals(DESPESAS_INFRA, show.getDespesasInfra());
    }

    @Test
    void setLotes() {
        Show show = new Show();
        ArrayList<Tier> lotes = new ArrayList<>();
        lotes.add(new Tier(12.00, null, 2L));
        show.setLotes(lotes);
        assertEquals(1, show.getLotes().size(), "Lotes diferente");
    }

    @Test
    void setIsEspecial() {
        Show show = new Show();
        show.setEspecial(IS_DATA_ESPECIAL);
        assertEquals(IS_DATA_ESPECIAL, show.isEspecial());
    }

    @Test
    void toStringTest() {
        Show show = new Show(date, NOME_ARTISTA, CACHE, DESPESAS_INFRA, null, IS_DATA_ESPECIAL);
        assertEquals(show.toString(), "data=" + date +
                ", artista='" + NOME_ARTISTA + '\'' +
                ", cache=" + CACHE +
                ", despesasInfra=" + DESPESAS_INFRA +
                ", lotes=null" +
                ", isEspecial=" + IS_DATA_ESPECIAL, "Date diferente");
    }
}
