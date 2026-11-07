package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de transferencia para Owner.
 *
 * @author Jose Paredes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
}
