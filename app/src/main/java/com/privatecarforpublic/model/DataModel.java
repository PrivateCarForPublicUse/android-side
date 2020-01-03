package com.privatecarforpublic.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataModel {
    private Long RoutId;
    private String applyTime;
    private String reason;
    List<SecModel> secModels;
    private Double routelength;
    private Double cost;
    private int isReimburse;
}
