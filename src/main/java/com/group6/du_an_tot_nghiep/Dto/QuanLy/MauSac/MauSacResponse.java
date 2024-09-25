package com.group6.du_an_tot_nghiep.Dto.QuanLy.MauSac;

import com.group6.du_an_tot_nghiep.Entities.MauSac;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MauSacResponse {

    private int id;
    private String tenMauSac;
    private String maMauSac;
    private Timestamp ngayTao;
    private int trangThai;

    public static MauSacResponse convertToResponse(MauSac mauSac)
    {
        MauSacResponse response = new ModelMapper().map(mauSac, MauSacResponse.class);
        return response;
    }

}
