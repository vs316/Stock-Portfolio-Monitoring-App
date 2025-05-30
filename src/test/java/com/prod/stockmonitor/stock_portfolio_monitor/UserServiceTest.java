package test.java.com.prod.stockmonitor.stock_portfolio_monitor;

import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService s1;

    @Test
    void testRegisterUser() {
        UserClass user = new UserClass();
        user.setUsername("John124");
        user.setEmailId("john@gmail.com");
        user.setFullName("John Watkins");
        user.setPassword("7657");
        user.setRole("user");
        UserClass savedUser = s1.registerUser(user);
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("John124", savedUser.getUsername());
        UserClass user1 = new UserClass();
        user1.setUsername("Darren");
        user1.setEmailId("darren.doe@gmail.com");
        user1.setFullName("Darren Carry");
        user1.setPassword("9087");
        user1.setRole("user");
        UserClass savedUser1 = s1.registerUser(user1);
        Assertions.assertNotNull(savedUser1.getId());
        Assertions.assertEquals("Darren", savedUser1.getUsername());
    }

}

