package leo.storage;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm");
    private final LocalDateTime dtOne = LocalDateTime.parse("19012023 12:00", formatter);
    private final LocalDateTime dtTwo = LocalDateTime.parse("19012023 16:00", formatter);
    private final LocalDateTime dtThree = LocalDateTime.parse("20012023 13:00", formatter);

    @Test
    public void testSameDateStringConversion() {
        Event event = new Event("meeting", dtOne, dtTwo);
        String expected = "[E][ ] meeting (Thu, Jan 19, 12:00 pm - 04:00 pm)";
        assertEquals(expected, event.toString());
    }

    @Test
    public void testDifferentDateStringConversion() {
        Event event = new Event("meeting", dtOne, dtThree);
        String expected = "[E][ ] meeting (Thu, Jan 19, 12:00 pm - Fri, Jan 20, 01:00 pm)";
        assertEquals(expected, event.toString());
    }

    @Test
    public void testSaveFormat() {
        String expectedOne = "[E][ ] meeting | 19012023 12:00 | 19012023 16:00\n";
        Event eventOne = new Event("meeting", dtOne, dtTwo);
        String expectedTwo = "[E][ ] meeting | 19012023 12:00 | 20012023 13:00\n";
        Event eventTwo = new Event("meeting", dtOne, dtThree);
        assertEquals(expectedOne, eventOne.saveFormat());
        assertEquals(expectedTwo, eventTwo.saveFormat());

    }
}
