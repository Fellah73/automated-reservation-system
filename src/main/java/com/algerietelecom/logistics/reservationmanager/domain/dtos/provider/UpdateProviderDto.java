package com.algerietelecom.logistics.reservationmanager.domain.dtos.provider;

import com.algerietelecom.logistics.reservationmanager.domain.enumeration.Provider.ProviderType;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UpdateProviderDto {
    @Nullable
    private String name;

    private ProviderType type;

    @Nullable
    private String nif;

    @Nullable
    private String email;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String address;

    @Nullable
    private String contactPerson;

    @Nullable
    private String logoUrl;

    @Nullable
    private Boolean active;
}
