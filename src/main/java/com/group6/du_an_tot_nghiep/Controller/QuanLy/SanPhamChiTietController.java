package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietDeleteRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietFilterRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietSearchRequest;
import com.group6.du_an_tot_nghiep.Entities.SanPhamChiTiet;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietUpdateRequest;
import com.group6.du_an_tot_nghiep.Entities.*;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamChiTietService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/san-pham-chi-tiet")
public class SanPhamChiTietController {
    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    SanPhamChiTietSearchRequest _sanPhamChiTietSearchRequest;

    @Autowired
    SanPhamChiTietFilterRequest _sanPhamFilterRequest;

    Page<SanPhamChiTiet> _pageData;
    int _idSanPham;
    int _deleteResult = 0;


    @GetMapping("/index/{id}")
    public String detail(@PathVariable("id") int id) {
        _idSanPham = id;
        return "/quan-ly/san-pham/index-san-pham-chi-tiet";
    }

    @GetMapping("/get-id")
    @ResponseBody
    public Integer getIdProduct(){
        return _idSanPham;
    }

    @GetMapping("/chat-lieu")
    @ResponseBody
    public List<ChatLieu> getListChatLieu(){
        return sanPhamChiTietService.findAllChatLieu();
    }

    @GetMapping("/de-giay")
    @ResponseBody
    public List<DeGiay> getListDeGiay(){
        return sanPhamChiTietService.findAllDeGiay();
    }

    @GetMapping("/kich-thuoc")
    @ResponseBody
    public List<KichThuoc> getListKichThuoc(){
        return sanPhamChiTietService.findAllKichThuoc();
    }

    @GetMapping("/mau-sac")
    @ResponseBody
    public List<MauSac> getListMauSac(){
        return sanPhamChiTietService.findAllMauSac();
    }

    @GetMapping("/danh-muc")
    @ResponseBody
    public List<TheLoai> getListDanhMuc(){
        return sanPhamChiTietService.findAllDanhMuc();
    }

    @GetMapping("/thuong-hieu")
    @ResponseBody
    public List<ThuongHieu> getListThuongHieu(){
        return sanPhamChiTietService.findAllThuongHieu();
    }

    @GetMapping("/hinh-anh/{id}")
    @ResponseBody
    public List<String> getListHinhAnh(@PathVariable("id") Integer productId){
        return sanPhamChiTietService.getListHinhAnh(productId);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public SanPhamChiTiet getDetailProduct(@PathVariable("id") Integer productId){
        return sanPhamChiTietService.findById(productId);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public Integer updateDetailProduct(@PathVariable("id") Integer productId, @RequestBody SanPhamChiTietUpdateRequest request){
        request.setId(productId);
        return sanPhamChiTietService.updateDetailProduct(request);
    }

    @PostMapping("/update-image/{id}")
    @ResponseBody
    public void updateImageProduct(@PathVariable("id") Integer productId, @RequestBody List<String> listHinhAnh){
        sanPhamChiTietService.updateHinhAnh(productId, listHinhAnh);
    }


    @GetMapping("/get-list-spct/{id}")
    @ResponseBody
    public Page<SanPhamChiTiet> getListSPCT(@PathVariable("id") Integer productId, @RequestParam("page") Optional<Integer> pageNumber){
        return sanPhamChiTietService.findAllSanPhamChiTiet(productId, pageNumber.orElse(0));
    }

    @GetMapping("/get-search-request")
    @ResponseBody
    public SanPhamChiTietSearchRequest getSerchRequest(){
        return new SanPhamChiTietSearchRequest();
    }

    @PostMapping("/search")
    @ResponseBody
    public Page<SanPhamChiTiet> searchByMa(@RequestBody SanPhamChiTietSearchRequest request){
        return sanPhamChiTietService.findSanPhamChiTietByMa(request.getMa());
    }

    @GetMapping("/get-delete-request")
    @ResponseBody
    public SanPhamChiTietDeleteRequest getDeteleRequest(){
        return new SanPhamChiTietDeleteRequest();
    }

    @PostMapping("/delete")
    @ResponseBody
    public Integer getDeteleRequest(@RequestBody SanPhamChiTietDeleteRequest request){
        return sanPhamChiTietService.delete(request.getListId());
    }

    @GetMapping("/get-filter-request")
    @ResponseBody
    public SanPhamChiTietFilterRequest getFilterRequest(){
        return new SanPhamChiTietFilterRequest();
    }

    @PostMapping("/filter")
    @ResponseBody
    public Page<SanPhamChiTiet> filter(@RequestBody SanPhamChiTietFilterRequest request, @RequestParam("page") Optional<Integer> pageNumber){
        return sanPhamChiTietService.filter(request,_idSanPham,pageNumber.orElse(0));
    }

    public void addAttributeThuocTinh (Model model) {
        model.addAttribute("lstChatLieu", sanPhamChiTietService.findAllChatLieu());
        model.addAttribute("lstMauSac", sanPhamChiTietService.findAllMauSac());
        model.addAttribute("lstKichCo", sanPhamChiTietService.findAllKichThuoc());
        model.addAttribute("lstThuongHieu", sanPhamChiTietService.findAllThuongHieu());
        model.addAttribute("lstDanhMuc", sanPhamChiTietService.findAllDanhMuc());
        model.addAttribute("lstDeGiay", sanPhamChiTietService.findAllDeGiay());

    }
}
