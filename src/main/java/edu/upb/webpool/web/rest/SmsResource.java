package edu.upb.webpool.web.rest;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.Sms;
import edu.upb.webpool.domain.User;
import edu.upb.webpool.domain.WebRtc;
import edu.upb.webpool.repository.SmsRepository;
import edu.upb.webpool.repository.UserRepository;
import edu.upb.webpool.repository.WebRtcRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Random;

/**
 * REST controller for managing {@link Pool}.
 */
@RestController
@RequestMapping("/api")
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmsRepository repository;

    @Autowired
    private UserRepository userRepository;

    public SmsResource(SmsRepository repository) {
        this.repository = repository;
    }


    @Value("${twilio.sid}")
    private String twilioUsername;

    @Value("${twilio.token}")
    private String twilioPassword;

    @Value("${twilio.number}")
    private String twilioNumber;

    @GetMapping("/sms/{pool}/{user}")
    public void save(@PathVariable(value = "pool", required = false) final String pool, @PathVariable(value = "user", required = false) final String user) throws URISyntaxException, ServletException, IOException {
        repository.deleteByPoolAndOwner(pool, user);

        List<User> results = userRepository.findByEmailIgnoreCase(user);
        String phone  = results.isEmpty() ? ""  : results.get(0).getPhone();

        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);

        System.out.println("VALIDATE SMS: " + randomNumber);

        if(phone != null && false) {

            Twilio.init(
                twilioUsername,
                twilioPassword);
            Message message = Message.creator(
                    new PhoneNumber(phone),
                    new PhoneNumber(twilioNumber),
                    "Validation for WebPool: " + String.valueOf(randomNumber))
                .create();
        }

        repository.save(new Sms()
            .owner(user)
            .pool(pool)
            .data(String.valueOf(randomNumber))
            .date(Instant.now())
        );
    }

}
