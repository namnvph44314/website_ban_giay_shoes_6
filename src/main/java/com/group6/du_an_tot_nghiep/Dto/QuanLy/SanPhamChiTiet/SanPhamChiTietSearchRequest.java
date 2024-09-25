package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietSearchRequest {
    @NotBlank(message = "Vui lòng nhập mã sản phẩm chi tiết")
    private String ma;
}
