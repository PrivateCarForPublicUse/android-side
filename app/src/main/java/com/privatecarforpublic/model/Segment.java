package com.privatecarforpublic.model;

import com.amap.api.services.help.Tip;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Segment implements Serializable {
    private Tip departure;
    private Tip destination;
}
