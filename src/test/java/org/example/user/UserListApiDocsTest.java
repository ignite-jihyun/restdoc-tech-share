package org.example.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserListApiDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 사용자_목록_문서화() throws Exception {
        // 사전 데이터 2건 생성
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "name": "Alice",
                  "email": "alice@example.com"
                }
                """))
            .andExpect(status().isCreated());
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "name": "Bob",
                  "email": "bob@example.com"
                }
                """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-list",
                        responseFields(
                                fieldWithPath("[].id").description("사용자 ID"),
                                fieldWithPath("[].name").description("이름"),
                                fieldWithPath("[].email").description("이메일")
                        )
                ));
    }
}
