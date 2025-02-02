package com.rabbiter.hospital.service;

import com.rabbiter.hospital.pojo.Patient;

import java.util.HashMap;
import java.util.List;

public interface PatientService {
    /**
     * 登录数据校验
     * */
    Patient login(int pId, String pPassword);
    /**
     * 分页模糊查询所有患者信息
     */
    HashMap<String, Object> findAllPatients(int pageNumber, int size, String query);
    /**
     * 删除患者信息
     */
    Boolean deletePatient(int pId);
    /**
     * 根据患者id查询患者信息
     */
    Patient findPatientById(int pId);
    /**
     * 增加患者信息
     */
    Boolean addPatient(Patient patient);
    /**
     * 统计患者男女人数
     */
    List<Integer> patientAge();

    /*
    * 更新患者信息
    * */
    Integer updatePatient(Patient patient);

    /*
     * 查询账号邮箱信息
     * */
    Boolean diffAccountEmail(Integer pId,String email);

    /*
    * 更新密码
    *
    * */

    Boolean updatePassword(int pId,String newPassword);
}
