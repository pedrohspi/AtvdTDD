package main;

public class Report {
    private Integer numTicketVip;
    private Integer numTicketNormal;
    private Integer numTicketMeia;
    private Double valueTotal;
    private StatusEnum status;

    public Report() {
    }

    public Integer getNumTicketVip() {
        return this.numTicketVip;
    }

    public void setNumTicketVip(Integer numTicketVip) {
        this.numTicketVip = numTicketVip;
    }

    public Integer getNumTicketNormal() {
        return this.numTicketNormal;
    }

    public void setNumTicketNormal(Integer numTicketNormal) {
        this.numTicketNormal = numTicketNormal;
    }

    public Integer getNumTicketMeia() {
        return this.numTicketMeia;
    }

    public void setNumTicketMeia(Integer numTicketMeia) {
        this.numTicketMeia = numTicketMeia;
    }

    public Double getValueTotal() {
        return valueTotal;
    }

    public void setValueTotal(Double valueTotal) {
        this.valueTotal = valueTotal;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}
