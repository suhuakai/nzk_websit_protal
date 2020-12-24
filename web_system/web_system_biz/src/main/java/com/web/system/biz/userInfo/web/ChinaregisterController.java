package com.web.system.biz.userInfo.web;


import com.web.common.security.permission.annotation.Opened;
import com.web.core.util.LocalAssert;
import com.web.system.api.entity.Chinaregister;
import com.web.system.biz.userInfo.service.ChinaregisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Opened
public class ChinaregisterController {

    @Autowired
    private ChinaregisterService chinaregisterService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public Chinaregister list(String id) {
        LocalAssert.notNull(id,"ID不允许为空");
      Chinaregister chinaregister = chinaregisterService.selectById(id);
      return  chinaregister;

    }


}
