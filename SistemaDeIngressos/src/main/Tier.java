package main;

import java.util.List;

public class Tier {
    private Long id;
    private List<Ticket> tickets;
    private Double discount;

    public Tier(Double discount, List<Ticket> tickets, Long id) {
        this.discount = discount;
        this.tickets = tickets;
        this.id = id;
    }

    public Tier() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}