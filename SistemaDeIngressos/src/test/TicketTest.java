import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        ticket = new Ticket();
    }

    @Test
    public void testConstructor() {
        assertNull(ticket.getId());
        assertNull(ticket.getTicketType());
        assertNull(ticket.isSold());
        assertNull(ticket.getValue());
    }

    @Test
    public void testGetSetId() {
        Long id = 1L;
        ticket.setId(id);
        assertEquals(id, ticket.getId());

        ticket.setId(null);
        assertNull(ticket.getId());
    }

    @Test
    public void testToString() {
        TicketTypeEnum ticketType = TicketTypeEnum.NORMAL;

        Ticket ticket = new Ticket(1L, ticketType, true, 150.00);

        String expectedString = "Ticket{value=150.0, isSold=true, ticketType=NORMAL, id=1}";

        assertEquals(expectedString, ticket.toString());
    }

    @Test
    public void testGetSetTicketType() {
        TicketTypeEnum type = TicketTypeEnum.VIP;
        ticket.setTicketType(type);
        assertEquals(type, ticket.getTicketType());

        ticket.setTicketType(null);
        assertNull(ticket.getTicketType());
    }

    @Test
    public void testGetSetSold() {
        Boolean isSold = true;
        ticket.setSold(isSold);
        assertEquals(isSold, ticket.isSold());

        ticket.setSold(null);
        assertNull(ticket.isSold());

        ticket.setSold(false);
        assertFalse(ticket.isSold());
    }

    @Test
    public void testGetSetValue() {
        Double value = 100.0;
        ticket.setValue(value);
        assertEquals(value, ticket.getValue(), 0.001);

        ticket.setValue(null);
        assertNull(ticket.getValue());

        value = -50.0;
        ticket.setValue(value);
        assertEquals(value, ticket.getValue(), 0.001);
    }
}