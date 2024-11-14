package sg.ic.asteroidmonitor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.ic.asteroidmonitor.dto.BaseResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;

@SpringBootTest
@AutoConfigureMockMvc
class TopTenAsteroidsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSuccessFetchingYear() throws Exception{
        mockMvc.perform(get("/top-10/2023"))
        .andExpect(status().isOk())
        .andDo(result -> {
            BaseResponse<List<StandardFeedResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() { 
            });
            assertEquals(200, response.getStatus());
            assertEquals("success", response.getMessage());
            assertEquals(10, response.getData().size());
        });
    }
}
