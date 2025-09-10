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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs
class UserDeleteApiDocsTest {

    @Autowired
    MockMvc mockMvc;

    @SuppressWarnings("DataFlowIssue")
    @Test
    void 사용자_삭제_문서화() throws Exception {
        // 생성
        var result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Eve",
                          "email": "eve@example.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andReturn();
        String location = result.getResponse().getHeader("Location");
        Long id = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(document("user-delete",
                        pathParameters(
                                parameterWithName("id").description("사용자 ID")
                        )
                ));
    }
}
