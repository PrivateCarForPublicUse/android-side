package com.privatecarforpublic.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RouteModel implements Serializable {
    private User user;
    private Car car;
    private Route route;
    private List<SecRouteModel> secRoutes;
}
