package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbiter.hospital.mapper.PatientMapper;
import com.rabbiter.hospital.pojo.Checks;
import com.rabbiter.hospital.pojo.Patient;
import com.rabbiter.hospital.service.PatientService;
//import com.rabbiter.hospital.utils.Md5Util;
import com.rabbiter.hospital.utils.HashUtil;
import com.rabbiter.hospital.utils.TodayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PatientService")
public class PatientServiceImpl implements PatientService {
    protected static final Logger Log = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Resource
    private PatientMapper patientMapper;


    /**
     * 登录数据校验
     * */
    @Override
    public Patient login(int pId, String pPassword){
        Patient patient = this.patientMapper.selectById(pId);
        if (patient==null){
            return null;
        }
        if(0 == patient.getPState()) {
            return null;
        }
        //String password = Md5Util.getMD5(pPassword);
        String password = HashUtil.getSHA256(pPassword);
        if ((patient.getPPassword()).equals(password)) {
            return patient;
        }
        return null;
    }
    /**
     * 分页模糊查询所有患者信息
     */
    @Override
    public HashMap<String, Object> findAllPatients(int pageNumber, int size, String query) {
        Page<Patient> page = new Page<>(pageNumber, size);
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.like("p_name", query).orderByDesc("p_state");
        IPage<Patient> iPage = this.patientMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("patients", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 删除患者信息
     */
    @Override
    public Boolean deletePatient(int pId) {
        Patient patient = new Patient();
        patient.setPId(pId);
        patient.setPState(0);
        this.patientMapper.updateById(patient);
        return true;
    }
    /**
     * 根据患者id查询患者信息
     */
    @Override
    public Patient findPatientById(int pId){
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.patientMapper.selectOne(wrapper);
    }

    /**
     * 增加患者信息
     */
    @Override
    public Boolean addPatient(Patient patient) {
        //如果账号已存在则返回false
        List<Patient> patients = this.patientMapper.selectList(null);
        for (Patient patient1 : patients) {
            if (patient.getPId() == patient1.getPId()) {
                return false;
            }
            if ((patient.getPEmail()).equals(patient1.getPEmail()) ){
                return false;
            }
        }
        int yourYear = Integer.parseInt(patient.getPBirthday().substring(0, 4));
        int todayYear = Integer.parseInt(TodayUtil.getTodayYmd().substring(0,4));
        //密码md5加密
        //String password = Md5Util.getMD5(patient.getPPassword());
        String password = HashUtil.getSHA256(patient.getPPassword());
        patient.setPPassword(password);
        patient.setPAge(todayYear-yourYear);
        patient.setPState(1);
        this.patientMapper.insert(patient);
        return true;
    }
    /**
     * 统计患者男女人数
     */
    public List<Integer> patientAge(){
        List<Integer> ageList = new ArrayList<>();
        Integer age1 = this.patientMapper.patientAge(0, 9);
        Integer age2 = this.patientMapper.patientAge(10, 19);
        Integer age3 = this.patientMapper.patientAge(20, 29);
        Integer age4 = this.patientMapper.patientAge(30, 39);
        Integer age5 = this.patientMapper.patientAge(40, 49);
        Integer age6 = this.patientMapper.patientAge(50, 59);
        Integer age7 = this.patientMapper.patientAge(60, 69);
        Integer age8 = this.patientMapper.patientAge(70, 79);
        Integer age9 = this.patientMapper.patientAge(80, 89);
        Integer age10 = this.patientMapper.patientAge(90, 99);
        ageList.add(age1);
        ageList.add(age2);
        ageList.add(age3);
        ageList.add(age4);
        ageList.add(age5);
        ageList.add(age6);
        ageList.add(age7);
        ageList.add(age8);
        ageList.add(age9);
        ageList.add(age10);
        return ageList;

    }


    /*
    *更新患者信息
    * */
    @Override
    public Integer updatePatient(Patient patient) {
        return this.patientMapper.updateById(patient);
    }

    /*
     * 验证账号邮箱是否一致
     * */
    @Override
    public Boolean diffAccountEmail(Integer pId, String email) {
        Patient patient = this.patientMapper.selectById(pId);
        if (patient==null){
            return false;
        }
        Gson gson = new Gson();
        String json = gson.toJson(patient);
        // 将JSON字符串转换为Map对象
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> patientMap = gson.fromJson(json, type);
        return patientMap.get("pEmail").equals(email);
    }

    @Override
    public Boolean updatePassword(int pId, String newPassword) {
        Patient patient = this.patientMapper.selectById(pId);
        if (patient != null) {
            // 密码加密
            String password = HashUtil.getSHA256(newPassword);

            // 检查新密码是否与旧密码相同
            boolean isSamePassword = patient.getPPassword().equals(password);
            if (isSamePassword) {
                // 新密码与旧密码相同，无需更新
                return false;
            }
            // 更新患者密码
            patient.setPPassword(password);
            int rowsUpdated = this.patientMapper.updateById(patient);

            // 检查更新是否成功
            return rowsUpdated > 0;
        } else {
            // 患者不存在
            return false;
        }
    }


}


