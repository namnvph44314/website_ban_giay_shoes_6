package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import com.group6.du_an_tot_nghiep.Entities.SanPham;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamResponse {
    private int id;
    private String tenSanPham;
    private Timestamp ngayTao;
    private int trangThai;
    private int soLuong;

    public static SanPhamResponse convertToResponse(SanPham sanPham) {
        SanPhamResponse response = new ModelMapper().map(sanPham,SanPhamResponse.class);
        return response;
    }
}
