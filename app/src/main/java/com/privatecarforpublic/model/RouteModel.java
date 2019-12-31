package com.privatecarforpublic.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date:2019/12/26
 * @author:zhongcz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteModel implements Serializable {
    private User user;
    private Car car;
    private Route route;
    private List<SecRouteModel> secRoutesModel;
}
