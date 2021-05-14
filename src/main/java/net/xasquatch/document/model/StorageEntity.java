package net.xasquatch.document.model;

import lombok.Data;
import net.xasquatch.document.model.enumulation.DataType;

import java.util.Date;

@Data
public class StorageEntity {

    private long no;
    private long mbr_no;
    private String mbr_nick_name;
    private DataType dataType;
    private String url;
    private Date date;

}
