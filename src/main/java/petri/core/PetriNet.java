package petri.core;

import java.util.Arrays;
/**
 * Modelo estructural de una Red de Petri ordinaria (P/T):
 * - pre: lo que consume cada transición
 * - post: lo que produce cada transición
 * - delayMs: semántica temporal (la usa el monitor, no esta clase)
 *
 * Nota: esta clase NO maneja concurrencia; solo define la lógica/ecuación de estado.
 */
public class PetriNet {
    private final int places;       // cantidad de plazas |P|
    private final int transitions;  // cantidad de transiciones |T|

    // pre[p][t] = tokens que consume la transición t desde la plaza p
    private final int[][] pre;

    // post[p][t] = tokens que produce la transición t hacia la plaza p
    private final int[][] post;

    // delayMs[t] = espera mínima en ms para poder disparar (0 = no temporizada)
    private final long[] delayMs;

    public PetriNet(int[][] pre, int[][] post, long[] delayMs) {
        // Dimensiones asumidas: pre es matriz |P| x |T|
        this.places = pre.length;
        this.transitions = pre[0].length;

        // Copias defensivas: evitamos que nos muten matrices desde afuera
        this.pre = deepCopy(pre);
        this.post = deepCopy(post);
        this.delayMs = Arrays.copyOf(delayMs, delayMs.length);

        // Validación de coherencia estructural (pre y post deben tener mismas dims)
        if (post.length != places || post[0].length != transitions) {
            throw new IllegalArgumentException("Dimensiones incompatibles en matrices pre/post");
        }
        // delayMs debe tener un delay por transición
        if (this.delayMs.length != transitions) {
            throw new IllegalArgumentException("delayMs debe tener tamaño igual a transitions");
        }
    }

    public int places() {
        return places; // getter simple
    }

    public int transitions() {
        return transitions; // getter simple
    }

    public long delayMs(int t) {
        return delayMs[t]; // delay configurado para Tt
    }

    /**
     * Chequeo SOLO por tokens (no incluye tiempo).
     * Una transición está habilitada si en cada plaza hay tokens suficientes para consumir pre[p][t].
     */
    public boolean isEnabledByTokens(Marking m, int t) {
        for (int p = 0; p < places; p++) {
            // si a la plaza p le faltan tokens para cubrir lo que consume Tt, no está habilitada
            if (m.get(p) < pre[p][t]) return false;
        }
        return true;
    }

    /**
     * Disparo "puro": calcula el siguiente marcado aplicando: * M' = M - pre[:,t] + post[:,t]
     * Ojo: esto no sincroniza ni duerme por tiempo; eso lo hace el monitor.
     */
    public Marking fire(Marking m, int t) {
        // Seguridad: no disparamos si estructuralmente no corresponde
        if (!isEnabledByTokens(m, t)) {
            throw new IllegalStateException("T" + t + " no está habilitada por tokens");
        }

        // Trabajamos con copia del vector de marcado para no mutar el Marking original
        int[] next = m.snapshot();

        for (int p = 0; p < places; p++) {
            // Ecuación de estado por componente (plaza):
            // tokens nuevos = tokens actuales - consumidos + producidos
            next[p] = next[p] - pre[p][t] + post[p][t];

            // Sanity check: en una red ordinaria bien formada no debería quedar negativo
            if (next[p] < 0) {
                throw new IllegalStateException("Token negativo en P" + p + " luego de disparar T" + t);
            }
        }
        return new Marking(next); // devolvemos el nuevo estado como objeto inmutable
    }

    /**
     * Copia profunda de matriz 2D.
     * (Arrays.copyOf solo copia la fila, no la matriz completa si no iterás filas)
     */
    private static int[][] deepCopy(int[][] a) {
        int[][] c = new int[a.length][];
        for (int i = 0; i < a.length; i++) {
            c[i] = Arrays.copyOf(a[i], a[i].length);
        }
        return c;
    }
}







