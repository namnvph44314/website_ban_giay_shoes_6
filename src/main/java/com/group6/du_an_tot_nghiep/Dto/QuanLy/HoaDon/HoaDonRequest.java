package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class HoaDonRequest {
    private Integer idHoaDon;
    private Integer soLuong;
    private Integer idHoaDonChiTiet;
}
