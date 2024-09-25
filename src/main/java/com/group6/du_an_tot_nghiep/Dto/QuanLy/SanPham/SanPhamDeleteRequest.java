package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDeleteRequest {
    @NotEmpty(message = "Vui lòng chọn sản phẩm cần xóa")
    private List<Integer> listId;
}
