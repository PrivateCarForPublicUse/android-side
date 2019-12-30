package com.privatecarforpublic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecRouteModel {
    private SecRoute secRoute;
    private Settlement settlement;
}
