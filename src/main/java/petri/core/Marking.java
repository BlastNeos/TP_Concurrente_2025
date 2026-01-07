package petri.core;

import java.util.Arrays;

/**
 * Representa un MARCADO de la red de Petri.
 * Un marcado es el estado: cuántos tokens hay en cada plaza.
 *
 * Importante: es inmutable desde afuera (no se pueden modificar los tokens).
 */
public class Marking {

    // tokens[p] = cantidad de tokens en la plaza p
    private final int[] tokens;

    /**
     * Crea un marcado a partir de un vector de tokens.
     * Se hace copia defensiva para evitar modificaciones externas.
     */
    public Marking(int[] tokens) {
        this.tokens = Arrays.copyOf(tokens, tokens.length);
    }

    /**
     * Devuelve la cantidad de plazas (tamaño del marcado).
     */
    public int size() {
        return tokens.length;
    }

    /**
     * Devuelve los tokens de la plaza index.
     * No expone el arreglo completo.
     */
    public int get(int index){
        return tokens[index];
    }

    /**
     * Devuelve una copia del marcado completo.
     * Se usa para calcular el siguiente estado sin mutar este.
     */
    public int[] snapshot() {
        return Arrays.copyOf(tokens, tokens.length);
    }

    /**
     * Representación textual del marcado (útil para logs/debug).
     */
    @Override
    public String toString(){
        return Arrays.toString(tokens);
    }
}
