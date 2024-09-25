package com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiamGiaSanPhamFilter {
    private String ngayBatDau;

    private String ngayKetThuc;

    @NotNull(message = "Vui lòng chọn trạng thái")
    private Integer trangThai;
}
