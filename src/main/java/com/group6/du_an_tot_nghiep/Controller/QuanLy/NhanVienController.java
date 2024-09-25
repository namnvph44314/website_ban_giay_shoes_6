package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienDetail;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.NhanVienFillDTO;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanRequest;
import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.QuanLy.NhanVienService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
//@RequestMapping("/admin/nhan-vien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    TaiKhoanService taiKhoanService;

    @Autowired
            private TaiKhoanDao taiKhoanDao;

    String _imgUrl;
    Integer _idTaiKhoan;
    int _idKh = 0;

    @GetMapping("/admin/nhan-vien/index")
    public String index() {
        return "/quan-ly/nhan_vien/index_nhan_vien";
    }

    @GetMapping("/api/nhan-vien/add")
    public String add() {
        return "/quan-ly/nhan_vien/add_nhan_vien";
    }


    @GetMapping("/api/nhan-vien/detail-nv/{id}")
    public String detail(@PathVariable("id") TaiKhoan idTaiKhoan) {
        _idTaiKhoan = idTaiKhoan.getId();
        System.out.println("idTaiKhoan"+idTaiKhoan.getId());
        return "/quan-ly/nhan_vien/update";
    }



    @PostMapping(value = "/api/uploadFile-nhan-vien")
    public String uploadFile(@RequestParam MultipartFile file) throws IOException {
        _imgUrl = file.getOriginalFilename();
        Path path = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads");
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        }
        return "redirect:/api/nhan-vien/add";

    }

    @PostMapping(value = "/api/uploadFile-detail-nhan-vien")
    public String uploadFileDetail(@RequestParam MultipartFile file) throws IOException{
        _imgUrl = file.getOriginalFilename();
        Path path = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads");
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()){
            InputStream inputStream = file.getInputStream();
            TaiKhoan tk= taiKhoanService.getTkById(_idTaiKhoan);
            System.out.println("_idKh"+ _idTaiKhoan);
            tk.setAnhDaiDien(_imgUrl);
            taiKhoanDao.save(tk);
            Files.copy(inputStream,path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        }

        return "redirect:/api/nhan-vien/detail-nv/"+_idTaiKhoan;

    }

    @GetMapping("/api/read-file-nhan-vien")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile() {
        try {
            String photo = _imgUrl;
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

    @GetMapping("/api/read-file-nv/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile(@PathVariable("id") int id) {
        try {
            String photo = taiKhoanService.getTkById(id).getAnhDaiDien();
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads", photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/api/nhan-vien/search")
    public ResponseEntity<Page<TaiKhoan>> searchTaiKhoan(@RequestBody NhanVienFillDTO nhanVienFillDTO) {
        try {
            Page<TaiKhoan> result = nhanVienService.searchByNhanVien(nhanVienFillDTO);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/nhan-vien/add-nv")
    public ResponseEntity<?> addTaiKhoan(@Valid @RequestBody TaiKhoanRequest taiKhoanRequest) {
        try {
            taiKhoanRequest.setAnh(_imgUrl);
            System.out.println("anh" + taiKhoanRequest.getAnh());
            TaiKhoanRequest createdTaiKhoan = nhanVienService.add(taiKhoanRequest);
            _imgUrl = null;
            return new ResponseEntity<>(createdTaiKhoan, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/api/nhan-vien/detail/{id}")
    public ResponseEntity<TaiKhoan> detail(
            @PathVariable("id") TaiKhoan taiKhoan,
            Model model
    ){
        _idKh=taiKhoan.getId();
        model.addAttribute("idKh",_idKh);
        return ResponseEntity.ok(taiKhoan);
    }

    @GetMapping("/api/nhan-vien/detail-dia-chi/{id}")
    public ResponseEntity<DiaChi> detailDiaChiMacDinh(
            @PathVariable("id") int id
    ){
        return ResponseEntity.ok(nhanVienService.getTkByIdKhAndMd(id));
    }

    @GetMapping("/api/nhan-vien/detail-nhan-vien")
    public ResponseEntity<NhanVienDetail> detailNhanVien(

    ){
        return ResponseEntity.ok(nhanVienService.getdetailNv(_idTaiKhoan));
    }

    @PutMapping("/api/nhan-vien/update-nv")
    public ResponseEntity<TaiKhoanRequest> updateTaiKhoan( @RequestBody TaiKhoanRequest taiKhoanRequest) {
        try {
            TaiKhoanRequest updatedTaiKhoan = nhanVienService.update(_idTaiKhoan, taiKhoanRequest);

            return ResponseEntity.ok(updatedTaiKhoan);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/api/nhan-vien/check-cccd")
    public ResponseEntity<Boolean> checkCCCD(@RequestParam String soCanCuoc) {
        Optional<TaiKhoan> existingTaiKhoan = taiKhoanDao.findBySoCanCuoc(soCanCuoc);
        return ResponseEntity.ok(existingTaiKhoan.isPresent());
    }
    @PutMapping("/api/capnhattrangthai/{id}")
    public ResponseEntity<TaiKhoan> updateTrangThai(@PathVariable Integer id) {
        try {
            TaiKhoan updatedTaiKhoan = nhanVienService.updateTrangThai(id);
            return ResponseEntity.ok(updatedTaiKhoan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/nv/tai-khoan/api/sdt")
    public ResponseEntity<TaiKhoan> sdt(
            @RequestParam(value = "sdt",required = false) String sdtParam
    ) {
        return ResponseEntity.ok(nhanVienService.getSdt(sdtParam));
    }

    @GetMapping("/nv/tai-khoan/api/email")
    public ResponseEntity<TaiKhoan> email(
            @RequestParam(value = "email",required = false) String emailParam
    ) {
        return ResponseEntity.ok(nhanVienService.getEmail(emailParam));
    }


    @GetMapping("/nv/tai-khoan/api/sdt-update")
    public ResponseEntity<TaiKhoan> sdtUpdate(
            @RequestParam(value = "sdt",required = false) String sdtParam,
            @RequestParam(value = "id",required = false) String idParam
    ) {
        return ResponseEntity.ok(nhanVienService.getSdtUpdate(sdtParam,Integer.parseInt(idParam)));
    }

    @GetMapping("/nv/tai-khoan/api/email-update")
    public ResponseEntity<TaiKhoan> emailUpdate(
            @RequestParam(value = "email",required = false) String emailParam,
            @RequestParam(value = "id",required = false) String idParam
    ) {
        return ResponseEntity.ok(nhanVienService.getEmailUpdate(emailParam,Integer.parseInt(idParam)));
    }

    @GetMapping("/nv/tai-khoan/api/socc-update")
    public ResponseEntity<TaiKhoan> soccUpdate(
            @RequestParam(value = "soCanCuoc",required = false) String soCanCuocParam,
            @RequestParam(value = "id",required = false) String idParam
    ) {
        return ResponseEntity.ok(nhanVienService.getSocccdUpdate(soCanCuocParam,Integer.parseInt(idParam)));
    }

}
