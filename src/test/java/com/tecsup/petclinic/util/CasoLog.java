package com.tecsup.petclinic.util;

public final class CasoLog {

    private CasoLog() {
    }

    public static void caso(String caso, String tabla) {
        System.out.println();
        System.out.println("=== " + caso + " | tabla: " + tabla + " ===");
    }

    public static void fila(int numero, String prueba, String datos, String resultado) {
        System.out.printf(" %d  %-22s %-26s -> %s [OK]%n", numero, prueba, datos, resultado);
    }
}
