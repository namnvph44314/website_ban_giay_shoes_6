package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToBillRequest {

    private Integer amountProduct;

    private Integer billId;

    private Integer productId;

}
