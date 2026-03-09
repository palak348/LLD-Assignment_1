import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

/**
 * Demo that shows immutability in action.
 *
 * - Tickets are built via Builder (no setters exist).
 * - Service "updates" return NEW ticket instances.
 * - External tag mutation has no effect on the ticket.
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        // 1. Create a ticket via service
        IncidentTicket t1 = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created: " + t1);

        // 2. "Update" by assigning — returns a NEW ticket
        IncidentTicket t2 = service.assign(t1, "agent@example.com");
        System.out.println("\nAfter assign (new ticket): " + t2);
        System.out.println("Original unchanged:        " + t1);

        // 3. Escalate — returns yet another NEW ticket
        IncidentTicket t3 = service.escalateToCritical(t2);
        System.out.println("\nAfter escalation (new ticket): " + t3);
        System.out.println("Previous unchanged:            " + t2);

        // 4. Attempt external tag mutation — has NO effect
        List<String> tags = t3.getTags();
        try {
            tags.add("HACKED_FROM_OUTSIDE");
            System.out.println("\nERROR: external mutation succeeded (should not happen)");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nExternal tag mutation blocked (UnsupportedOperationException) — immutability works!");
        }
        System.out.println("Ticket after attempted mutation: " + t3);
    }
}
