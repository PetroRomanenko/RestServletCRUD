import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ferros.model.Event;
import com.ferros.model.File;
import com.ferros.model.User;
import com.ferros.repository.EventRepository;
import com.ferros.repository.FileRepository;
import com.ferros.repository.UserRepository;
import com.ferros.service.EventService;
import java.util.ArrayList;
import java.util.List;

public class EventServiceTest {

    private EventService eventService;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;

    @BeforeEach
    public void setUp() {
        fileRepository = mock(FileRepository.class);
        eventRepository = mock(EventRepository.class);
        userRepository = mock(UserRepository.class);
        eventService = new EventService(fileRepository, userRepository, eventRepository);
    }

    @Test
    public void testSaveEvent() {
        Event eventToCreate = new Event();
        when(eventRepository.save(eventToCreate)).thenReturn(eventToCreate);

        Event savedEvent = eventService.save(eventToCreate);

        verify(eventRepository, times(1)).save(eventToCreate);
        assertEquals(eventToCreate, savedEvent);
    }

    @Test
    public void testGetAllEvents() {
        List<Event> eventList = new ArrayList<>();
        when(eventRepository.getAll()).thenReturn(eventList);

        List<Event> resultEventList = eventService.getAllEvents();

        verify(eventRepository, times(1)).getAll();
        assertEquals(eventList, resultEventList);
    }

    @Test
    public void testGetEventById() {
        Event event = new Event();
        when(eventRepository.getById(1)).thenReturn(event);

        Event resultEvent = eventService.getById(1);

        verify(eventRepository, times(1)).getById(1);
        assertEquals(event, resultEvent);
    }

    @Test
    public void testCreateUploadEvent() {
        User user = new User();
        File fileToSave = new File();
        Event newUploadEvent = new Event();
        when(eventRepository.save(newUploadEvent)).thenReturn(newUploadEvent);

        Event resultEvent = eventService.createUploadEvent(user, fileToSave);

        verify(eventRepository, times(1)).save(newUploadEvent);
        assertEquals(newUploadEvent, resultEvent);
    }

    @Test
    @Ignore
    public void testUpdateEvent() {
        User user = new User();
        File file = new File();
        Event event = new Event(1, user, file);
        when(userRepository.getById(1)).thenReturn(user);
        when(fileRepository.getById(1)).thenReturn(file);
        when(eventRepository.update(event)).thenReturn(event);

        Event resultEvent = eventService.upateEvent(1, 1, 1);

        verify(userRepository, times(1)).getById(1);
        verify(fileRepository, times(1)).getById(1);
        verify(eventRepository, times(1)).update(event);
        assertEquals(event, resultEvent);
    }

    @Test
    public void testDeleteUser() {
        eventService.deleteUser(1);
        verify(eventRepository, times(1)).deleteById(1);
    }
}