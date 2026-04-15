package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider.main;

import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;

import lombok.Data;

@Data
public class CreateProviderDto {
    private String name;
    private ProviderType type;
    private String nif;
    private String email;
    private String phoneNumber;
    private String address;
    private String contactPerson;
    private String logoUrl;
    private Boolean active;
}
