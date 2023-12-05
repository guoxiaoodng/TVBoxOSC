package com.github.tvbox.osc.constant;

import com.github.tvbox.osc.bean.IdNameAddressBean;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static List<IdNameAddressBean> getConfigList() {
        List<IdNameAddressBean> addressList = new ArrayList<>();
        addressList.add(new IdNameAddressBean(0, "南风", "https://agit.ai/Yoursmile7/TVBox/raw/branch/master/XC.json"));
        addressList.add(new IdNameAddressBean(1, "未命名", "https://raw.liucn.cc/box/m.json"));
        addressList.add(new IdNameAddressBean(2, "CH猫友", "https://freed.yuanhsing.cf/TVBox/meowcf.json"));

        return addressList;
    }
}
