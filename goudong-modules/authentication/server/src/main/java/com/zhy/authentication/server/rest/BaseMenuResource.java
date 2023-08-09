package com.zhy.authentication.server.rest;

import com.zhy.authentication.server.service.BaseMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.zhy.authentication.server.domain.BaseMenu}.
 */
@RestController
@RequestMapping("/menu")
public class BaseMenuResource {

    private final Logger log = LoggerFactory.getLogger(BaseMenuResource.class);

    private final BaseMenuService baseMenuService;

    public BaseMenuResource(BaseMenuService baseMenuService) {
        this.baseMenuService = baseMenuService;
    }
}
