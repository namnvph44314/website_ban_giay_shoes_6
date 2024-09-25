package com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaRequest {
    @NotBlank(message = "Vui lòng nhập mã khuyến mãi")
    @Size(min = 3, max = 10, message = "Vui lòng nhập mã khuyến mãi từ 3 đến 10 ký tự")
    private String maKhuyenMai;
    @NotBlank(message = "Vui lòng nhập tên khuyến mãi")
    @Size(min = 3, max = 30, message = "Vui lòng nhập tên khuyến mãi từ 3 đến 30 ký tự")
    private String ten;
    @NotNull(message = "Vui lòng nhập giá trị tối đa")
    private BigDecimal giaTriToiDa;
    @NotNull(message = "Vui lòng nhập điều kiện")
    private BigDecimal giaTriNhoNhat;
    @NotNull(message = "Vui lòng nhập số lượng")
    private Integer soLuong;
    @NotNull(message = "Vui lòng chọn kiểu")
    private Integer kieu;
    @NotNull(message = "Vui lòng chọn kiểu giá trị")
    private Integer kieuGiaTri;
    @NotNull(message = "Vui lòng nhập giá trị")
    private BigDecimal giaTri;
    @NotNull(message = "Vui lòng nhập ngày bắt đầu")
    private LocalDateTime ngayBatDau;
    @NotNull(message = "Vui lòng nhập ngày kết thúc")
    private LocalDateTime ngayKetThuc;
}
