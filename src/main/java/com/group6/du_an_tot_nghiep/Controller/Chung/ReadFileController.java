package com.group6.du_an_tot_nghiep.Controller.Chung;

import com.group6.du_an_tot_nghiep.Service.KhachHang.KhachHangSanPhamService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ReadFileController {
    @Autowired
    KhachHangSanPhamService _khachHangSanPhamService;

    @Autowired
    TaiKhoanService _taiKhoanService;

    @GetMapping("/api/read-file-by-id-img/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFileByIdImg(@PathVariable("id") int id) {

        try {
            String photo = _khachHangSanPhamService.findUrlByIdHinhAnh(id);
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/api/read-file-by-idspct-return-anh-mac-dinh/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFileByIdSpct(@PathVariable("id") int id) {
        System.out.println("=======================");
        System.out.println(id);
        try {
            String photo = _khachHangSanPhamService.findUrlAnhMacDinh(id);
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/api/read-file-by-img-url/{url}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFileByImgUrl(@PathVariable("url") String url) {
        try {
            String photo = url;
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {

            return null;
        }
    }

    @GetMapping("/api/read-file-by-id-tai-khoan/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFileByIdTaiKhoan (@PathVariable("id") int id){
        try {
            String photo = _taiKhoanService.getTkById(id).getAnhDaiDien();
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
