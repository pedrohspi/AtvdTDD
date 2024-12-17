package test;

import main.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TierTest {

    private Tier tier;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        tier = new Tier();
        ticket = new Ticket();
    }

    @Test
    public void testConstructor() {
        assertNull(tier.getId());
        assertNull(tier.getTickets());
        assertNull(tier.getDiscount());
    }

    @Test
    public void testGetSetId() {
        Long id = 1L;
        tier.setId(id);
        assertEquals(id, tier.getId());

        tier.setId(null);
        assertNull(tier.getId());
    }

    @Test
    public void testGetSetTickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        tier.setTickets(tickets);
        assertEquals(tickets, tier.getTickets());

        tier.setTickets(null);
        assertNull(tier.getTickets());

        tickets = new ArrayList<>();
        tier.setTickets(tickets);
        assertTrue(tier.getTickets().isEmpty());
    }

    @Test
    public void testGetSetDiscount() {
        Double discount = 10.0;
        tier.setDiscount(discount);
        assertEquals(discount, tier.getDiscount(), 0.001);

        tier.setDiscount(null);
        assertNull(tier.getDiscount());

        discount = -5.0;
        tier.setDiscount(discount);
        assertEquals(discount, tier.getDiscount(), 0.001);
    }
}