package com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan;

import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TkResponse {
    private String name;
    private String soDienThoai;
    private List<DiaChi> address;
}
