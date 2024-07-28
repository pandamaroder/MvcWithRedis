package com.example.demo;

import com.example.demo.dto.CategoryCreateRequest;
import com.example.demo.dto.CategoryCreateResponse;
import com.example.demo.dto.CategoryDto;
import com.example.demo.helpers.DataHelper;
import com.example.demo.model.Category;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryServiceTests extends TestBase {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createNewCategory() {
        final String categoryName = DataHelper.getAlphabeticString(10);

        final int countCategoryBefore = getEntriesCount(TABLE_CATEGORIES);
        final CategoryCreateResponse testCategory = createCategory(categoryName);
        final int countCategoryAfter = getEntriesCount(TABLE_CATEGORIES);

        assertThat(countCategoryAfter - countCategoryBefore)
            .isOne();

        assertThat(testCategory)
            .isNotNull();
        assertThat(testCategory.id())
            .isPositive();
        assertThat(testCategory.name())
            .isEqualTo(categoryName);
    }

    @Test
    void deleteCategory() {
        final String name = UUID.randomUUID().toString();
        createCategory(DataHelper.getAlphabeticString(10));
        createCategory(DataHelper.getAlphabeticString(10));
        final CategoryCreateResponse testCategory = createCategory(name);

        final int countCategoryBefore = getEntriesCount(TABLE_CATEGORIES);
        final CategoryDto categoryDto = categoryService.deleteCategory(testCategory.id());
        final int countCategoryAfter = getEntriesCount(TABLE_CATEGORIES);
        assertThat(countCategoryBefore)
            .as("Verification that there are some entities before deletion in the table")
            .isGreaterThan(1);
        assertThat(countCategoryBefore - countCategoryAfter)
            .isOne();
        assertThat(categoryDto)
            .isNotNull();
        assertThat(categoryDto.id())
            .isPositive();
        assertThat(categoryDto.name())
            .isEqualTo(name);
    }

    @Test
    void updateCategory() {
        final CategoryCreateResponse testCategory = createCategory(DataHelper.getAlphabeticString(10));
        final int countCategoryBefore = getEntriesCount(
            TABLE_CATEGORIES);
        final String updatedCategoryName = DataHelper.getAlphabeticString(5);
        final CategoryDto updatedCategory = new CategoryDto(testCategory.id(), updatedCategoryName);
        final CategoryDto categoryDtoAfterUpdate = categoryService.updateCategory(updatedCategory);
        final int countCategoryAfter = getEntriesCount(TABLE_CATEGORIES);

        assertThat(countCategoryAfter - countCategoryBefore)
            .isZero();
        assertThat(testCategory)
            .isNotNull();
        assertThat(categoryDtoAfterUpdate.id())
            .isPositive();
        assertThat(categoryDtoAfterUpdate.name())
            .isEqualTo(updatedCategoryName.trim()
                .toLowerCase(Locale.ROOT));
        assertThat(categoryDtoAfterUpdate.name())
            .isEqualTo(updatedCategoryName);
    }

    @Test
    void testCreateIfNeedCategoryInTransaction() {
        final String categoryName = "testcategory";
        assertThat(categoryRepository.findByName(categoryName))
            .isNotPresent();
        //TODO на 22 рефактор
        final Category category = transactionTemplate.execute(status ->
            categoryService.createIfNeedCategory(categoryName)
        );
        assertThat(categoryRepository.findByName(categoryName))
            .isNotNull();
        assertThat(category)
            .isNotNull();
        assertThat(category.getId())
            .isPositive();
        assertThat(category.getName())
            .isEqualTo(categoryName);
    }

    @Test
    void testCreateIfNeedCategoryWithoutTransaction() {
        final String categoryName = "test";

        assertThatThrownBy(() -> categoryService
            .createIfNeedCategory(categoryName))
            .isInstanceOf(IllegalTransactionStateException.class)
            .hasMessage("No existing transaction found for transaction marked with propagation 'mandatory'");
    }

    /*@Test
    void deleteCategoryWithNews() {
        final String categoryName = DataHelper.getAlphabeticString(10);
        final CategoryCreateResponse testCategory = createCategory(categoryName);
        prepareNewsWithUsers(categoryName);
        final int countNewsBefore = getEntriesCount(TABLE_NAME_NEWS);
        final int countCategoryBefore = getEntriesCount(TABLE_NAME_CATEGORIES);
        final CategoryDto categoryDto = categoryService.deleteCategory(testCategory.id());
        final int countCategoryAfter = getEntriesCount(TABLE_NAME_CATEGORIES);
        final int countNewsAfter = getEntriesCount(TABLE_NAME_NEWS);

        assertThat(countCategoryBefore - countCategoryAfter)
            .isOne();
        assertThat(categoryDto)
            .isNotNull();
        assertThat(categoryDto.id())
            .isPositive();
        assertThat(categoryDto.name())
            .isEqualTo(categoryName);

        assertThat(countNewsBefore)
            .isEqualTo(2);
        assertThat(countNewsAfter)
            .isZero();

    }*/

    private CategoryCreateResponse createCategory(final String categoryName) {
        return categoryService
            .createCategory(new CategoryCreateRequest(categoryName));
    }
}
