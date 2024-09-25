package com.group6.du_an_tot_nghiep.Dto.QuanLy.DeGiay;

import com.group6.du_an_tot_nghiep.Entities.DeGiay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeGiayResponse {

    private int id;
    private String tenDeGiay;
    private Timestamp ngayTao;
    private int trangThai;

    public static DeGiayResponse convertToResponse(DeGiay deGiay)
    {
        DeGiayResponse response = new ModelMapper().map(deGiay, DeGiayResponse.class);
        return response;
    }

}
