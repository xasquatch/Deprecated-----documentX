package net.xasquatch.document.model;

import lombok.Data;

@Data
public class History {

    private long no;
    private long mbr_no;
    private String destination;
    private String ip_address;
    private String date;
}
