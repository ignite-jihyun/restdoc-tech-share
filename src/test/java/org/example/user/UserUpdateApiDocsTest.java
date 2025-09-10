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
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserUpdateApiDocsTest {

    @Autowired
    MockMvc mockMvc;

    @SuppressWarnings("DataFlowIssue")
    @Test
    void 사용자_수정_문서화() throws Exception {
        // 생성
        var result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Dave",
                          "email": "dave@example.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();
        String location = result.getResponse().getHeader("Location");
        Long id = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "David",
                          "email": "david@example.com"
                        }
                        """))
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        pathParameters(
                                parameterWithName("id").description("사용자 ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("name").description("이름").optional(),
                                fieldWithPath("email").description("이메일").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 ID"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일")
                        )
                ));
    }
}
