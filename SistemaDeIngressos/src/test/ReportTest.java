
package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {

    private Report report;

    @BeforeEach
    public void setUp() {
        report = new Report();
    }

    @Test
    public void testConstructor() {
        assertNull(report.getNumTicketMeia());
        assertNull(report.getNumTicketNormal());
        assertNull(report.getNumTicketVip());
        assertNull(report.getValueTotal());
        assertNull(report.getStatus());
    }

    @Test
    public void testGetSetNumTicketMeia() {
        Integer numTicket = 5;
        report.setNumTicketMeia(numTicket);
        assertEquals(numTicket, report.getNumTicketMeia());

        report.setNumTicketMeia(null);
        assertNull(report.getNumTicketMeia());

        numTicket = -1;
        report.setNumTicketMeia(numTicket);
        assertEquals(numTicket, report.getNumTicketMeia());
    }

    @Test
    public void testGetSetNumTicketNormal() {
        Integer numTicket = 5;
        report.setNumTicketNormal(numTicket);
        assertEquals(numTicket, report.getNumTicketNormal());

        report.setNumTicketNormal(null);
        assertNull(report.getNumTicketNormal());

        numTicket = -1;
        report.setNumTicketNormal(numTicket);
        assertEquals(numTicket, report.getNumTicketNormal());
    }

    @Test
    public void testGetSetNumTicketVip() {
        Integer numTicket = 5;
        report.setNumTicketVip(numTicket);
        assertEquals(numTicket, report.getNumTicketVip());

        report.setNumTicketVip(null);
        assertNull(report.getNumTicketVip());

        numTicket = -1;
        report.setNumTicketVip(numTicket);
        assertEquals(numTicket, report.getNumTicketVip());
    }

    @Test
    public void testGetSetValueTotal() {
        Double valueTotal = 100.0;
        report.setValueTotal(valueTotal);
        assertEquals(valueTotal, report.getValueTotal(), 0.001);

        report.setValueTotal(null);
        assertNull(report.getValueTotal());

        valueTotal = -50.0;
        report.setValueTotal(valueTotal);
        assertEquals(valueTotal, report.getValueTotal(), 0.001);
    }

    @Test
    public void testGetSetStatus() {
        StatusEnum status = StatusEnum.ESTÁVEL;
        report.setStatus(status);
        assertEquals(status, report.getStatus());

        report.setStatus(null);
        assertNull(report.getStatus());

        status = StatusEnum.LUCRO;
        report.setStatus(status);
        assertEquals(status, report.getStatus());
    }

}