package org.qatracker.lesson_17;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.qatracker.model.Priority;
import org.qatracker.model.TestCase;
import org.qatracker.repository.Repository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // подключаем расширение JUnit для Mockito
public class MockitoExampleTest {

    @Mock
    Repository<TestCase, Integer> repo; // Mockito создаёт mock автоматически

    @Test
    void basicMockito_example() {
        // ── Задаём поведение: when().thenReturn() ────────────────────────────
        TestCase tc = new TestCase("Mocked test", Priority.HIGH);

        // "когда вызовут findAll() — вернуть этот список"
        when(repo.findAll()).thenReturn(List.of(tc));

        // Вызываем
        List<TestCase> result = repo.findAll();

        // Проверяем результат
        assertEquals(1, result.size());
        assertEquals("Mocked test", result.get(0).getTitle());

        // ── Проверяем вызов: verify() ─────────────────────────────────────────
        verify(repo, times(1)).findAll(); // findAll() вызван ровно 1 раз
    }

    @Test
    void mockito_throwException() throws IOException {
        // "когда вызовут save() с любым TestCase — бросить исключение"
        doThrow(new IOException("Disk full"))
                .when(repo).save(any(TestCase.class));

        assertThrows(IOException.class,
                () -> repo.save(new TestCase("Test", Priority.LOW))
        );
    }

    @Test
    void mockito_verifyNoInteraction() {
        // Проверяем что метод НЕ вызывался
        verify(repo, never()).delete(anyInt());
    }

}
