package org.qatracker.lesson_17;

import org.qatracker.model.Priority;
import org.qatracker.model.TestCase;
import org.qatracker.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCaseServiceWithMockitoTest {

    @Mock
    Repository<TestCase, Integer> repository; // Mockito создаст mock автоматически

    @Test
    void findAll_shouldDelegateToRepository() {
        // Arrange: задаем поведение mock-объекта
        TestCase tc = new TestCase("Login", Priority.CRITICAL);
        when(repository.findAll()).thenReturn(List.of(tc)); // stub-поведение

        // Act
        List<TestCase> result = repository.findAll();

        // Assert
        assertEquals(1, result.size());
        // Проверяем, что метод findAll() вызван 1 раз
        verify(repository, times(1)).findAll();

    }

    @Test
    void save_shouldThrowWhenRepositoryFails() throws Exception {
        // Настраиваем мок бросать исключение
        doThrow(new IOException("Disk full"))
                .when(repository).save(any(TestCase.class));

        assertThrows(IOException.class, () -> repository.save(new TestCase("Test", Priority.LOW)));
    }
}
