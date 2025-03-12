package services;

import models.*;
import repository.ShowRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Serviço que gerencia operações relacionadas a shows.
 * Inclui a criação de shows, compra de ingressos e geração de relatórios sobre shows.
 */
public class ShowServices {
    private final ShowRepository showRepository;
    private Long currentIngressoId;
    private Long loteId;

    /**
     * Constrói uma instância de ShowServices com o repositório de shows fornecido.
     *
     * @param showRepository o repositório de shows
     */
    public ShowServices(ShowRepository showRepository) {
        this.showRepository = showRepository;
        this.currentIngressoId = 0L;
        this.loteId = 0L;
    }

    /**
     * Cria um show e o salva no repositório.
     *
     * @param date a data do show
     * @param artista o artista do show
     * @param cache o cachê do artista
     * @param totalDespesas as despesas totais do show
     * @param quantLotes a quantidade de lotes de ingressos
     * @param quantIngressosPorLote a quantidade de ingressos por lote
     * @param precoNormal o preço do ingresso normal
     * @param isDataEspecial indica se a data do show é especial
     * @param descontoLote o desconto aplicável a cada lote
     * @param vip a porcentagem de ingressos VIP
     */
    public void criarShow(Date date, String artista, Double cache, Double totalDespesas, Integer quantLotes,
                          Integer quantIngressosPorLote, Double precoNormal, Boolean isDataEspecial,
                          Double descontoLote, Double vip) {

        if (vip < 20 || vip > 30)
            throw new IllegalArgumentException("Limites de VIP estão inválidos");
        if(quantIngressosPorLote < 1)
            throw new IllegalArgumentException("Quantidade de ingressos inválidos");
        if (descontoLote < 0 || descontoLote > 25)
            throw new IllegalArgumentException("Desconto inválidos");

        List<LoteModel> loteModels = criarLotes(quantLotes, quantIngressosPorLote, precoNormal, descontoLote, vip / 100);
        if (isDataEspecial) {
            totalDespesas *= 1.15;
        }
        ShowModel showModel = new ShowModel(date, artista, cache, totalDespesas, loteModels, isDataEspecial);
        showRepository.save(showModel);
    }

    private List<LoteModel> criarLotes(Integer quantLotes, Integer quantIngressosPorLote, Double precoNormal,
                                       Double descontoLote, Double vip) {
        List<LoteModel> loteModels = new ArrayList<>();
        descontoLote = ajustarDesconto(descontoLote);

        for (int i = 0; i < quantLotes; i++) {
            List<IngressoModel> ingressoModels = criarIngressos(quantIngressosPorLote, precoNormal, vip, descontoLote);
            LoteModel loteModel = new LoteModel(descontoLote, ingressoModels, loteId++);
            loteModels.add(loteModel);
        }
        return loteModels;
    }

    private List<IngressoModel> criarIngressos(Integer quantIngressosPorLote, Double precoBase, Double vip, Double descontoLote) {
        List<IngressoModel> ingressoModels = new ArrayList<>();

        int expectedVip = (int) Math.round(quantIngressosPorLote * vip);
        int expectedMeiaEntrada = (int) Math.round(quantIngressosPorLote * 0.10);

        int totalIngressos = expectedVip + expectedMeiaEntrada;
        int expectedNormal = quantIngressosPorLote - totalIngressos;

        double precoVip = (precoBase * 2) - (2 * precoBase * descontoLote/100);
        double precoNormal = precoBase - (precoBase * descontoLote/100);

        adicionarIngressos(ingressoModels, expectedVip, TipoIngressoEnum.VIP, precoVip);
        adicionarIngressos(ingressoModels, expectedMeiaEntrada, TipoIngressoEnum.MEIA_ENTRADA, precoBase * 0.5);
        adicionarIngressos(ingressoModels, expectedNormal, TipoIngressoEnum.NORMAL, precoNormal);

        return ingressoModels;
    }


    private void adicionarIngressos(List<IngressoModel> ingressoModels, int quantidade, TipoIngressoEnum tipo, Double preco) {
        for (int i = 0; i < quantidade; i++) {
            IngressoModel ingressoModel = new IngressoModel(currentIngressoId++, tipo, false, preco);
            ingressoModels.add(ingressoModel);
        }
    }

    private Double ajustarDesconto(Double descontoLote) {
        if (descontoLote > 25) return 25.0;
        if (descontoLote < 0) return 0.0;
        return descontoLote;
    }

    /**
     * Compra um ingresso para um show específico.
     *
     * @param date a data do show
     * @param artista o artista do show
     * @param idLote o ID do lote de ingressos
     * @param tipo o tipo de ingresso a ser comprado
     * @return o ingresso comprado
     * @throws IllegalStateException se não houver ingressos disponíveis ou se o ingresso solicitado não estiver disponível
     */
    public IngressoModel comprarIngresso(Date date, String artista, Long idLote, TipoIngressoEnum tipo) {
        ShowModel showModel = showRepository.findById(date, artista)
                .orElseThrow(() -> new IllegalArgumentException("Show não encontrado"));

        LoteModel lote = showModel.getLotes().stream()
                .filter(l -> l.getId().equals(idLote))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado"));

        return lote.getIngressos().stream()
                .filter(ingresso -> !ingresso.isVendido() && (tipo == null || ingresso.getTipoIngresso().equals(tipo)))
                .findFirst()
                .map(ingresso -> {
                    ingresso.setVendido(true);
                    return ingresso;
                })
                .orElseThrow(() -> new IllegalStateException("Nenhum ingresso disponível para o lote"));
    }

    /**
     * Cria um relatório sobre um show específico.
     *
     * @param date a data do show
     * @param artista o artista do show
     * @return o relatório criado
     * @throws IllegalArgumentException se o show não for encontrado
     */
    public RelatorioModel criarRelatorio(Date date, String artista) {
        ShowModel showModel = showRepository.findById(date, artista)
                .orElseThrow(() -> new IllegalArgumentException("Show não encontrado"));

        RelatorioModel relatorio = new RelatorioModel();
        Double valorTotal = 0.00;
        int[] totalIngressosVendidos = new int[TipoIngressoEnum.values().length];

        for (LoteModel loteModel : showModel.getLotes()) {
            for (IngressoModel ingressoModel : loteModel.getIngressos()) {
                if (ingressoModel.isVendido()) {
                    TipoIngressoEnum tipo = ingressoModel.getTipoIngresso();
                    totalIngressosVendidos[tipo.ordinal()]++;
                    valorTotal += ingressoModel.getValor();
                }
            }
        }

        relatorio.setNumIngressoMeia(totalIngressosVendidos[TipoIngressoEnum.MEIA_ENTRADA.ordinal()]);
        relatorio.setNumIngressoNormal(totalIngressosVendidos[TipoIngressoEnum.NORMAL.ordinal()]);
        relatorio.setNumIngressoVip(totalIngressosVendidos[TipoIngressoEnum.VIP.ordinal()]);
        relatorio.setValorTotal(valorTotal);

        Double custoTotal = showModel.getDespesasInfra() + showModel.getCache();
        if (valorTotal < custoTotal) {
            relatorio.setStatus(StatusEnum.PREJUÍZO);
        } else if (valorTotal > custoTotal) {
            relatorio.setStatus(StatusEnum.LUCRO);
        } else {
            relatorio.setStatus(StatusEnum.ESTÁVEL);
        }

        return relatorio;
    }
}
