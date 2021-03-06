package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecRouteModel {
    private SecRoute secRoute;
    private Settlement settlement;
}
