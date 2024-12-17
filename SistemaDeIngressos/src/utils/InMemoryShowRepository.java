
package utils;

import main.*;

import java.util.*;

public class InMemoryShowRepository implements ShowRepository {
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
        return List.of();
    }

    @Override
    public void deleteById(Date id, String artista) {

    }

    private String generateKey(Date data, String artista) {
        return data.toString() + "|" + artista;
    }
}