package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteProductRequest {
    private Integer productId;

    private Integer billId;
}
