package am.s_mukhamedzhanov.sd.events;

import am.s_mukhamedzhanov.sd.clock.Clock;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class EventsStatistic {
    private final Map<String, List<Instant>> events = new HashMap<>();
    private final Clock clock;

    public EventsStatistic(Clock clock) {
        this.clock = clock;
    }

    void incEvent(String name) {
        events.computeIfAbsent(name, (key) -> new ArrayList<>()).add(clock.now());
    }

    double getEventStatisticByName(String name) {
        shrinkToHour(name);
        return events.getOrDefault(name, Collections.emptyList()).size() / 60d;
    }

    Map<String, Double> getAllEventStatistic() {
        return events.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                el -> getEventStatisticByName(el.getKey())
        ));
    }

    void printStatistic() {
        Map<String, Double> statistic = getAllEventStatistic();

        for (String name : statistic.keySet()) {
            System.out.printf("Event's rpm %s: %f\n", name, statistic.get(name));
        }
    }

    private void shrinkToHour(String name) {
        List<Instant> instants = events.get(name);
        if (instants == null) return;
        Instant hourAgo = clock.now().minus(1, ChronoUnit.HOURS);
        events.put(name, instants.stream().filter(el -> el.isAfter(hourAgo)).toList());
    }
}
