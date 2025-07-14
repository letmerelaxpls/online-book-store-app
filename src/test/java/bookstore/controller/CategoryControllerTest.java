package bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookstore.dto.book.BookDto;
import bookstore.dto.category.CategoryRequestDto;
import bookstore.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/categories/create-categories-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/categories/drop-categories-table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class CategoryControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("getAll should return all categories")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/categories/insert-2-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_2Categories_True() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode content = jsonNode.get("content");

        CategoryResponseDto[] responseDtos = objectMapper.treeToValue(
                content, CategoryResponseDto[].class);

        assertThat(responseDtos)
                .extracting(CategoryResponseDto::getName)
                .containsExactlyInAnyOrder("Horror", "Comedy");
    }

    @Test
    @DisplayName("getById should return Category with correct id")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/categories/insert-2-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_CategoryWithId1_True() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String expectedName = "Horror";
        String actualName = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                CategoryResponseDto.class).getName();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("getBooksByCategoryId should return correct books")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = {"classpath:database/books/create-books-table.sql",
            "classpath:database/books/create-books_categories-table.sql",
            "classpath:database/categories/insert-2-categories.sql",
            "classpath:database/books/insert-3-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/drop-books_categories-table.sql",
            "classpath:database/books/drop-books-table.sql",
            "classpath:database/categories/delete-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_CategoryWithId1_True() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories/1/books")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode content = jsonNode.get("content");

        BookDto[] responseDtos = objectMapper.treeToValue(content, BookDto[].class);

        assertThat(responseDtos)
                .extracting(BookDto::getTitle)
                .containsExactlyInAnyOrder("The Shining", "It")
                .doesNotContain("The Hitchhikers Guide to the Galaxy");
    }

    @Test
    @DisplayName("createCategory should return correct response dto")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_CommonCategoryRequest_True() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Horror");

        CategoryResponseDto expectedDto = new CategoryResponseDto();
        expectedDto.setName("Horror");

        String jsonRequestBody = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto resultDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryResponseDto.class);

        assertThat(resultDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("updateCategory should return correct updated category dto")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/insert-2-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ChangeCategoryToFantasy_True() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Fantasy");
        CategoryResponseDto expectedDto = new CategoryResponseDto();
        expectedDto.setId(1L);
        expectedDto.setName("Fantasy");

        String jsonRequestBody = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult mvcResult = mockMvc.perform(put("/categories/1")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto resultDto = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CategoryResponseDto.class);

        Assertions.assertEquals(expectedDto, resultDto);
    }

    @Test
    @DisplayName("deleteCategory should delete correct category")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/insert-2-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_CategoryWithId1_NoContent() throws Exception {
        String uri = "/categories/1";

        mockMvc.perform(delete(uri))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound());
    }
}
