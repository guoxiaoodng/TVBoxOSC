package com.github.tvbox.osc.constant;

import com.github.tvbox.osc.bean.IdNameAddressBean;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static List<IdNameAddressBean> getConfigList() {
        List<IdNameAddressBean> addressList = new ArrayList<>();
        addressList.add(new IdNameAddressBean("巧记", "http://pandown.pro/tvbox/tvbox.json"));
        addressList.add(new IdNameAddressBean("胖虎", "https://notabug.org/imbig66/tv-spider-man/raw/master/配置/0801.json"));
        addressList.add(new IdNameAddressBean("欧歌", "http://tv.nxog.top/m/"));
        addressList.add(new IdNameAddressBean("Pandown", "http://pandown.pro/tvbox/tvbox.json"));
        addressList.add(new IdNameAddressBean("MEOW", "https://github.com/YuanHsing/freed/raw/master/TVBox/meow.json"));


        return addressList;
    }

    public static String DEFAULT_URL = "http://pandown.pro/tvbox/tvbox.json";

    public static String getTabName() {
        return "主页";
    }
}
