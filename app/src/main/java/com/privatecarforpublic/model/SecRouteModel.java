package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SecRouteModel {
    private SecRoute secRoute;
    private Settlement settlement;
}
