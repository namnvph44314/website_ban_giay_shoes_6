
package com.group6.du_an_tot_nghiep.Controller.QuanLy;

import com.group6.du_an_tot_nghiep.Dao.TaiKhoanDao;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.KhachHangSanPhamSearchRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.DiaChi.DiaChiRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.HoaDon.ThongTinDonHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanRequest;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.TaiKhoan.TaiKhoanResponse;
import com.group6.du_an_tot_nghiep.Entities.DiaChi;
import com.group6.du_an_tot_nghiep.Entities.TaiKhoan;
import com.group6.du_an_tot_nghiep.Service.FileStorageService;
import com.group6.du_an_tot_nghiep.Service.QuanLy.TaiKhoanService;
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

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller

public class TaiKhoanApi {
    @Autowired
    TaiKhoanService taiKhoanService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private TaiKhoanDao taiKhoanDao;

    String _imgUrl;
    TaiKhoanRequest _taiKhoanRequest=new TaiKhoanRequest("","","","","",null,1,0,0,0+"","",1);
    int _idKh=0;

    @GetMapping("/kh/tai-khoan/api/page-tk")
    public ResponseEntity<Page<TaiKhoanResponse>> PageTkRes(
            @RequestParam("page") Optional<Integer> pageParam
    ) {
        int page= pageParam.orElse(0);
        return ResponseEntity.ok(taiKhoanService.getPageTK(page));
    }

    @GetMapping("/kh/tai-khoan/api/loc")
    public ResponseEntity<Page<TaiKhoanResponse>> loc(
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam(value = "gioiTinh",required = false) String gioiTinhParam,
            @RequestParam(value = "trangThai",required = false) String trangThaiParam
    ) {
        int page= pageParam.orElse(0);
        Integer gioiTinh=Integer.parseInt(gioiTinhParam);
        Integer trangThai=Integer.parseInt(trangThaiParam);
        return ResponseEntity.ok(taiKhoanService.getLocTK(gioiTinh,trangThai,page));
    }

    @GetMapping("/kh/tai-khoan/api/tim-kiem")
    public ResponseEntity<Page<TaiKhoanResponse>> timKiem(
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam(value = "timKiem",required = false) String timKiemParam
    ) {
        int page= pageParam.orElse(0);
        return ResponseEntity.ok(taiKhoanService.timKiem(timKiemParam,page));
    }

    @PostMapping(value = "/api/uploadFile-khach-hang")
    public String uploadFile(@RequestParam MultipartFile file) throws IOException{
        _imgUrl = file.getOriginalFilename();
        Path path = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads\\");
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()){
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream,path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        }
        return "redirect:/tai-khoan/add";

    }

    @PostMapping(value = "/api/uploadFile-detail-khach-hang")
    public String uploadFileDetail(@RequestParam MultipartFile file) throws IOException{
        _imgUrl = file.getOriginalFilename();
        Path path = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads");
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()){
            TaiKhoan tk= taiKhoanService.getTkById(_idKh);
            tk.setAnhDaiDien(_imgUrl);
            taiKhoanDao.save(tk);
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream,path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        }
        return "redirect:/tai-khoan/detail/"+_idKh;

    }

    @PostMapping(value = "/kh/tai-khoan/api/set-tai-khoan")
    public ResponseEntity<TaiKhoanRequest> setTaiKhoan(@RequestBody TaiKhoanRequest taiKhoanRequest){
        _taiKhoanRequest=taiKhoanRequest;
        System.out.println(taiKhoanRequest);
        return ResponseEntity.ok(taiKhoanRequest);
    }

    @GetMapping("/kh/tai-khoan/api/get-tai-khoan")
    public ResponseEntity<TaiKhoanRequest> getTaiKhoan(){
        System.out.println(_taiKhoanRequest);
        return ResponseEntity.ok(_taiKhoanRequest);
    }

    @GetMapping("/api/read-file-khach-hang")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile() {
        try {
            String photo = _imgUrl;
            Path fileName = Paths.get("D:\\du_an_tot_nghiep\\src\\main\\resources\\uploads\\", photo);
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

    @GetMapping("/api/read-file-khach-hang/{id}")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> readFile (@PathVariable("id") int id){
        try {
            String photo = taiKhoanService.getTkById(id).getAnhDaiDien();
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

    @PostMapping("/kh/tai-khoan/api/add")
    public ResponseEntity<TaiKhoanRequest> add(@RequestBody TaiKhoanRequest taiKhoanRequest) throws IOException {
        taiKhoanRequest.setAnh(_imgUrl);
        _taiKhoanRequest=new TaiKhoanRequest("","","","","",null,1,0,0,0+"","",1);
        return ResponseEntity.ok(taiKhoanService.add(taiKhoanRequest));
    }


    @GetMapping("/kh/tai-khoan/api/sdt")
    public ResponseEntity<TaiKhoan> sdt(
            @RequestParam(value = "sdt",required = false) String sdtParam
    ) {
        return ResponseEntity.ok(taiKhoanService.getSdt(sdtParam));
    }

    @GetMapping("/kh/tai-khoan/api/email")
    public ResponseEntity<TaiKhoan> email(
            @RequestParam(value = "email",required = false) String emailParam
    ) {
        return ResponseEntity.ok(taiKhoanService.getEmail(emailParam));
    }

    @GetMapping("/kh/tai-khoan/api/sdt-update")
    public ResponseEntity<TaiKhoan> sdtUpdate(
            @RequestParam(value = "sdt",required = false) String sdtParam,
            @RequestParam(value = "id",required = false) String idParam
    ) {
        return ResponseEntity.ok(taiKhoanService.getSdtUpdate(sdtParam,Integer.parseInt(idParam)));
    }

    @GetMapping("/kh/tai-khoan/api/email-update")
    public ResponseEntity<TaiKhoan> emailUpdate(
            @RequestParam(value = "email",required = false) String emailParam,
            @RequestParam(value = "id",required = false) String idParam
    ) {
        return ResponseEntity.ok(taiKhoanService.getEmailUpdate(emailParam,Integer.parseInt(idParam)));
    }

    @GetMapping("/kh/tai-khoan/api/update-trang-thai/{id}")
    public ResponseEntity<TaiKhoan> updateTrangThai(
            @PathVariable("id") TaiKhoan taiKhoan
    ){
        System.out.println(taiKhoan.getId());
        int id=taiKhoan.getId();
        taiKhoanService.updateTrangThai(null,taiKhoanService.getById(id).getTrangThai(),id);
        return ResponseEntity.ok(taiKhoan);
    }

    @GetMapping("/kh/tai-khoan/api/detail/{id}")
    public ResponseEntity<TaiKhoan> detail(
            @PathVariable("id") TaiKhoan taiKhoan,
            Model model
    ){
        _idKh=taiKhoan.getId();
        model.addAttribute("idKh",_idKh);
        return ResponseEntity.ok(taiKhoan);
    }

    @PutMapping("/kh/tai-khoan/api/update/{id}")
    public ResponseEntity<TaiKhoanRequest> update(@PathVariable("id") int id, @RequestBody TaiKhoanRequest taiKhoanRequest){
        taiKhoanRequest.setAnh(_imgUrl);
        return ResponseEntity.ok(taiKhoanService.updateTaiKhoan(id,taiKhoanRequest));
    }

    @GetMapping("/kh/tai-khoan/api/detail-dia-chi/{id}")
    public ResponseEntity<DiaChi> detailDiaChiMacDinh(
            @PathVariable("id") int id
    ){
        return ResponseEntity.ok(taiKhoanService.getTkByIdKhAndMd(id));
    }

    @PutMapping("/kh/tai-khoan/api/update-dia-chi-mac-dinh/{id}")
    public ResponseEntity<DiaChiRequest> updateDiaChiMd(@PathVariable("id") int id, @RequestBody DiaChiRequest diaChiRequest){
        return ResponseEntity.ok(taiKhoanService.updateDiaChi(id,diaChiRequest));
    }

    @PostMapping("/kh/tai-khoan/api/add-dia-chi")
    public ResponseEntity<DiaChiRequest> addDiaChi(@RequestBody DiaChiRequest diaChiRequest){
        return ResponseEntity.ok(taiKhoanService.addDiaChi(diaChiRequest,_idKh));
    }

    @GetMapping("/kh/tai-khoan/api/page-dia-chi")
    public ResponseEntity<Page<DiaChi>> PageDiaChi(
            @RequestParam("page") Optional<Integer> pageParam
    ) {
        int page= pageParam.orElse(0);
        System.out.println("=====================");
        System.out.println(_idKh);
        return ResponseEntity.ok(taiKhoanService.getPageDiaChi(_idKh,page));
    }

    @DeleteMapping("/kh/tai-khoan/api/deleteDc/{id}")
    public ResponseEntity<Void> deleteDc(
            @PathVariable("id") int idDc
    ){
        taiKhoanService.deleteDc(idDc);
        System.out.println("Vô òiiiiiiiiiiiiiiiii");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/kh/tai-khoan/api/update-mac-dinh")
    public ResponseEntity<Void> updateMacDinh(
            @RequestParam("idDc") String idDc
    ){
        int idDiaChi=Integer.parseInt(idDc);
        taiKhoanService.updateMacDinhTrue(idDiaChi);
        taiKhoanService.updateMacDinhFalse(idDiaChi,_idKh);
        return ResponseEntity.ok().build();
    }
}
