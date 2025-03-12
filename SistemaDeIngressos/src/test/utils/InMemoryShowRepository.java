package br.edu.ufcg.ccc.vv.utils;

import br.edu.ufcg.ccc.vv.models.ShowModel;
import br.edu.ufcg.ccc.vv.repository.ShowRepository;

import java.util.*;

public class InMemoryShowRepository implements ShowRepository {
    private final Map<String, ShowModel> database = new HashMap<>();

    @Override
    public ShowModel save(ShowModel show) {
        String key = generateKey(show.getData(), show.getArtista());
        database.put(key, show);
        return show;
    }

    @Override
    public Optional<ShowModel> findById(Date id, String artista) {
        String key = generateKey(id, artista);
        return Optional.ofNullable(database.get(key));
    }

    @Override
    public List<ShowModel> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Date id, String artista) {

    }

    private String generateKey(Date data, String artista) {
        return data.toString() + "|" + artista;
    }
}