package com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia;

import com.group6.du_an_tot_nghiep.Entities.PhieuGiamGia;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VoucherResponse {

    private BigDecimal bestSale;

    private PhieuGiamGia bestVoucher;
}
