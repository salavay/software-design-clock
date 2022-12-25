package am.s_mukhamedzhanov.sd.events;

import am.s_mukhamedzhanov.sd.clock.SettableClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventsStatisticTest {

    SettableClock clock;
    EventsStatistic eventsStatistic;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));

        clock = new SettableClock(Instant.now());
        eventsStatistic = new EventsStatistic(clock);
    }

    @Test
    void incEvent() {
        eventsStatistic.incEvent("event1");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        assertEquals(1, statistic.size());
        assertTrue(statistic.containsKey("event1"));
        assertEquals(2 / 60d, statistic.get("event1"));
    }

    @Test
    void getEventStatisticByName() {
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        eventsStatistic.incEvent("event3");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");

        double result = eventsStatistic.getEventStatisticByName("event1");
        assertEquals(3 / 60d, result);

    }

    @Test
    void getAllEventStatistic() {
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        eventsStatistic.incEvent("event3");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");

        var expected = Map.of(
                "event1", 3 / 60d,
                "event2", 2 / 60d,
                "event3", 1 / 60d
        );

        assertEquals(expected, eventsStatistic.getAllEventStatistic());
    }

    @Test
    void printStatistic() {
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        eventsStatistic.incEvent("event3");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");
        eventsStatistic.incEvent("event2");
        clock.plusTime(1, ChronoUnit.MINUTES);
        eventsStatistic.incEvent("event1");

        eventsStatistic.printStatistic();

        assertEquals("""
                Event's rpm event1: 0,050000
                Event's rpm event3: 0,016667
                Event's rpm event2: 0,033333
                """, outputStreamCaptor.toString());
    }
}