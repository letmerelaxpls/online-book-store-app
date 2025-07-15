package bookstore.controller;

import static bookstore.util.TestUtil.createBookDtoWithTitleThat;
import static bookstore.util.TestUtil.createBookWithTitleThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Sql(scripts = "classpath:database/categories/insert-2-categories.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/categories/delete-2-categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class BookControllerTest {
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
    @DisplayName("getAll should return all books")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/books/insert-3-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-3-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ThreeBooks_True() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode content = jsonNode.get("content");

        BookDto[] bookDtos = objectMapper.treeToValue(content, BookDto[].class);

        assertThat(bookDtos)
                .extracting(BookDto::getTitle)
                .containsExactlyInAnyOrder("The Shining",
                        "It", "That", "The Hitchhikers Guide to the Galaxy",
                        "The Devils", "American Psycho");
    }

    @Test
    @DisplayName("getBookById should return correct book")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/books/insert-3-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-3-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_BookWithIdFour_True() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String expectedTitle = "The Shining";
        String actualTitle = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                BookDto.class).getTitle();
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    @DisplayName("searchBooks should return correct books with specified parameters")
    @WithMockUser(username = "user", roles = "USER")
    @Sql(scripts = "classpath:database/books/insert-3-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-3-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBooks_WithAuthorStephenKing_True() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("authors", "Stephen King"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse()
                .getContentAsString());
        JsonNode content = jsonNode.get("content");

        BookDto[] bookDtos = objectMapper.treeToValue(content, BookDto[].class);

        assertThat(bookDtos)
                .extracting(BookDto::getAuthor)
                .containsOnly("Stephen King");
    }

    @Test
    @DisplayName("createBook should return correct book dto")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/delete-1-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_BookWithTitleThat_True() throws Exception {
        String jsonRequestBody = objectMapper.writeValueAsString(createBookWithTitleThat());
        BookDto expectedDto = createBookDtoWithTitleThat();
        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("updateBookById should return correct updated book dto")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/insert-3-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-3-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBookById_ChangeBookToThat_True() throws Exception {
        CreateBookRequestDto requestDto = createBookWithTitleThat();
        BookDto expectedDto = createBookDtoWithTitleThat();
        String jsonRequestBody = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(put("/books/5")
                        .content(jsonRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto result = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("deleteBookById should remove correct book")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/insert-3-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-3-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBookById_BookWithIdFour_True() throws Exception {
        String uri = "/books/4";
        mockMvc.perform(delete(uri))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound());
    }
}
