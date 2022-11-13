package com.goudong.commons.dto.oauth2;

import com.goudong.core.util.tree.v2.TreeInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

@ExtendWith({})
class BaseMenuDTOTest {

    @Test
    void testTree() {
        List<BaseMenuDTO> lists = new ArrayList<>();
        BaseMenuDTO baseMenuDTO = new BaseMenuDTO();
        baseMenuDTO.setId(1L);

        BaseMenuDTO baseMenuDTO1 = new BaseMenuDTO();
        baseMenuDTO1.setId(2L);
        baseMenuDTO1.setParentId(1L);


        BaseMenuDTO baseMenuDTO2 = new BaseMenuDTO();
        baseMenuDTO2.setId(3L);
        baseMenuDTO2.setParentId(1L);

        lists.add(baseMenuDTO);
        lists.add(baseMenuDTO1);
        lists.add(baseMenuDTO2);

        System.out.println("lists = " + lists);

        List<BaseMenuDTO> treeInterfaces = TreeInterface.toTreeByInterface(lists);
        System.out.println("treeInterfaces = " + treeInterfaces);

    }
}