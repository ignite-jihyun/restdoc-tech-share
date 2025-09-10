package org.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserGetApiDocsTest {

    @Autowired
    MockMvc mockMvc;

    @SuppressWarnings("DataFlowIssue")
    @Test
    void 사용자_조회_문서화() throws Exception {
        // 먼저 생성
        var result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Charlie",
                          "email": "charlie@example.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();
        String location = result.getResponse().getHeader("Location");
        Long id = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

        mockMvc.perform(get("/users/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-get",
                        pathParameters(
                                parameterWithName("id").description("사용자 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 ID"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일")
                        )
                ));
    }
}
