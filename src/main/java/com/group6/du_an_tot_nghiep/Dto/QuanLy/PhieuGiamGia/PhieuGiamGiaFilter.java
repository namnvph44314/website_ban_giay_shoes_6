package com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaFilter {

    private String ngayBatDau;

    private String ngayKetThuc;
    @NotNull(message = "Vui lòng chọn kiểu")
    private Integer kieu;
    @NotNull(message = "Vui lòng chọn kiểu giá trị")
    private Integer kieuGiaTri;
    @NotNull(message = "Vui lòng chọn trạng thái")
    private Integer trangThai;

    @Override
    public String toString() {
        return "PhieuGiamGiaFilter{" +
                "ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", kieu=" + kieu +
                ", kieuGiaTri=" + kieuGiaTri +
                ", trangThai=" + trangThai +
                '}';
    }
}
