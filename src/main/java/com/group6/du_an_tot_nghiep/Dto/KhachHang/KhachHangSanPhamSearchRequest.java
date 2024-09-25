package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangSanPhamSearchRequest {

    private String ten;
}
