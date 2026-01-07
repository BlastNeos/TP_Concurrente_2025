package petri.monitor;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPolicy implements Policy {
    @Override
    public int choose(List<Integer> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("Candidates vacío");
        }
        int idx = ThreadLocalRandom.current().nextInt(candidates.size());
        return candidates.get(idx);
    }
}