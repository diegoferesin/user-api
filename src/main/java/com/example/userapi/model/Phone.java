package com.example.userapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Phone {

    @NotBlank(message = "phone number is required")
    @Column(name = "phone_number")
    private String number;

    @NotBlank(message = "city code is required")
    @Column(name = "city_code")
    private String citycode;

    @NotBlank(message = "country code is required")
    @Column(name = "country_code")
    private String countrycode;
}