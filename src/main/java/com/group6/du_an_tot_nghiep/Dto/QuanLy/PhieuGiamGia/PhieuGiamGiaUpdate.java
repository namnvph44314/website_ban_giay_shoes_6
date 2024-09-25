package com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaUpdate {
    private String maKhuyenMai;
    private String ten;
    private BigDecimal giaTriToiDa;
    private BigDecimal giaTriNhoNhat;
    private Integer soLuong;
    private Integer kieu;
    private Integer kieuGiaTri;
    private BigDecimal giaTri;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;

}
