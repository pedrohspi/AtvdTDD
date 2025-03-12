package br.edu.ufcg.ccc.vv.repository;

import br.edu.ufcg.ccc.vv.models.ShowModel;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ShowRepository {
    ShowModel save(ShowModel show);
    Optional<ShowModel> findById(Date id, String artista);
    List<ShowModel> findAll();
    void deleteById(Date id, String artista);
}