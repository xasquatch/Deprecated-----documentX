package net.xasquatch.document.model;

import lombok.Data;

import java.util.Date;

@Data
public class Authorization {

    private long no;
    private String name;
    private String mbr_no;
    private String mbr_email;
    private Date created_date;

}
