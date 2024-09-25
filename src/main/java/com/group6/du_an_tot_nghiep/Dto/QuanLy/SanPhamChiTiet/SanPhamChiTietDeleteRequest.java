package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class SanPhamChiTietDeleteRequest {
    @NotEmpty(message = "Vui lòng chọn sản phẩm cần xóa")
    private List<Integer> listId;

    public SanPhamChiTietDeleteRequest() {
        this.listId = new ArrayList<>();
    }
}
