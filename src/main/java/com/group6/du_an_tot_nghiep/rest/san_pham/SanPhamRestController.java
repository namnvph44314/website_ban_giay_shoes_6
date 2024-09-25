package com.group6.du_an_tot_nghiep.rest.san_pham;

import com.group6.du_an_tot_nghiep.Dto.QuanLy.SanPham.SanPhamSaveRequest;
import com.group6.du_an_tot_nghiep.Entities.*;
import com.group6.du_an_tot_nghiep.Service.QuanLy.KichThuocService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.MauSacService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamChiTietService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/admin/san-pham")
public class SanPhamRestController {

    @Autowired
    MauSacService mauSacService;

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    KichThuocService kichThuocService;

    @Autowired
    SanPhamService sanPhamService;

    @GetMapping("/mau-sac")
    public ResponseEntity<List<MauSac>> getListMauSac(){
        return ResponseEntity.ok(mauSacService.findAllByTrangThai());
    }

    @GetMapping("/danh-muc")
    public ResponseEntity<List<TheLoai>> getListTheLoai(){
        return ResponseEntity.ok(sanPhamChiTietService.findAllDanhMuc());
    }

    @GetMapping("/thuong-hieu")
    public ResponseEntity<List<ThuongHieu>> getListThuongHieu(){
        return ResponseEntity.ok(sanPhamChiTietService.findAllThuongHieu());
    }

    @GetMapping("/de-giay")
    public ResponseEntity<List<DeGiay>> getListDeGiay(){
        return ResponseEntity.ok(sanPhamChiTietService.findAllDeGiay());
    }

    @GetMapping("/chat-lieu")
    public ResponseEntity<List<ChatLieu>> getListChatLieu(){
        return ResponseEntity.ok(sanPhamChiTietService.findAllChatLieu());
    }

    @GetMapping("/kich-thuoc")
    public ResponseEntity<List<KichThuoc>> getListKichThuoc(){
        return ResponseEntity.ok(kichThuocService.findAllByTrangThai());
    }

    private static final String UPLOADED_FOLDER = "D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads\\";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile file : files) {
            try {
                File folder = new File(UPLOADED_FOLDER);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi tải lên file: " + e.getMessage());
            }
        }
        return ResponseEntity.ok("Tải lên thành công");
    }


    @PostMapping("/save-detail-product")
    public ResponseEntity<Boolean> saveDetailProduct(@RequestBody SanPhamSaveRequest sanPhamSaveRequest){
        SanPhamSaveRequest request = sanPhamSaveRequest;
        return ResponseEntity.ok(sanPhamService.saveDetailProduct(request));
    }

}
