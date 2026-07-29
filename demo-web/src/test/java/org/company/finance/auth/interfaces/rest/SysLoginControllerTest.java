package org.company.finance.auth.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.company.finance.auth.application.command.UserCommandService;
import org.company.finance.auth.application.query.UserQueryService;
import org.company.finance.auth.interfaces.rest.request.RefreshTokenRequestVO;
import org.company.finance.auth.interfaces.rest.response.LoginResponseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SysLoginControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private UserCommandService userCommandService;

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private SysLoginController sysLoginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sysLoginController).build();
    }

    @Test
    void shouldRefreshToken() throws Exception {
        RefreshTokenRequestVO requestVO = new RefreshTokenRequestVO();
        requestVO.setRefreshToken("refresh-token");

        LoginResponseVO responseVO = new LoginResponseVO();
        responseVO.setToken("new-access-token");
        responseVO.setRefreshToken("new-refresh-token");
        responseVO.setExpiresIn(7200);
        when(userCommandService.refreshToken("refresh-token")).thenReturn(responseVO);

        mockMvc.perform(post("/sys/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(requestVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new-refresh-token"));

        verify(userCommandService, times(1)).refreshToken(eq("refresh-token"));
    }
}
