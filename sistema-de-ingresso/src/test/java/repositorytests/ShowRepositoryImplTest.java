package repositorytests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.ShowModel;
import repository.ShowRepository;
import repository.ShowRepositoryImpl;

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
        ShowModel show = new ShowModel(data, "Artista Teste", 1000.0, 5000.0, null, true);

        ShowModel savedShow = showRepository.save(show);

        assertEquals(show, savedShow);
        assertTrue(showRepository.findById(data, "Artista Teste").isPresent());
    }

    @Test
    public void testFindById() {
        Date data = new Date();
        ShowModel show = new ShowModel(data, "Artista Teste", 1000.0, 5000.0, null, true);
        showRepository.save(show);

        Optional<ShowModel> foundShow = showRepository.findById(data, "Artista Teste");

        assertTrue(foundShow.isPresent());
        assertEquals(show, foundShow.get());
    }

    @Test
    public void testFindAll() {
        Date data1 = new Date();
        ShowModel show1 = new ShowModel(data1, "Artista Teste 1", 1000.0, 5000.0, null, true);

        Date data2 = new Date();
        ShowModel show2 = new ShowModel(data2, "Artista Teste 2", 1000.0, 5000.0, null, true);

        showRepository.save(show1);
        showRepository.save(show2);

        List<ShowModel> shows = showRepository.findAll();

        assertEquals(2, shows.size());
        assertTrue(shows.contains(show1));
        assertTrue(shows.contains(show2));
    }

    @Test
    public void testDeleteById() {
        Date data = new Date();
        ShowModel show = new ShowModel(data, "Artista Teste", 1000.0, 5000.0, null, true);
        showRepository.save(show);

        showRepository.deleteById(data, "Artista Teste");

        assertFalse(showRepository.findById(data, "Artista Teste").isPresent());
    }
}