package com.group6.du_an_tot_nghiep.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaDto {
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Integer kieu;
    private Integer kieuGiaTri;
    private Integer trangThai;
}
