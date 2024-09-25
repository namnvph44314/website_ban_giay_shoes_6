package com.group6.du_an_tot_nghiep.Service.KhachHang;

import com.group6.du_an_tot_nghiep.Controller.KhachHang.GioHangController;
import com.group6.du_an_tot_nghiep.Dao.*;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListGioHangChiTietResponse;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ListPggGioHang;
import com.group6.du_an_tot_nghiep.Dto.KhachHang.ThongTinGioHang;
import com.group6.du_an_tot_nghiep.Dto.QuanLy.PhieuGiamGia.PhieuGiamGiaRequest;
import com.group6.du_an_tot_nghiep.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GioHangService {

    @Autowired
    GioHangDao gioHangDao;

    @Autowired
    GioHangChiTietDao gioHangChiTietDao;

    @Autowired
    SanPhamChiTietDao sanPhamChiTietDao;

    @Autowired
    GiamGiaSanPhamChiTietDao giamGiaSanPhamChiTietDao;

    @Autowired
    PhieuGiamGiaDao phieuGiamGiaDao;

    public boolean themVaoGio(int idSpct, int soLuongMua, TaiKhoan taiKhoan) {
        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(idSpct).orElse(null);
        if (soLuongMua < 1) {
            return false;
        } else if (soLuongMua > sanPhamChiTiet.getSoLuong()) {
            return false;
        }else {
            if (gioHang == null) {
                gioHang = gioHangDao.save(new GioHang());
                GioHangController._gioHang = gioHang;
                GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
                gioHangChiTiet.setSoLuong(soLuongMua);
                gioHangChiTiet.setGioHangByIdGioHang(gioHang);
                gioHangChiTiet.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
                gioHangChiTiet.setTrangThai(1);
                gioHangChiTiet.setSelected(false);
                gioHangChiTietDao.save(gioHangChiTiet);
                return true;
            } else {
                GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findBySanPhamChiTietByIdSpctAndGioHangByIdGioHangAndTrangThai(sanPhamChiTiet,gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
                if (gioHangChiTiet == null) {
                    GioHangChiTiet gioHangChiTiet1 = new GioHangChiTiet();
                    gioHangChiTiet1.setSoLuong(soLuongMua);
                    gioHangChiTiet1.setGioHangByIdGioHang(gioHang);
                    gioHangChiTiet1.setTrangThai(1);
                    gioHangChiTiet1.setSelected(false);
                    gioHangChiTiet1.setSanPhamChiTietByIdSpct(sanPhamChiTiet);
                    gioHangChiTietDao.save(gioHangChiTiet1);
                    return true;
                } else {
                    if (gioHangChiTiet.getSoLuong() + soLuongMua > sanPhamChiTiet.getSoLuong()) {
                        return false;
                    }else{
                        gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() + soLuongMua);
                        gioHangChiTietDao.save(gioHangChiTiet);
                        return true;
                    }
                }
            }
        }
    }

    public GioHang taoGioHang() {
        GioHang gioHang = gioHangDao.save(new GioHang());
        return gioHang;
    }

    public GioHang getGioHangByTaiKhoan(TaiKhoan taiKhoan) {
        return gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
    }

    public List<ListGioHangChiTietResponse> getListGioHangChiTiet(GioHang gioHang) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietDao.fingGhctByGioHangAndTrangThaiSpct(gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
        List<ListGioHangChiTietResponse> responseList = new ArrayList<>();
        gioHangChiTietList.stream().forEach(gioHangChiTiet -> {
            ListGioHangChiTietResponse response = new ListGioHangChiTietResponse();
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(gioHangChiTiet.getSanPhamChiTietByIdSpct().getId()).orElse(null);
            BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
            int soLuongKho = sanPhamChiTiet.getSoLuong();
            int soLuongMua = gioHangChiTiet.getSoLuong();
            if (soLuongKho <= 0) {
                gioHangChiTiet.setSelected(false);
                gioHangChiTietDao.save(gioHangChiTiet);
            }else {
                if (soLuongMua > soLuongKho){
                    soLuongMua = soLuongKho;
                    gioHangChiTiet.setSoLuong(soLuongMua);
                    gioHangChiTietDao.save(gioHangChiTiet);
                }
            }
            response.setSoLuongKho(soLuongKho);
            response.setSoLuongMua(soLuongMua);
            if (giaGiam == null) {
                response.setGiaGiam(sanPhamChiTiet.getGia());
            } else {
                response.setGiaGiam(sanPhamChiTiet.getGia().subtract(giaGiam));
            }
            response.setIdSpct(sanPhamChiTiet.getId());
            response.setKichThuoc(sanPhamChiTiet.getKichThuocByIdKichCo());
            response.setMauSac(sanPhamChiTiet.getMauSacByIdMauSac());
            response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
            response.setSelected(gioHangChiTiet.getSelected());
            response.setIdGhct(gioHangChiTiet.getId());
            responseList.add(response);
        });
        return responseList.stream().sorted(Comparator.comparingInt(ListGioHangChiTietResponse::getIdGhct)).collect(Collectors.toList());
    }

    public boolean updateSoLuong(int idGhct, int soLuong) {
        try {
            GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findById(idGhct).orElse(null);
            gioHangChiTiet.setSoLuong(soLuong);
            gioHangChiTietDao.save(gioHangChiTiet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSelected(int idGhct, boolean selected) {
        try {
            GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findById(idGhct).orElse(null);
            gioHangChiTiet.setSelected(selected);
            gioHangChiTietDao.save(gioHangChiTiet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTrangThai(int idGhct, int trangThai) {
        try {
            GioHangChiTiet gioHangChiTiet = gioHangChiTietDao.findById(idGhct).orElse(null);
            gioHangChiTiet.setTrangThai(trangThai);
            gioHangChiTietDao.save(gioHangChiTiet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public ThongTinGioHang getThongTinGioHang(GioHang gioHang) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietDao.findGhctByGioHangAndTrangThaiSpctForThongTinGioHang(gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
        List<ListGioHangChiTietResponse> responseList = new ArrayList<>();
        gioHangChiTietList.stream().forEach(gioHangChiTiet -> {
            if (gioHangChiTiet.getSelected() == true) {
                ListGioHangChiTietResponse response = new ListGioHangChiTietResponse();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(gioHangChiTiet.getSanPhamChiTietByIdSpct().getId()).orElse(null);
                BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
                if (giaGiam == null) {
                    response.setGiaGiam(sanPhamChiTiet.getGia());
                } else {
                    response.setGiaGiam(sanPhamChiTiet.getGia().subtract(giaGiam));
                }
                response.setIdSpct(sanPhamChiTiet.getId());
                response.setKichThuoc(sanPhamChiTiet.getKichThuocByIdKichCo());
                response.setMauSac(sanPhamChiTiet.getMauSacByIdMauSac());
                response.setSoLuongMua(gioHangChiTiet.getSoLuong());
                response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
                response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
                response.setSelected(gioHangChiTiet.getSelected());
                response.setIdGhct(gioHangChiTiet.getId());
                responseList.add(response);
            }
        });

        List<BigDecimal> listThanhTien = new ArrayList<>();
        PhieuGiamGia phieuGiamGia = gioHang.getPhieuGiamGiaByIdPgg();
        BigDecimal tongTienTruocGiam = new BigDecimal(0);
        BigDecimal tienGiam = new BigDecimal(0);
        BigDecimal tongTienSauGiam = new BigDecimal(0);
        String maPgg;
        if (phieuGiamGia == null) {
            maPgg = null;
        } else {
            if (phieuGiamGia.getSoLuong() < 1 || phieuGiamGia.getNgayKetThuc().before(new Timestamp(System.currentTimeMillis()))) {
                  maPgg = null;
                  gioHang.setPhieuGiamGiaByIdPgg(null);
                  gioHangDao.save(gioHang);
            }else {
                maPgg = phieuGiamGia.getMaKhuyenMai();
            }
        }


        responseList.stream().forEach(response -> {
            listThanhTien.add(response.getGiaGiam().multiply(BigDecimal.valueOf(response.getSoLuongMua())));
        });
        for (BigDecimal thanhTien : listThanhTien) {
            tongTienTruocGiam = tongTienTruocGiam.add(thanhTien);
        }
        if (phieuGiamGia != null && phieuGiamGia.getGiaTriNhoNhat().compareTo(tongTienTruocGiam)==1) {
            maPgg = null;
            gioHang.setPhieuGiamGiaByIdPgg(null);
            gioHangDao.save(gioHang);
        }
        ThongTinGioHang thongTinGioHang = new ThongTinGioHang();
        if (maPgg == null) {
            thongTinGioHang.setTienGiam(tienGiam);
            thongTinGioHang.setMaPgg(null);
            thongTinGioHang.setTongTienSauGiam(tongTienTruocGiam);
            thongTinGioHang.setTongTienTruocGiam(tongTienTruocGiam);
        } else {
            if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                tienGiam = phieuGiamGia.getGiaTri();
            } else {
                tienGiam = tongTienTruocGiam.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienGiam) == -1) {
                    tienGiam = phieuGiamGia.getGiaTriToiDa();
                }
            }
            tongTienSauGiam = tongTienTruocGiam.subtract(tienGiam);
            thongTinGioHang.setTienGiam(tienGiam);
            thongTinGioHang.setMaPgg(maPgg);
            thongTinGioHang.setTongTienSauGiam(tongTienSauGiam);
            thongTinGioHang.setTongTienTruocGiam(tongTienTruocGiam);
        }
        return thongTinGioHang;
    }

    public boolean kiemTraPgg(GioHang gioHang, BigDecimal tongTienTruocGiam) {
        if (gioHang.getPhieuGiamGiaByIdPgg().getGiaTriNhoNhat().compareTo(tongTienTruocGiam) == 1) {
            gioHang.setPhieuGiamGiaByIdPgg(null);
            gioHangDao.save(gioHang);
            return false;
        } else {
            return true;
        }
    }

    public boolean updatePgg(GioHang gioHang, int idPgg) {
        if (idPgg == -1) {
            gioHang.setPhieuGiamGiaByIdPgg(null);
            gioHangDao.save(gioHang);
            return false;
        } else {
            gioHang.setPhieuGiamGiaByIdPgg(phieuGiamGiaDao.findById(idPgg).orElse(null));
            gioHangDao.save(gioHang);
            return true;
        }
    }

    public List<ListPggGioHang> getListPggPublic(BigDecimal tongTien) {
        List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaDao.findPggCongKhai(tongTien, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaList.stream().forEach(phieuGiamGia -> {
            ListPggGioHang pggGioHang = new ListPggGioHang();
            if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTri());
            } else {
                BigDecimal tienDuocGiamTheoDon = tongTien.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienDuocGiamTheoDon) == -1) {
                    pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTriToiDa());
                } else {
                    pggGioHang.setTienDuocGiamTheoDon(tienDuocGiamTheoDon);
                }
            }
            pggGioHang.setIdPgg(phieuGiamGia.getId());
            pggGioHang.setMaPgg(phieuGiamGia.getMaKhuyenMai());
            pggGioHang.setGiaTriGiam(phieuGiamGia.getGiaTri());
            pggGioHang.setGiamToiDa(phieuGiamGia.getGiaTriToiDa());
            pggGioHang.setDonToiThieu(phieuGiamGia.getGiaTriNhoNhat());
            pggGioHang.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
            pggGioHang.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            pggGioHang.setSoLuong(phieuGiamGia.getSoLuong());
            listPggGioHangs.add(pggGioHang);
        });

        listPggGioHangs.sort(new Comparator<ListPggGioHang>() {
            @Override
            public int compare(ListPggGioHang o1, ListPggGioHang o2) {
                return o2.getTienDuocGiamTheoDon().compareTo(o1.getTienDuocGiamTheoDon());
            }
        });
        return listPggGioHangs;
    }

    public List<ListPggGioHang> getListPggPrivate(BigDecimal tongTien, TaiKhoan taiKhoan) {
        List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaDao.findPggCaNhan(tongTien, taiKhoan.getId(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaList.stream().forEach(phieuGiamGia -> {
            ListPggGioHang pggGioHang = new ListPggGioHang();
            if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTri());
            } else {
                BigDecimal tienDuocGiamTheoDon = tongTien.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienDuocGiamTheoDon) == -1) {
                    pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTriToiDa());
                } else {
                    pggGioHang.setTienDuocGiamTheoDon(tienDuocGiamTheoDon);
                }
            }
            pggGioHang.setIdPgg(phieuGiamGia.getId());
            pggGioHang.setMaPgg(phieuGiamGia.getMaKhuyenMai());
            pggGioHang.setGiaTriGiam(phieuGiamGia.getGiaTri());
            pggGioHang.setGiamToiDa(phieuGiamGia.getGiaTriToiDa());
            pggGioHang.setDonToiThieu(phieuGiamGia.getGiaTriNhoNhat());
            pggGioHang.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
            pggGioHang.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            pggGioHang.setSoLuong(phieuGiamGia.getSoLuong());
            listPggGioHangs.add(pggGioHang);
        });

        listPggGioHangs.sort(new Comparator<ListPggGioHang>() {
            @Override
            public int compare(ListPggGioHang o1, ListPggGioHang o2) {
                return o2.getTienDuocGiamTheoDon().compareTo(o1.getTienDuocGiamTheoDon());
            }
        });
        return listPggGioHangs;
    }

    public List<ListPggGioHang> getListPggPublicInit(GioHang gioHang) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietDao.fingGhctByGioHangAndTrangThaiSpct(gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
        List<ListGioHangChiTietResponse> responseList = new ArrayList<>();
        gioHangChiTietList.stream().forEach(gioHangChiTiet -> {
            if (gioHangChiTiet.getSelected() == true) {
                ListGioHangChiTietResponse response = new ListGioHangChiTietResponse();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(gioHangChiTiet.getSanPhamChiTietByIdSpct().getId()).orElse(null);
                BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
                if (giaGiam == null) {
                    response.setGiaGiam(sanPhamChiTiet.getGia());
                } else {
                    response.setGiaGiam(giaGiam);
                }
                response.setIdSpct(sanPhamChiTiet.getId());
                response.setKichThuoc(sanPhamChiTiet.getKichThuocByIdKichCo());
                response.setMauSac(sanPhamChiTiet.getMauSacByIdMauSac());
                response.setSoLuongMua(gioHangChiTiet.getSoLuong());
                response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
                response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
                response.setSelected(gioHangChiTiet.getSelected());
                response.setIdGhct(gioHangChiTiet.getId());
                responseList.add(response);
            }
        });

        List<BigDecimal> listThanhTien = new ArrayList<>();
        BigDecimal tongTien = new BigDecimal(0);
        responseList.stream().forEach(response -> {
            listThanhTien.add(response.getGiaGiam().multiply(BigDecimal.valueOf(response.getSoLuongMua())));
        });
        for (BigDecimal thanhTien : listThanhTien) {
            tongTien = tongTien.add(thanhTien);
        }
        BigDecimal tongTienFinal = tongTien;
        List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaDao.findPggCongKhai(tongTienFinal, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaList.stream().forEach(phieuGiamGia -> {
            ListPggGioHang pggGioHang = new ListPggGioHang();
            if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTri());
            } else {
                BigDecimal tienDuocGiamTheoDon = tongTienFinal.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienDuocGiamTheoDon) == -1) {
                    pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTriToiDa());
                } else {
                    pggGioHang.setTienDuocGiamTheoDon(tienDuocGiamTheoDon);
                }
            }
            pggGioHang.setIdPgg(phieuGiamGia.getId());
            pggGioHang.setMaPgg(phieuGiamGia.getMaKhuyenMai());
            pggGioHang.setGiaTriGiam(phieuGiamGia.getGiaTri());
            pggGioHang.setGiamToiDa(phieuGiamGia.getGiaTriToiDa());
            pggGioHang.setDonToiThieu(phieuGiamGia.getGiaTriNhoNhat());
            pggGioHang.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
            pggGioHang.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            pggGioHang.setSoLuong(phieuGiamGia.getSoLuong());
            listPggGioHangs.add(pggGioHang);
        });

        listPggGioHangs.sort(new Comparator<ListPggGioHang>() {
            @Override
            public int compare(ListPggGioHang o1, ListPggGioHang o2) {
                return o2.getTienDuocGiamTheoDon().compareTo(o1.getTienDuocGiamTheoDon());
            }
        });
        return listPggGioHangs;
    }

    public List<ListPggGioHang> getListPggPrivateInit(GioHang gioHang, TaiKhoan taiKhoan) {
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietDao.fingGhctByGioHangAndTrangThaiSpct(gioHang, GioHangChiTietDao.CHO_THANH_TOAN);
        List<ListGioHangChiTietResponse> responseList = new ArrayList<>();
        gioHangChiTietList.stream().forEach(gioHangChiTiet -> {
            if (gioHangChiTiet.getSelected() == true) {
                ListGioHangChiTietResponse response = new ListGioHangChiTietResponse();
                SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(gioHangChiTiet.getSanPhamChiTietByIdSpct().getId()).orElse(null);
                BigDecimal giaGiam = giamGiaSanPhamChiTietDao.findGiaGiamByIdSpct(sanPhamChiTiet.getId());
                if (giaGiam == null) {
                    response.setGiaGiam(sanPhamChiTiet.getGia());
                } else {
                    response.setGiaGiam(giaGiam);
                }
                response.setIdSpct(sanPhamChiTiet.getId());
                response.setKichThuoc(sanPhamChiTiet.getKichThuocByIdKichCo());
                response.setMauSac(sanPhamChiTiet.getMauSacByIdMauSac());
                response.setSoLuongMua(gioHangChiTiet.getSoLuong());
                response.setTenSanPham(sanPhamChiTiet.getSanPhamByIdSanPham().getTenSanPham());
                response.setSoLuongKho(sanPhamChiTiet.getSoLuong());
                response.setSelected(gioHangChiTiet.getSelected());
                response.setIdGhct(gioHangChiTiet.getId());
                responseList.add(response);
            }
        });

        List<BigDecimal> listThanhTien = new ArrayList<>();
        BigDecimal tongTien = new BigDecimal(0);
        responseList.stream().forEach(response -> {
            listThanhTien.add(response.getGiaGiam().multiply(BigDecimal.valueOf(response.getSoLuongMua())));
        });
        for (BigDecimal thanhTien : listThanhTien) {
            tongTien = tongTien.add(thanhTien);
        }
        BigDecimal tongTienFinal = tongTien;
        List<ListPggGioHang> listPggGioHangs = new ArrayList<>();
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaDao.findPggCaNhan(tongTienFinal, taiKhoan.getId(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaList.stream().forEach(phieuGiamGia -> {
            ListPggGioHang pggGioHang = new ListPggGioHang();
            if (phieuGiamGia.getKieuGiaTri() == PhieuGiamGiaDao.TIEN) {
                pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTri());
            } else {
                BigDecimal tienDuocGiamTheoDon = tongTienFinal.multiply(phieuGiamGia.getGiaTri()).divide(new BigDecimal(100));
                if (phieuGiamGia.getGiaTriToiDa().compareTo(tienDuocGiamTheoDon) == -1) {
                    pggGioHang.setTienDuocGiamTheoDon(phieuGiamGia.getGiaTriToiDa());
                } else {
                    pggGioHang.setTienDuocGiamTheoDon(tienDuocGiamTheoDon);
                }
            }
            pggGioHang.setIdPgg(phieuGiamGia.getId());
            pggGioHang.setMaPgg(phieuGiamGia.getMaKhuyenMai());
            pggGioHang.setGiaTriGiam(phieuGiamGia.getGiaTri());
            pggGioHang.setGiamToiDa(phieuGiamGia.getGiaTriToiDa());
            pggGioHang.setDonToiThieu(phieuGiamGia.getGiaTriNhoNhat());
            pggGioHang.setKieuGiaTri(phieuGiamGia.getKieuGiaTri());
            pggGioHang.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
            pggGioHang.setSoLuong(phieuGiamGia.getSoLuong());
            listPggGioHangs.add(pggGioHang);
        });

        listPggGioHangs.sort(new Comparator<ListPggGioHang>() {
            @Override
            public int compare(ListPggGioHang o1, ListPggGioHang o2) {
                return o2.getTienDuocGiamTheoDon().compareTo(o1.getTienDuocGiamTheoDon());
            }
        });
        return listPggGioHangs;
    }

    public Integer getSoLuongSpTrongGh(int idSpct, TaiKhoan taiKhoan) {
        System.out.println("====================");
        System.out.println(idSpct);
        System.out.println(taiKhoan.getId());
        GioHang gioHang = gioHangDao.findByTaiKhoanByIdTaiKhoan(taiKhoan).orElse(null);
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietDao.findById(idSpct).orElse(null);
        Integer soLuong = gioHangChiTietDao.getSoLuongSpctTrongGio(sanPhamChiTiet, gioHang).orElse(null);
        System.out.println("so Luong");
        System.out.println(soLuong);
        if (soLuong == null) {
            return null;
        } else {
            return soLuong;
        }
    }
}
