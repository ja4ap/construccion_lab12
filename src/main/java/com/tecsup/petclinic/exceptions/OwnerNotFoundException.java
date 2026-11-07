package com.tecsup.petclinic.exceptions;

/**
 * Excepción lanzada cuando un propietario no existe en la base de datos.
 *
 * @author Jose Paredes
 */
public class OwnerNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public OwnerNotFoundException(String message) {
        super(message);
    }
}
