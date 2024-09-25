package com.group6.du_an_tot_nghiep.Dto.QuanLy.DiaChi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiRequest {
    private int id;
    private String hoVaTen;
    private String soDienThoai;
    private int idTinh;
    private int idHuyen;
    private String idXa;
    private String diaChiCuThe;
    private Boolean macDinh;
}
