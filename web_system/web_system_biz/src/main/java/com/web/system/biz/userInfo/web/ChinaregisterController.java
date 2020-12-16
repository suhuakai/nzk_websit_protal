package com.web.system.biz.userInfo.web;


import com.web.core.entity.Pager;
import com.web.system.api.entity.Chinaregister;
import com.web.system.biz.userInfo.service.ChinaregisterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sunhua
 * @since 2020-12-15
 */
@RestController
@RequestMapping("/chinaregister")
public class ChinaregisterController {
    @Autowired
    private ChinaregisterService chinaregisterService;


    @RequestMapping("/getList")
    public Pager getList(String rno,  Integer pageSize, Integer pageNum) {
        Pager pager = new Pager(true);
        pager.setPageNum(pageNum == null ? 1 : pageNum);
        pager.setPageSize(pageSize == null ? 10 : pageSize);
        pager.addQueryParam("rno", rno);
        List<Chinaregister> chinaregisterList = chinaregisterService.getList(pager);
        pager.setRows(chinaregisterList);
        return pager;
    }


}
