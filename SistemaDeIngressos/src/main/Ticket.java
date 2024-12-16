package main;

public class Ticket {
    private Long id;
    private TicketTypeEnum ticketType;
    private Boolean isSold;
    private Double value;

    public Ticket() {
    }

    public Ticket(Long id, TicketTypeEnum ticketType, Boolean isSold, Double value) {
        this.id = id;
        this.ticketType = ticketType;
        this.isSold = isSold;
        this.value = value;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketTypeEnum getTicketType() {
        return this.ticketType;
    }

    public void setTicketType(TicketTypeEnum ticketType) {
        this.ticketType = ticketType;
    }

    public Boolean isSold() {
        return this.isSold;
    }

    public void setSold(Boolean sold) {
        this.isSold = sold;
    }

    public Double getValor() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Ticket{");
        sb.append("value=").append(value);
        sb.append(", isSold=").append(isSold);
        sb.append(", ticketType=").append(ticketType);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}