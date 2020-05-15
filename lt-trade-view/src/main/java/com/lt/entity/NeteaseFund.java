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
}
