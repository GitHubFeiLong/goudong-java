package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.service.BaseMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link BaseMenu}.
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
