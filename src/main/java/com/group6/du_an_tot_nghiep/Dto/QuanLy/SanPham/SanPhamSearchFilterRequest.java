package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamSearchFilterRequest {
    private String ten;
    @NotNull(message = "Vui lòng chọn trạng thái để tìm kiếm")
    private int trangThai;
}
