package main;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShowRepository {
    Show save(Show show);

    Optional<Show> findById(Date id, String artista);

    List<Show> findAll();

    void deleteById(Date id, String artista);
}