package com.genug.wpob.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("애플리케이션 실행 테스트 - 성공: '/' 모든 접근 허용")
    void healthCheckTest() throws Exception {
        // expected
        mockMvc.perform(get("/"))
                .andExpectAll(
                        status().isOk(),
                        content().string("The service is up and running...."))
                .andDo(print());
    }
}