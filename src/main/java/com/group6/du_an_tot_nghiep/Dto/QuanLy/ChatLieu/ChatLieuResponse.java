package com.group6.du_an_tot_nghiep.Dto.QuanLy.ChatLieu;

import com.group6.du_an_tot_nghiep.Entities.ChatLieu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLieuResponse {

    private int id;
    private String tenChatLieu;
    private Timestamp ngayTao;
    private int trangThai;

    public static ChatLieuResponse convertToResponse(ChatLieu chatLieu)
    {
        ChatLieuResponse response = new ModelMapper().map(chatLieu, ChatLieuResponse.class);
        return response;
    }

}
