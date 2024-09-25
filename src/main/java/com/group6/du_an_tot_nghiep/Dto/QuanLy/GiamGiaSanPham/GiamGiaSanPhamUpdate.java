package com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPham;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiamGiaSanPhamUpdate {
    @Size(min = 3, max = 30, message = "Vui lòng nhập tên đợt giảm giá từ 3 đến 30 ký tự")
    private String ten;
    @NotNull(message = "Vui lòng nhập giá trị")
    @Min( value = 0, message = "Giá trị phải trong khoảng từ 0-100")
    @Max( value = 100, message = "Giá trị phải trong khoảng từ 0-100")
    private Integer giaTri;
    @NotBlank(message = "Vui lòng nhập ngày bắt đầu")
    private String ngayBatDau;
    @NotBlank(message = "Vui lòng nhập ngày kết thúc")
    private String ngayKetThuc;

    @Override
    public String toString() {
        return "GiamGiaSanPhamUpdate{" +
                "ten='" + ten + '\'' +
                ", giaTri=" + giaTri +
                ", ngayBatDau='" + ngayBatDau + '\'' +
                ", ngayKetThuc='" + ngayKetThuc + '\'' +
                '}';
    }
}
