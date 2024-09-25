package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet;

import com.group6.du_an_tot_nghiep.Entities.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class SanPhamChiTietFilterRequest {
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int mauSac;
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int kichCo;
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int thuongHieu;
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int danhMuc;
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int deGiay;
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int chatLieu;
    @NotNull(message = "Vui lòng chọn 1 giá trị để lọc")
    private int trangThai;
    @NotNull(message = "Vui lòng nhập giá")
    private BigDecimal startPrice;
    @NotNull(message = "Vui lòng nhập giá")
    private BigDecimal endPrice;

    public SanPhamChiTietFilterRequest () {
        this.mauSac = -1;
        this.kichCo = -1;
        this.thuongHieu = -1;
        this.danhMuc = -1;
        this.deGiay = -1;
        this.chatLieu = -1;
        this.trangThai = -1;
        this.startPrice = new BigDecimal(0);
        this.endPrice = new BigDecimal(10000000);
    }
}
