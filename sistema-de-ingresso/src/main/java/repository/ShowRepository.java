package repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import models.ShowModel;

public interface ShowRepository {
    ShowModel save(ShowModel show);
    Optional<ShowModel> findById(Date id, String artista);
    List<ShowModel> findAll();
    void deleteById(Date id, String artista);
}
