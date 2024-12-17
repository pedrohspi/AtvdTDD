package main;

import java.util.*;

public class ShowRepositoryImpl implements ShowRepository {
    private final Map<String, Show> database = new HashMap<>();

    @Override
    public Show save(Show show) {
        String key = generateKey(show.getData(), show.getArtista());
        database.put(key, show);
        return show;
    }

    @Override
    public Optional<Show> findById(Date id, String artista) {
        String key = generateKey(id, artista);
        return Optional.ofNullable(database.get(key));
    }

    @Override
    public List<Show> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(Date id, String artista) {
        String key = generateKey(id, artista);
        database.remove(key);
    }

    private String generateKey(Date data, String artista) {
        return data.toString() + "|" + artista;
    }
}