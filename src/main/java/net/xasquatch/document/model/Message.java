package net.xasquatch.document.model;

import lombok.Data;
import net.xasquatch.document.model.enumulation.MessageType;

@Data
public class Message {
    private long no;
    private long chatting_room_no;
    private String mbr_no;
    private String mbr_nick_name;
    private String contents;
    private MessageType messageType;
    private String ip_address;
    private String date;
}
