package petri.app;

import petri.monitor.MonitorInterface;

/**
 * Worker representa un hilo de ejecución.
 * Cada worker intenta disparar un conjunto fijo de transiciones
 * usando el monitor como único punto de acceso a la red.
 */
public class Worker implements Runnable {

    // Conjunto de transiciones que este worker tiene permitido intentar disparar
    private final int[] transitions;

    // Monitor compartido que controla concurrencia, tiempos y estado
    private final MonitorInterface monitor;

    public Worker(int[] transitions, MonitorInterface monitor) {
        this.transitions = transitions;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            for (int t : transitions) {
                if (!monitor.fireTransition(t)) return; // acá sale
            }
        }
    }
}

