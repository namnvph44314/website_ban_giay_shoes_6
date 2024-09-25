package com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGiaKhachHang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhieuGiamGiaKhachHangAdd {
    private List<Integer> pggKh;

    @Override
    public String toString() {
        return "PhieuGiamGiaKhachHangAdd{" +
                "pggKh=" + pggKh +
                '}';
    }
}
