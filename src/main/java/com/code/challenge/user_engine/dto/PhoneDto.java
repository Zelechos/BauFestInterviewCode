package com.code.challenge.user_engine.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDto {
    private long number;
    private int cityCode;
    private String countryCode;
}
