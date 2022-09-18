package com.goudong.junit5.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

// @SpringBootTest
@ExtendWith(SpringExtension.class)
class AServiceBakTest {

    // @Test
    // void test1() {
    //     AService aService = mock(AService.class);
    //     int add = aService.add(1, 2);
    //     // 未定义存根，返回默认值 0 false null
    //     assertTrue(add == 0);
    //     when(aService.add(1, 2)).thenReturn(10);
    //
    //     int add1 = aService.add(1, 2);
    //     assertTrue(add1 == 10);
    // }
    //
    // /**
    //  * 需要以下其中一个
    //  * `@SpringBootTest`
    //  * `@ExtendWith(SpringExtension.class)`
    //  */
    // @Mock
    // AService aService;
    // @Test
    // void test2() {
    //     int add = aService.add(1, 2);
    //     assertTrue(add == 0);
    //     when(aService.add(1, 2)).thenReturn(10);
    //     assertTrue(aService.add(1, 2) == 10);
    // }
    //
    // @MockBean
    // AService mockBeanAService;
    // @Test
    // void test5() {
    //     int add = mockBeanAService.badd(1, 2);
    //     assertTrue(add == 0);
    //     when(mockBeanAService.add(1, 2)).thenReturn(10);
    //     assertTrue(10 == mockBeanAService.add(1,2));
    // }
    //
    //
    // @Test
    // void test3() {
    //     // 真实对象
    //     AService aService = spy(AService.class);
    //     // 调用实际方法
    //     int add = aService.add(1, 2);
    //     assertTrue(add == 3);
    //
    //     when(aService.add(1, 2)).thenReturn(10);
    //     assertTrue(aService.add(1, 2) == 10);
    // }
    //
    // @Spy
    // AService spyAService;
    //
    // @Test
    // void test4() {
    //     int add = spyAService.add(1, 2);
    //     assertTrue(add == 3);
    //
    //     when(spyAService.add(1, 2)).thenReturn(10);
    //     assertTrue(spyAService.add(1,2) == 10);
    // }

    // @SpyBean
    // AService spyBeanAService;
    //
    // @Test
    // void test6() {
    //     int add = spyBeanAService.add(1, 2);
    //     assertTrue(add == 3);
    //
    //     when(spyBeanAService.add(1, 2)).thenReturn(10);
    //     assertTrue(10 == spyBeanAService.add(1, 2));
    //
    // }

    @InjectMocks
    AService injectMockAService;

    @Mock
    BService bService;
    @Test
    void test7() {
        int add = injectMockAService.badd(1, 2);
        assertTrue(3 == add);
    }


}