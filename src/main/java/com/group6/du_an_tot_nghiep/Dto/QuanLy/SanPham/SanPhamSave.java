package com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham;

import lombok.Data;

import java.util.List;

@Data
public class SanPhamSave {

    private List<SanPhamRequest> listSP;

    private List<String> listImgDelete;
}
