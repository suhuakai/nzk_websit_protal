package com.web.system.biz.userInfo.web;


import com.web.core.entity.R;
import com.web.core.util.PageUtils;
import com.web.system.biz.userInfo.service.ChinaregisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 前端控制器
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

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = chinaregisterService.queryPage(params);
        return R.ok().put("page", page);
    }


}
