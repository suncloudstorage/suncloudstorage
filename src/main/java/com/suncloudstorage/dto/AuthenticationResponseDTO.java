package com.suncloudstorage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDTO {
    private String accessToken;
}
