package id.ac.tazkia.notifikasi.gmail.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String subject;
    private String to;
    private String body;
}
