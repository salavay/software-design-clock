package am.s_mukhamedzhanov.sd.clock;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

public class SettableClock implements Clock {

    private Instant instant;

    public SettableClock(Instant instant) {
        this.instant = instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public void plusTime(long amount, TemporalUnit temporalUnit) {
        this.instant = this.instant.plus(amount, temporalUnit);
    }

    @Override
    public Instant now() {
        return instant;
    }
}
