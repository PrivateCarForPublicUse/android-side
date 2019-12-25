package com.privatecarforpublic.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApplyCarDTO implements Serializable {
    private String startTime;
    private String endTime;
    private String reason;
    private List<String> names;
    private List<PointLatDTO> lats;
    private Long carId;
}
