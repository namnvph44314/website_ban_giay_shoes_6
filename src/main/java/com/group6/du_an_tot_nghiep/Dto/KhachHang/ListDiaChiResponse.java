package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListDiaChiResponse {
    private int id;
    private String hoVaTen;
    private String soDienThoai;
    private int idTinh;
    private int idHuyen;
    private String idXa;
    private String diaChiCuThe;
    private String tenTinh;
    private String tenHuyen;
    private String tenXa;
}
