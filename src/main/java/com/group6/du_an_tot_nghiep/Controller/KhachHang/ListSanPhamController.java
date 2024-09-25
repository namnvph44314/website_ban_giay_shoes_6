package com.group6.du_an_tot_nghiep.Controller.KhachHang;

import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamHeadFilterRequest;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListSanPhamSideBarFilterRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPhamChiTiet.SanPhamChiTietFilterRequest;
import com.group6.du_an_tot_nghiep.Entities.GioHang;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.KhachHang.GioHangService;
import com.group6.du_an_tot_nghiep.Service.KhachHang.ListSanPhamService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ListSanPhamController {
    @Autowired
    ListSanPhamService _listSanPhamService;

    @Autowired
    SanPhamService sanPhamService;

    ListSanPhamSideBarFilterRequest _sideBarFilterRequest = new ListSanPhamSideBarFilterRequest();
    ListSanPhamHeadFilterRequest _headFilterRequest = new ListSanPhamHeadFilterRequest(0);
    KhachHangSanPhamSearchRequest _searchRequest = new KhachHangSanPhamSearchRequest();
    PagedListHolder<ListSanPhamResponse> _pageData;

    @Autowired
    TaiKhoanService _taiKhoanService;
    @Autowired
    HttpSession session;
    TaiKhoan _taiKhoan;
    GioHang _gioHang;

    @GetMapping("/list-san-pham/index")
    public String index (Model model, @RequestParam Optional<Integer> pageNumber) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        if (_taiKhoan != null){
            _gioHang = _listSanPhamService.findGioHangByTaiKhoan(_taiKhoan);
            model.addAttribute("soSpTrongGh", _listSanPhamService.getSoLuongSpTrongGh(_gioHang));
        }
        _pageData = _listSanPhamService.findAll(pageNumber.orElse(0),_headFilterRequest.getKieuLoc());

        model.addAttribute("taiKhoan", _taiKhoan);
        model.addAttribute("pageData", _pageData);
        model.addAttribute("headFilterRequest",_headFilterRequest);
        addAttributeSideBarFilterRequest(model);
        model.addAttribute("sideBarFilterRequest",_sideBarFilterRequest);
        model.addAttribute("searchRequest",_searchRequest);
        return "/khach_hang/list_san_pham";
    }


    @PostMapping("/api/list-san-pham/sidebar-filter")
    public String sideBarFilter (Model model,@ModelAttribute("sideBarFilterRequest") ListSanPhamSideBarFilterRequest request) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        model.addAttribute("taiKhoan", _taiKhoan);
        _pageData = _listSanPhamService.sideBarFilter(0,request, _headFilterRequest.getKieuLoc());
        if (_pageData.getPageList().isEmpty()) {
            _pageData = null;
        }
        _searchRequest = new KhachHangSanPhamSearchRequest();
        model.addAttribute("soSpTrongGh", _listSanPhamService.getSoLuongSpTrongGh(_gioHang));
        model.addAttribute("pageData", _pageData);
        model.addAttribute("headFilterRequest",_headFilterRequest);
        model.addAttribute("searchRequest",_searchRequest);
        addAttributeSideBarFilterRequest(model);
        _sideBarFilterRequest = request;
        return "/khach_hang/list_san_pham";
    }

    @GetMapping("/list-san-pham/find-by-danh-muc/{idDanhMuc}")
    public String findByDanhMuc (@PathVariable int idDanhMuc, Model model,@ModelAttribute("sideBarFilterRequest") ListSanPhamSideBarFilterRequest request) {
        request.getTheLoaiList().add(idDanhMuc);
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        _gioHang = _listSanPhamService.findGioHangByTaiKhoan(_taiKhoan);
        model.addAttribute("taiKhoan", _taiKhoan);
        _pageData = _listSanPhamService.findByDanhMuc(0,request);
        if (_pageData.getPageList().isEmpty()) {
            _pageData = null;
        }
        _searchRequest = new KhachHangSanPhamSearchRequest();
        model.addAttribute("soSpTrongGh", _listSanPhamService.getSoLuongSpTrongGh(_gioHang));
        model.addAttribute("pageData", _pageData);
        model.addAttribute("headFilterRequest",new ListSanPhamHeadFilterRequest(0));
        model.addAttribute("searchRequest",_searchRequest);
        addAttributeSideBarFilterRequest(model);
        _sideBarFilterRequest = request;
        return "/khach_hang/list_san_pham";
    }

    @PostMapping("/api/list-san-pham/head-filter")
    public String headFilter (Model model, @ModelAttribute("headFilterRequest") ListSanPhamHeadFilterRequest request) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        model.addAttribute("taiKhoan", _taiKhoan);
        _pageData =_listSanPhamService.headFilter(request.getKieuLoc(),_sideBarFilterRequest,_searchRequest);
        if (_pageData.getPageList().isEmpty()) {
            _pageData = null;
        }
        model.addAttribute("pageData", _pageData);
        model.addAttribute("soSpTrongGh", _listSanPhamService.getSoLuongSpTrongGh(_gioHang));
        model.addAttribute("sideBarFilterRequest",_sideBarFilterRequest);
        model.addAttribute("searchRequest",_searchRequest);
        addAttributeSideBarFilterRequest(model);
        _headFilterRequest = request;
        return "/khach_hang/list_san_pham";
    }

    @PostMapping("/api/list-san-pham/search")
    public String search (Model model, @ModelAttribute("searchRequest") KhachHangSanPhamSearchRequest request) {
        _taiKhoan = (TaiKhoan) session.getAttribute("username");
        if (_taiKhoan != null){
            _gioHang = _listSanPhamService.findGioHangByTaiKhoan(_taiKhoan);
            model.addAttribute("soSpTrongGh", _listSanPhamService.getSoLuongSpTrongGh(_gioHang));
        }
        model.addAttribute("taiKhoan", _taiKhoan);
        _pageData = _listSanPhamService.search(0,request, _headFilterRequest.getKieuLoc());
        if (_pageData.getPageList().isEmpty()) {
            _pageData = null;
        }
        _sideBarFilterRequest = new ListSanPhamSideBarFilterRequest();
        model.addAttribute("pageData", _pageData);
        model.addAttribute("headFilterRequest",_headFilterRequest);
        model.addAttribute("sideBarFilterRequest",_sideBarFilterRequest);
        addAttributeSideBarFilterRequest(model);
        _searchRequest = request;
        return "/khach_hang/list_san_pham";
    }

    public void addAttributeSideBarFilterRequest (Model model) {
        model.addAttribute("mauSacList", _listSanPhamService.findAllMauSac());
        model.addAttribute("kichThuocList", _listSanPhamService.findAllKichThuoc());
        model.addAttribute("thuongHieuList", _listSanPhamService.findAllThuongHieu());
        model.addAttribute("theLoaiList", _listSanPhamService.findAllDanhMuc());
        model.addAttribute("deGiayList", _listSanPhamService.findAllDeGiay());
        model.addAttribute("chatLieuList", _listSanPhamService.findAllChatLieu());
    }
}
