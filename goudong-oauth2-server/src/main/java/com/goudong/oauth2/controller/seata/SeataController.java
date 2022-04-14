package com.goudong.oauth2.controller.seata;

import com.goudong.commons.framework.core.Result;
import com.goudong.oauth2.po.StorageTblPO;
import com.goudong.oauth2.repository.StorageTblRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/4/14 22:13
 */
@Api(tags = "seata")
@Slf4j
@Controller
@RequestMapping("/seata")
public class SeataController {

    //~fields
    //==================================================================================================================
    private final StorageTblRepository storageTblRepository;

    public SeataController(StorageTblRepository storageTblRepository) {
        this.storageTblRepository = storageTblRepository;
    }

    //~methods
    //==================================================================================================================

    @ApiOperation(value = "减少库存", notes = "")
    @DeleteMapping("/del")
    @Transactional
    public Result delStorage () {
        StorageTblPO byId = storageTblRepository.findById(1).get();
        if (byId == null) {
            byId = new StorageTblPO();
            byId.setId(1);
            byId.setCommodityCode("code");
            byId.setCount(100);
            storageTblRepository.save(byId);
        }

        byId.setCount(byId.getCount() - 1);

        storageTblRepository.save(byId);
        return Result.ofSuccess();
    }
}