package via.pro3;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Test
    public void testGetAllAnimals() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setOrigin("Africa");
        animal.setDate(LocalDate.now());

        Mockito.when(animalService.getAllAnimals()).thenReturn(List.of(animal));

        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origin").value("Africa"));
    }

    @Test
    public void testGetAnimalById() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setOrigin("Africa");
        animal.setDate(LocalDate.now());

        Mockito.when(animalService.getAnimalById(1L)).thenReturn(Optional.of(animal));

        mockMvc.perform(get("/animals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin").value("Africa"));
    }

    @Test
    public void testSaveAnimal() throws Exception {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setOrigin("Africa");
        animal.setDate(LocalDate.now());

        Mockito.when(animalService.saveAnimal(Mockito.any(Animal.class))).thenReturn(animal);

        mockMvc.perform(post("/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"origin\":\"Africa\",\"date\":\"2023-10-10\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin").value("Africa"));
    }

    @Test
    public void testDeleteAnimal() throws Exception {
        mockMvc.perform(delete("/animals/1"))
                .andExpect(status().isNoContent());
    }
}
