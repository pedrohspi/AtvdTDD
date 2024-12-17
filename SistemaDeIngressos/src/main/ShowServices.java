package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowServices {
    private final ShowRepository showRepository;
    private Long currentIngressoId;
    private Long loteId;

    public ShowServices(ShowRepository showRepository) {
        this.showRepository = showRepository;
        this.currentIngressoId = 0L;
        this.loteId = 0L;
    }

    public void criarShow(Date date, String artista, Double cache, Double totalDespesas, Integer quantLotes,
            Integer quantIngressosPorLote, Double precoNormal, Boolean isDataEspecial,
            Double descontoLote, Double vip) {

        if (vip < 20 || vip > 30)
            throw new IllegalArgumentException("Limites de VIP estão inválidos");
        if (quantIngressosPorLote < 1)
            throw new IllegalArgumentException("Quantidade de ingressos inválidos");
        if (descontoLote < 0 || descontoLote > 25)
            throw new IllegalArgumentException("Desconto inválidos");

        List<LoteModel> loteModels = criarLotes(quantLotes, quantIngressosPorLote, precoNormal, descontoLote,
                vip / 100);
        if (isDataEspecial) {
            totalDespesas *= 1.15;
        }
        Show showModel = new ShowModel(date, artista, cache, totalDespesas, loteModels, isDataEspecial);
        showRepository.save(showModel);
    }

    private List<LoteModel> criarLotes(Integer quantLotes, Integer quantIngressosPorLote, Double precoNormal,
            Double descontoLote, Double vip) {
        List<LoteModel> loteModels = new ArrayList<>();
        descontoLote = ajustarDesconto(descontoLote);

        for (int i = 0; i < quantLotes; i++) {
            List<TicketModel> ticketModels = criarIngressos(quantIngressosPorLote, precoNormal, vip, descontoLote);
            LoteModel loteModel = new LoteModel(descontoLote, ticketModels, loteId++);
            loteModels.add(loteModel);
        }
        return loteModels;
    }

    private List<TicketModel> criarIngressos(Integer quantIngressosPorLote, Double precoBase, Double vip,
            Double descontoLote) {
        List<TicketModel> ticketModels = new ArrayList<>();

        int expectedVip = (int) Math.round(quantIngressosPorLote * vip);
        int expectedMeiaEntrada = (int) Math.round(quantIngressosPorLote * 0.10);

        int totalIngressos = expectedVip + expectedMeiaEntrada;
        int expectedNormal = quantIngressosPorLote - totalIngressos;

        double precoVip = (precoBase * 2) - (2 * precoBase * descontoLote / 100);
        double precoNormal = precoBase - (precoBase * descontoLote / 100);

        adicionarIngressos(ticketModels, expectedVip, TicketTypeEnum.VIP, precoVip);
        adicionarIngressos(ticketModels, expectedMeiaEntrada, TicketTypeEnum.MEIA_ENTRADA, precoBase * 0.5);
        adicionarIngressos(ticketModels, expectedNormal, TicketTypeEnum.NORMAL, precoNormal);

        return ticketModels;
    }

    private void adicionarIngressos(List<TicketModel> ticketModels, int quantidade, TicketTypeEnum tipo, Double preco) {
        for (int i = 0; i < quantidade; i++) {
            TicketModel ticketModel = new TicketModel(currentIngressoId++, tipo, false, preco);
            ticketModels.add(ticketModel);
        }
    }

    private Double ajustarDesconto(Double descontoLote) {
        if (descontoLote > 25)
            return 25.0;
        if (descontoLote < 0)
            return 0.0;
        return descontoLote;
    }

    public TicketModel comprarIngresso(Date date, String artista, Long idLote, TicketTypeEnum tipo) {
        ShowModel showModel = showRepository.findById(date, artista)
                .orElseThrow(() -> new IllegalArgumentException("Show não encontrado"));

        LoteModel lote = showModel.getLotes().stream()
                .filter(l -> l.getId().equals(idLote))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado"));

        return lote.getIngressos().stream()
                .filter(ticket -> !ticket.isVendido() && (tipo == null || ticket.getTipoIngresso().equals(tipo)))
                .findFirst()
                .map(ticket -> {
                    ticket.setVendido(true);
                    return ticket;
                })
                .orElseThrow(() -> new IllegalStateException("Nenhum ingresso disponível para o lote"));
    }

    public ReportModel criarReport(Date date, String artista) {
        ShowModel showModel = showRepository.findById(date, artista)
                .orElseThrow(() -> new IllegalArgumentException("Show não encontrado"));

        ReportModel report = new ReportModel();
        Double totalValue = 0.00;
        int[] totalTicketsSold = new int[TicketTypeEnum.values().length];

        for (LoteModel loteModel : showModel.getLotes()) {
            for (TicketModel ticketModel : loteModel.getIngressos()) {
                if (ticketModel.isVendido()) {
                    TicketTypeEnum tipo = ticketModel.getTipoIngresso();
                    totalTicketsSold[tipo.ordinal()]++;
                    totalValue += ticketModel.getValor();
                }
            }
        }

        report.setNumTicketMeia(totalTicketsSold[TicketTypeEnum.MEIA_ENTRADA.ordinal()]);
        report.setNumTicketNormal(totalTicketsSold[TicketTypeEnum.NORMAL.ordinal()]);
        report.setNumTicketVip(totalTicketsSold[TicketTypeEnum.VIP.ordinal()]);
        report.setTotalValue(totalValue);

        Double totalCost = showModel.getDespesasInfra() + showModel.getCache();
        if (totalValue < totalCost) {
            report.setStatus(StatusEnum.PREJUÍZO);
        } else if (totalValue > totalCost) {
            report.setStatus(StatusEnum.LUCRO);
        } else {
            report.setStatus(StatusEnum.ESTÁVEL);
        }

        return report;
    }
}
