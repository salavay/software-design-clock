package am.s_mukhamedzhanov.sd.clock;

import java.time.Instant;

public class NormalClock implements Clock {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
