package repository;

import java.util.*;

import models.ShowModel;

/**
 * Implementação da interface ShowRepository que armazena os dados dos shows em memória usando um Map.
 */
public class ShowRepositoryImpl implements ShowRepository {
    private final Map<String, ShowModel> database = new HashMap<>();

    /**
     * Salva um show no repositório.
     *
     * @param show o show a ser salvo
     * @return o show salvo
     */
    @Override
    public ShowModel save(ShowModel show) {
        String key = generateKey(show.getData(), show.getArtista());
        database.put(key, show);
        return show;
    }

    /**
     * Encontra um show pelo seu ID (data) e artista.
     *
     * @param id a data do show
     * @param artista o artista do show
     * @return um Optional contendo o show, se encontrado
     */
    @Override
    public Optional<ShowModel> findById(Date id, String artista) {
        String key = generateKey(id, artista);
        return Optional.ofNullable(database.get(key));
    }

    /**
     * Retorna todos os shows armazenados no repositório.
     *
     * @return uma lista de todos os shows
     */
    @Override
    public List<ShowModel> findAll() {
        return new ArrayList<>(database.values());
    }

    /**
     * Deleta um show pelo seu ID (data) e artista.
     *
     * @param id a data do show
     * @param artista o artista do show
     */
    @Override
    public void deleteById(Date id, String artista) {
        String key = generateKey(id, artista);
        database.remove(key);
    }

    /**
     * Gera uma chave única para identificar um show com base na data e no artista.
     *
     * @param data a data do show
     * @param artista o artista do show
     * @return a chave gerada
     */
    private String generateKey(Date data, String artista) {
        return data.toString() + "|" + artista;
    }
}
