package petri.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PriorityPolicy implements Policy {
    private final Set<Integer> highPriority;

    public PriorityPolicy(Set<Integer> highPriority) {
        this.highPriority = highPriority;
    }

    @Override
    public int choose(List<Integer> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("Candidates vacío");
        }

        // filtro los candidatos que sean “high priority”
        List<Integer> preferred = new ArrayList<>();
        for (int t : candidates) {
            if (highPriority.contains(t)) preferred.add(t);
        }

        List<Integer> pickFrom = preferred.isEmpty() ? candidates : preferred;

        int idx = ThreadLocalRandom.current().nextInt(pickFrom.size());
        return pickFrom.get(idx);
    }
}