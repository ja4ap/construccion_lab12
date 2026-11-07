package com.tecsup.petclinic.util;

public final class CasoLog {

    private static final String LINEA = "=".repeat(86);
    private static final String GUION = "-".repeat(86);

    private CasoLog() {
    }

    public static void inicio(String caso, String prueba) {
        System.out.println();
        System.out.println(LINEA);
        System.out.println(" " + caso + " | " + prueba);
        System.out.println(GUION);
    }

    public static void seccion(String titulo) {
        System.out.println();
        System.out.println(GUION);
        System.out.println(" " + titulo);
        System.out.println(GUION);
    }

    public static void dato(String etiqueta, Object valor) {
        System.out.printf("   %-30s : %s%n", etiqueta, valor);
    }

    public static void verificacion(String etiqueta, Object esperado, Object obtenido) {
        String esperadoTexto = String.valueOf(esperado);
        String obtenidoTexto = String.valueOf(obtenido);
        String estado = esperadoTexto.equals(obtenidoTexto) ? "OK" : "ERROR";
        System.out.printf("   %-30s : esperado = %-24s obtenido = %-24s [%s]%n",
                etiqueta, esperadoTexto, obtenidoTexto, estado);
    }

    public static void ok(String mensaje) {
        System.out.println("   [OK] " + mensaje);
    }

    public static void fin() {
        System.out.println(GUION);
        System.out.println(" RESULTADO : PRUEBA CONFORME");
        System.out.println(LINEA);
        System.out.println();
    }
}
