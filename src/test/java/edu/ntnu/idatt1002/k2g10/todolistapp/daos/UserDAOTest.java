package edu.ntnu.idatt1002.k2g10.todolistapp.daos;

import edu.ntnu.idatt1002.k2g10.todolistapp.Theme;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Category;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Priority;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.Task;
import edu.ntnu.idatt1002.k2g10.todolistapp.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Persistence;
import javax.transaction.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private final UserDAO userDAO = new UserDAO(
            Persistence.createEntityManagerFactory("pu-todo-derby-test").createEntityManager());
    private User user1;

    @BeforeEach
    void resetUser() {
        user1 = new User("JohnD", "Doe", "John", "john@doe.no", "PASSWORD", Theme.LIGHT);
    }

    @Test
    @DisplayName("User gets created successfully")
    @Transactional
    void testUserGetsCreatedSuccessfully() throws Exception {
        userDAO.create(user1);

        assertTrue(userDAO.exists(user1));
    }

    @Test
    @DisplayName("User data is correct after create")
    @Transactional
    void testUserDataIsCorrectAfterCreate() throws Exception {
        userDAO.create(user1);

        User user = userDAO.get(user1.getUsername()).get();

        boolean firstnameOK = user.getFirstname().equals(user1.getFirstname());
        boolean lastnameOK = user.getLastname().equals(user1.getLastname());
        boolean emailOK = user.getEmail().equals(user1.getEmail());
        boolean passwordOK = user.verifyPassword("PASSWORD");
        boolean themeOK = user.getTheme().equals(user1.getTheme());

        assertTrue(firstnameOK && lastnameOK && emailOK && passwordOK && themeOK);
    }

    @Test
    @DisplayName("User data is correct after update")
    @Transactional
    void testUserDataIsCorrectAfterUpdate() throws Exception {
        userDAO.create(user1);

        String firstName = "Johnny";
        String lastName = "Bravo";
        String email = "JBro@Javo.no";
        String password = "Password1";
        Theme theme = Theme.CONTRAST;

        User user2 = new User(user1.getUsername(), firstName, lastName, email, password, theme);
        user2.setId(user1.getId());
        userDAO.update(user2);

        User user = userDAO.get(user1.getUsername()).get();

        boolean firstnameOK = user.getFirstname().equals(firstName);
        boolean lastnameOK = user.getLastname().equals(lastName);
        boolean emailOK = user.getEmail().equals(email);
        boolean passwordOK = user.verifyPassword(password);
        boolean themeOK = user.getTheme().equals(theme);

        assertTrue(firstnameOK && lastnameOK && emailOK && passwordOK && themeOK);
    }

    @Test
    @DisplayName("User is removed after delete")
    @Transactional
    void testUserIsRemovedAfterDelete() throws Exception {
        userDAO.create(user1);
        userDAO.delete(user1);

        assertFalse(userDAO.exists(user1));
    }

    @Test
    @DisplayName("Task data is correct after create")
    @Transactional
    void testTaskDataIsCorrectAfterCreate() throws Exception {
        userDAO.create(user1);
        User user = userDAO.get(user1.getUsername()).get();
        Task task = new Task("Task test", "FindOutIfTaskIsMade", LocalDate.now(), LocalDate.now(), Priority.HIGH,
                new Category("Test category", 'A'));

        user.getTaskList().getTasks().add(task);
        userDAO.update(user);
        user = userDAO.get(user1.getUsername()).get();
        Task storedTask = user.getTaskList().getTasks().get(0);

        boolean titleOK = task.getTitle().equals(storedTask.getTitle());
        boolean descriptionOK = task.getDescription().equals(storedTask.getDescription());
        boolean startDate = task.getStartTime().equals(storedTask.getStartTime());
        boolean endDate = task.getEndTime().equals(storedTask.getEndTime());
        boolean priorityCheck = task.getPriority().equals(storedTask.getPriority());
        boolean categoryCheck = task.getCategory().equals(storedTask.getCategory());

        assertTrue(titleOK && descriptionOK && startDate && endDate && priorityCheck && categoryCheck);
    }

    @Test
    @DisplayName("Task data is correct after update")
    @Transactional
    void testTaskDataIsCorrectAfterUpdate() throws Exception {
        User user = new User("JohnD", "Doe", "John", "john@doe.no", "PASSWORD", Theme.LIGHT);
        user.getTaskList().getTasks().add(new Task("Task test", "FindOutIfTaskIsMade", LocalDate.now(), LocalDate.now(),
                Priority.HIGH, new Category("Test category", 'A')));
        userDAO.create(user);

        String newTitle = "WOW";
        String newDesc = "owowo";
        LocalDate newDate = LocalDate.now().plusDays(10);
        Priority newPriority = Priority.NONE;

        user = userDAO.get(user1.getUsername()).get();
        Task storedTask = user.getTaskList().getTasks().get(0);

        storedTask.setTitle(newTitle);
        storedTask.setDescription(newDesc);
        storedTask.setStartTime(newDate);
        storedTask.setEndTime(newDate);
        storedTask.setPriority(newPriority);

        userDAO.update(user);
        user = userDAO.get(user.getUsername()).get();
        storedTask = user.getTaskList().getTasks().get(0);

        boolean titleOK = newTitle.equals(storedTask.getTitle());
        boolean descriptionOK = newDesc.equals(storedTask.getDescription());
        boolean startDate = newDate.equals(storedTask.getStartTime());
        boolean endDate = newDate.equals(storedTask.getEndTime());
        boolean priorityCheck = newPriority.equals(storedTask.getPriority());

        assertTrue(titleOK && descriptionOK && startDate && endDate && priorityCheck);
    }

    @Test
    @DisplayName("Category data is correct after create")
    @Transactional
    void testCategoryDataIsCorrectAfterCreate() throws Exception {
        userDAO.create(user1);
        User user = userDAO.get(user1.getUsername()).get();
        Category category = new Category("Test Category", 'T');

        user.getTaskList().getCategories().add(category);

        userDAO.update(user);
        user = userDAO.get(user1.getUsername()).get();

        Category storedCategory = user.getTaskList().getCategories().get(0);

        boolean titleOK = user.getTaskList().getCategories().get(0).getTitle().equals(storedCategory.getTitle());
        boolean iconOK = user.getTaskList().getCategories().get(0).getIcon() == storedCategory.getIcon();

        assertTrue(titleOK && iconOK);
    }

    @Test
    @DisplayName("Category data is correct after update")
    @Transactional
    void testCategoryDataIsCorrectAfterUpdate() throws Exception {
        userDAO.create(user1);
        User user = userDAO.get(user1.getUsername()).get();
        user.getTaskList().getCategories().add(new Category("Test Category", 'T'));

        String newTitle = "WOW";
        char newIcon = 'O';

        Category storedCategory = user.getTaskList().getCategories().get(0);
        storedCategory.setTitle(newTitle);
        storedCategory.setIcon(newIcon);

        userDAO.update(user);
        user = userDAO.get(user.getUsername()).get();
        storedCategory = user.getTaskList().getCategories().get(0);

        boolean titleOK = newTitle.equals(user.getTaskList().getCategories().get(0).getTitle());
        boolean iconOK = (newIcon == user.getTaskList().getCategories().get(0).getIcon());

        assertTrue(titleOK && iconOK);
    }

}
