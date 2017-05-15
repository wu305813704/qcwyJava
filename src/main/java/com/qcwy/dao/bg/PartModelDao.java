package com.qcwy.dao.bg;

import com.qcwy.entity.bg.PartModel;

import java.util.List;

/**
 * Created by KouKi on 2017/5/10.
 */
public interface PartModelDao {
    //获取所有零件类型
    List<PartModel> getAll();
}
