package com.group6.du_an_tot_nghiep.Dto.QuanLy.ThongKe;

import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeSanPhamBanChay {
    
    private int idSanPham;
    private String tenSanPham;
    private Integer soLuong;
    private BigDecimal giaTien;

    @Override
    public String toString() {
        return "ThongKeSanPhamBanChay{" +
                "idSanPham=" + idSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", giaTien=" + giaTien +
                '}';
    }
}
