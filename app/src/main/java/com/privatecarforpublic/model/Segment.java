package com.privatecarforpublic.model;

import com.amap.api.services.help.Tip;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Segment {
    private Tip departure;
    private Tip destination;
}
