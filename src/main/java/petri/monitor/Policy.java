package petri.monitor;

import java.util.List;

public interface Policy {
    int choose(List<Integer> candidates);
}
