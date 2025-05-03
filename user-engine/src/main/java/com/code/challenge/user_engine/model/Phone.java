package com.code.challenge.user_engine.model;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    @Id
    @GeneratedValue
    private UUID id;

    private long number;
    private int cityCode;
    private String countryCode;
}
