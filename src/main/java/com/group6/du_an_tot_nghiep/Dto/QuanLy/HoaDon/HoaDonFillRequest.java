package com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Getter
@Setter
public class HoaDonFillRequest {
    private String search;
    private String hoVaTen;
    private String soDienThoaiNhan;

    private Integer loaiDonHang;
    private Integer trangThai;

    @JsonProperty("ngaybatdau")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ngaybatdau;
    @JsonProperty("ngayketthuc")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ngayketthuc;

    public Timestamp ngayTao(){
        return ngayTao();
    }

    private int  page;
    private int size;

    private String sort;
}
