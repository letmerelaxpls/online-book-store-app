package bookstore.service;

import static bookstore.util.TestUtil.createHorrorCategory;
import static bookstore.util.TestUtil.createHorrorCategoryRequestDto;
import static bookstore.util.TestUtil.createHorrorCategoryResponseDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bookstore.dto.category.CategoryRequestDto;
import bookstore.dto.category.CategoryResponseDto;
import bookstore.mapper.CategoryMapper;
import bookstore.model.Category;
import bookstore.repository.category.CategoryRepository;
import bookstore.service.category.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("findAll should return all categories")
    void findAll_TwoCategories_True() {
        //given
        Category firstCategory = new Category();
        firstCategory.setId(2L);
        firstCategory.setName("Comedy");
        CategoryResponseDto secondCategoryResponseDto = new CategoryResponseDto();
        secondCategoryResponseDto.setId(2L);
        secondCategoryResponseDto.setName("Comedy");

        Pageable pageable = PageRequest.of(0, 10);
        //when
        Category secondCategory = createHorrorCategory();
        CategoryResponseDto firstCategoryResponseDto = createHorrorCategoryResponseDto();

        Page<Category> categoryPage = new PageImpl<>(
                List.of(firstCategory, secondCategory), pageable, 2);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryResponseDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryResponseDto);

        Page<CategoryResponseDto> expectedPage = new PageImpl<>(
                List.of(firstCategoryResponseDto, secondCategoryResponseDto), pageable, 2);

        Page<CategoryResponseDto> result = categoryService.findAll(pageable);
        //then
        assertEquals(expectedPage, result);
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(firstCategory);
        verify(categoryMapper).toDto(secondCategory);
    }

    @Test
    @DisplayName("getById should return Category with id 1")
    void getById_CategoryWithIdOne_True() {
        //given
        Long id = 1L;
        Category category = createHorrorCategory();
        Optional<Category> optionalCategory = Optional.of(category);
        CategoryResponseDto expectedDto = createHorrorCategoryResponseDto();
        //when
        when(categoryRepository.findById(id)).thenReturn(optionalCategory);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        CategoryResponseDto result = categoryService.getById(id);
        //then
        assertEquals(expectedDto, result);
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("save should return correct response dto")
    void save_CategoryRequestDtoWithIdOne_True() {
        //given
        CategoryRequestDto categoryRequestDto = createHorrorCategoryRequestDto();
        Category category = createHorrorCategory();
        CategoryResponseDto expectedDto = createHorrorCategoryResponseDto();
        //when
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        CategoryResponseDto result = categoryService.save(categoryRequestDto);
        //then
        assertEquals(expectedDto, result);
        verify(categoryMapper).toEntity(categoryRequestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("update should return correct dto")
    void update_CategoryRequestDtoChangeName_True() {
        //given
        Category initCategory = new Category();
        initCategory.setId(1L);
        initCategory.setName("Comedy");
        Category changedCategory = createHorrorCategory();
        CategoryRequestDto categoryRequestDto = createHorrorCategoryRequestDto();
        CategoryResponseDto expectedDto = createHorrorCategoryResponseDto();
        //when
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(initCategory));
        doNothing().when(categoryMapper).updateFromCategoryDto(initCategory, categoryRequestDto);
        when(categoryRepository.save(initCategory)).thenReturn(changedCategory);
        when(categoryMapper.toDto(changedCategory)).thenReturn(expectedDto);
        CategoryResponseDto result = categoryService.update(1L, categoryRequestDto);
        //then
        assertEquals(expectedDto, result);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).updateFromCategoryDto(initCategory, categoryRequestDto);
        verify(categoryRepository).save(initCategory);
        verify(categoryMapper).toDto(changedCategory);
    }

    @Test
    @DisplayName("deleteById should remove correct category")
    void deleteById_CategoryWithIdOne_True() {
        //given
        Long id = 1L;
        //when
        categoryService.deleteById(id);
        //then
        verify(categoryRepository).deleteById(id);
    }
}
