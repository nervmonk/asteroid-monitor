package sg.ic.asteroidmonitor.controller;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.ic.asteroidmonitor.dto.BaseResponse;
import sg.ic.asteroidmonitor.dto.FeedDateRangeReq;
import sg.ic.asteroidmonitor.dto.NeoLookupResponse;
import sg.ic.asteroidmonitor.dto.StandardFeedResponse;

@SpringBootTest
@AutoConfigureMockMvc
class NeoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSuccessFetch() throws Exception {
        var request = FeedDateRangeReq.builder()
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 8))
                .build();

        mockMvc.perform(
                post("/neo/feed")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    BaseResponse<List<StandardFeedResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertEquals(200, response.getStatus());
                    assertEquals("success", response.getMessage());
                    assertEquals(10, response.getData().size());
                });
    }

    @Test
    void testInvalidDateRange() throws Exception {
        var request = FeedDateRangeReq.builder()
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 4, 30))
                .build();

        mockMvc.perform(
                post("/neo/feed")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    BaseResponse<List<StandardFeedResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertEquals(400, response.getStatus());
                    assertEquals("startDate must be same or before endDate", response.getMessage());
                    assertEquals(null, response.getData());
                });
    }

    @Test
    void testExceedingDateRange() throws Exception {
        var request = FeedDateRangeReq.builder()
                .startDate(LocalDate.of(2024, 5, 1))
                .endDate(LocalDate.of(2024, 5, 9))
                .build();

        mockMvc.perform(
                post("/neo/feed")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    BaseResponse<List<StandardFeedResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertEquals(400, response.getStatus());
                    assertEquals("The date range must not exceed 7 days", response.getMessage());
                    assertEquals(null, response.getData());
                });
    }

    @Test
    void testEmptyDate() throws Exception {
        var request = FeedDateRangeReq.builder()
                .startDate(null)
                .endDate(LocalDate.of(2024, 5, 9))
                .build();

        mockMvc.perform(
                post("/neo/feed")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest())
                .andDo(result -> {
                    BaseResponse<List<StandardFeedResponse>> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertEquals(400, response.getStatus());
                    assertEquals("startDate: Start date is required", response.getMessage());
                    assertEquals(null, response.getData());
                });
    }

    @Test
    void testSuccessGetLookup() throws Exception {
        mockMvc.perform(
                get("/neo/lookup/54380194"))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    BaseResponse<NeoLookupResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertEquals(200, response.getStatus());
                    assertEquals("success", response.getMessage());
                    assertNotNull(response.getData());
                });
    }

    @Test
    void testLookupNotFound() throws Exception {
        mockMvc.perform(
                get("/neo/lookup/1"))
                .andExpectAll(status().isInternalServerError())
                .andDo(result -> {
                    BaseResponse<NeoLookupResponse> response = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertEquals(500, response.getStatus());
                    assertNull(response.getData());
                });
    }
}
