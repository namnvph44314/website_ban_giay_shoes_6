package com.group6.du_an_tot_nghiep.Dto.QuanLy.GiamGiaSanPhamChiTiet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiamGiaSanPhamChiTietAdd {
    private List<Integer> ggspct;

    @Override
    public String toString() {
        return "GiamGiaSanPhamChiTietAdd{" +
                "ggspct=" + ggspct +
                '}';
    }
}
