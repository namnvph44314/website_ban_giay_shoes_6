package com.group6.du_an_tot_nghiep.Dto.QuanLy.ThuongHieu;

import com.group6.du_an_tot_nghiep.Entities.ThuongHieu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThuongHieuResponse {

    private int id;
    private String tenThuongHieu;
    private Timestamp ngayTao;
    private int trangThai;

    public static ThuongHieuResponse convertToResponse(ThuongHieu thuongHieu)
    {
        ThuongHieuResponse response = new ModelMapper().map(thuongHieu, ThuongHieuResponse.class);
        return response;
    }

}
