import br.edu.ufcg.ccc.vv.models.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ShowRepositoryImplTest {
    private ShowRepository showRepository;

    @BeforeEach
    public void setUp() {
        showRepository = new ShowRepositoryImpl();
    }

    @Test
    public void testSave() {
        Date data = new Date();
        Show show = new Show(data, "Artista Teste", 1000.0, 5000.0, null, true);

        Show savedShow = showRepository.save(show);

        assertEquals(show, savedShow);
        assertTrue(showRepository.findById(data, "Artista Teste").isPresent());
    }

    @Test
    public void testFindById() {
        Date data = new Date();
        Show show = new Show(data, "Artista Teste", 1000.0, 5000.0, null, true);
        showRepository.save(show);

        Optional<Show> foundShow = showRepository.findById(data, "Artista Teste");

        assertTrue(foundShow.isPresent());
        assertEquals(show, foundShow.get());
    }

    @Test
    public void testFindAll() {
        Date data1 = new Date();
        Show show1 = new Show(data1, "Artista Teste 1", 1000.0, 5000.0, null, true);

        Date data2 = new Date();
        Show show2 = new Show(data2, "Artista Teste 2", 1000.0, 5000.0, null, true);

        showRepository.save(show1);
        showRepository.save(show2);

        List<Show> shows = showRepository.findAll();

        assertEquals(2, shows.size());
        assertTrue(shows.contains(show1));
        assertTrue(shows.contains(show2));
    }

    @Test
    public void testDeleteById() {
        Date data = new Date();
        Show show = new Show(data, "Artista Teste", 1000.0, 5000.0, null, true);
        showRepository.save(show);

        showRepository.deleteById(data, "Artista Teste");

        assertFalse(showRepository.findById(data, "Artista Teste").isPresent());
    }
}
