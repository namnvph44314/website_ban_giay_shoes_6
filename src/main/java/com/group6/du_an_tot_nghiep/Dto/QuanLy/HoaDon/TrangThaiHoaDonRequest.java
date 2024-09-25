package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TrangThaiHoaDonRequest {

    private Integer newTrangThai;

    @NotBlank(message = "Không được để trống ghi chú")
    private String ghiChu;
    private Integer idTaiKhoan;
}
