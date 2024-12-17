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

        List<Tier> tiers = criarLotes(quantLotes, quantIngressosPorLote, precoNormal, descontoLote, vip / 100);
        if (isDataEspecial) {
            totalDespesas *= 1.15;
        }
        Show show = new Show(date, artista, cache, totalDespesas, tiers, isDataEspecial);
        showRepository.save(show);
    }

    private List<Tier> criarLotes(Integer quantLotes, Integer quantIngressosPorLote, Double precoNormal,
            Double descontoLote, Double vip) {
        List<Tier> tiers = new ArrayList<>();
        descontoLote = ajustarDesconto(descontoLote);

        for (int i = 0; i < quantLotes; i++) {
            List<Ticket> tickets = criarIngressos(quantIngressosPorLote, precoNormal, vip, descontoLote);
            Tier tier = new Tier(descontoLote, tickets, loteId++);
            tiers.add(tier);
        }
        return tiers;
    }

    private List<Ticket> criarIngressos(Integer quantIngressosPorLote, Double precoBase, Double vip,
            Double descontoLote) {
        List<Ticket> tickets = new ArrayList<>();

        int expectedVip = (int) Math.round(quantIngressosPorLote * vip);
        int expectedMeiaEntrada = (int) Math.round(quantIngressosPorLote * 0.10);

        int totalIngressos = expectedVip + expectedMeiaEntrada;
        int expectedNormal = quantIngressosPorLote - totalIngressos;

        double precoVip = (precoBase * 2) - (2 * precoBase * descontoLote / 100);
        double precoNormal = precoBase - (precoBase * descontoLote / 100);

        adicionarIngressos(tickets, expectedVip, TicketTypeEnum.VIP, precoVip);
        adicionarIngressos(tickets, expectedMeiaEntrada, TicketTypeEnum.MEIA_ENTRADA, precoBase * 0.5);
        adicionarIngressos(tickets, expectedNormal, TicketTypeEnum.NORMAL, precoNormal);

        return tickets;
    }

    private void adicionarIngressos(List<Ticket> tickets, int quantidade, TicketTypeEnum tipo, Double preco) {
        for (int i = 0; i < quantidade; i++) {
            Ticket ticket = new Ticket(currentIngressoId++, tipo, false, preco);
            tickets.add(ticket);
        }
    }

    private Double ajustarDesconto(Double descontoLote) {
        if (descontoLote > 25)
            return 25.0;
        if (descontoLote < 0)
            return 0.0;
        return descontoLote;
    }

    public Ticket comprarIngresso(Date date, String artista, Long idLote, TicketTypeEnum tipo) {
        Show show = showRepository.findById(date, artista)
                .orElseThrow(() -> new IllegalArgumentException("Show não encontrado"));

        Tier tier = show.getLotes().stream()
                .filter(l -> l.getId().equals(idLote))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lote não encontrado"));

        return tier.getTickets().stream()
                .filter(ticket -> !ticket.isSold() && (tipo == null || ticket.getTicketType().equals(tipo)))
                .findFirst()
                .map(ticket -> {
                    ticket.setSold(null);(true);
                    return ticket;
                })
                .orElseThrow(() -> new IllegalStateException("Nenhum ingresso disponível para o lote"));
    }

    public Report criarReport(Date date, String artista) {
        Show show = showRepository.findById(date, artista)
                .orElseThrow(() -> new IllegalArgumentException("Show não encontrado"));

        Report report = new Report();
        Double totalValue = 0.00;
        int[] totalTicketsSold = new int[TicketTypeEnum.values().length];

        for (Tier tier : show.getLotes()) {
            for (Ticket ticket : tier.getTickets()) {
                if (ticket.isSold()) {
                    TicketTypeEnum tipo = ticket.getTicketType();
                    totalTicketsSold[tipo.ordinal()]++;
                    totalValue += ticket.getValor();
                }
            }
        }

        report.setNumTicketMeia(totalTicketsSold[TicketTypeEnum.MEIA_ENTRADA.ordinal()]);
        report.setNumTicketNormal(totalTicketsSold[TicketTypeEnum.NORMAL.ordinal()]);
        report.setNumTicketVip(totalTicketsSold[TicketTypeEnum.VIP.ordinal()]);
        report.setValueTotal(totalValue);

        Double totalCost = show.getDespesasInfra() + show.getCache();
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
