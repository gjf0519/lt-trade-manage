package com.lt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaijf
 * @description 网易财经实时交易数据
 * @date 2020/4/14
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeteaseFund {
    private List<FundRecord> zhubi_list;
    private String begin;
    private String end;

    public List<FundRecord> getZhubi_list() {
        return zhubi_list;
    }

    public void setZhubi_list(List<FundRecord> zhubi_list) {
        this.zhubi_list = zhubi_list;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
