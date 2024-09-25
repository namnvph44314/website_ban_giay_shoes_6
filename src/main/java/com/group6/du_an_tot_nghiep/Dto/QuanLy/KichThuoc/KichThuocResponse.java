package com.group6.du_an_tot_nghiep.Dto.QuanLy.KichThuoc;

import com.group6.du_an_tot_nghiep.Entities.KichThuoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KichThuocResponse {

    private int id;
    private Float tenKichThuoc;
    private Timestamp ngayTao;
    private int trangThai;

    public static KichThuocResponse convertToResponse(KichThuoc kichThuoc)
    {
        KichThuocResponse response = new ModelMapper().map(kichThuoc, KichThuocResponse.class);
        return response;
    }

}
