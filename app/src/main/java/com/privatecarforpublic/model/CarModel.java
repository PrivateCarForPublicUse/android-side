package com.privatecarforpublic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CarModel implements Serializable {
    private User user;
    private Car car;
}
