package com.group6.du_an_tot_nghiep.Dto.QuanLy.TheLoai;

import com.group6.du_an_tot_nghiep.Entities.TheLoai;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheLoaiResponse {

    private int id;
    private String tenTheLoai;
    private Timestamp ngayTao;
    private int trangThai;

    public static TheLoaiResponse convertToResponse(TheLoai theLoai)
    {
        TheLoaiResponse response = new ModelMapper().map(theLoai, TheLoaiResponse.class);
        return response;
    }

}
