package com.group6.du_an_tot_nghiep.Dto.KhachHang;

import com.group6.du_an_tot_nghiep.Entities.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ListSanPhamSideBarFilterRequest {
    private List<Integer> mauSacList;
    private List<Integer> kichThuocList;
    private List<Integer> thuongHieuList;
    private List<Integer> theLoaiList;
    private List<Integer> deGiayList;
    private List<Integer> chatLieuList;
    private BigDecimal startPrice;
    private BigDecimal endPrice;

    public ListSanPhamSideBarFilterRequest() {
        this.mauSacList = new ArrayList<>();
        this.kichThuocList = new ArrayList<>();
        this.thuongHieuList = new ArrayList<>();
        this.theLoaiList = new ArrayList<>();
        this.deGiayList = new ArrayList<>();
        this.chatLieuList = new ArrayList<>();
        this.startPrice = new BigDecimal(0);
        this.endPrice = new BigDecimal(10000000);
    }
}
