package com.drawupdateprobabilities.domain;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class ProbabilityType {
    private Short id;
    private String code;
    private String description;
}
