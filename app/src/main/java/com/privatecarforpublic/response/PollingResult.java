package com.privatecarforpublic.response;

import com.privatecarforpublic.model.RouteModel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PollingResult implements Serializable {
    private static final long serialVersionUID = 4832771715671880043L;
    private Integer code;
    private String message;
    private RouteModel data;
}