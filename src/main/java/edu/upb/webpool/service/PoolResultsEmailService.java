package edu.upb.webpool.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upb.webpool.domain.Pool;
import edu.upb.webpool.domain.PoolEntry;
import edu.upb.webpool.repository.PoolEntryRepository;
import edu.upb.webpool.repository.PoolRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

@Service
public class PoolResultsEmailService {

    private static final Logger log = LoggerFactory.getLogger(PoolResultsEmailService.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
        .ofPattern("dd MMMM yyyy, HH:mm", Locale.ENGLISH)
        .withZone(ZoneId.of("Europe/Bucharest"));

    private final PoolRepository poolRepository;
    private final PoolEntryRepository poolEntryRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final JHipsterProperties jHipsterProperties;
    private final ObjectMapper objectMapper;

    public PoolResultsEmailService(
        PoolRepository poolRepository,
        PoolEntryRepository poolEntryRepository,
        JavaMailSender mailSender,
        SpringTemplateEngine templateEngine,
        JHipsterProperties jHipsterProperties,
        ObjectMapper objectMapper
    ) {
        this.poolRepository = poolRepository;
        this.poolEntryRepository = poolEntryRepository;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.jHipsterProperties = jHipsterProperties;
        this.objectMapper = objectMapper;
    }

    public synchronized Pool sendResults(Pool pool) {
        List<PoolEntry> entries = poolEntryRepository.findByPool(pool.getId());
        Set<String> recipients = new LinkedHashSet<>();
        if (pool.getUsers() != null) {
            pool.getUsers().stream().filter(this::isEmail).map(String::toLowerCase).forEach(recipients::add);
        }
        entries.stream().map(PoolEntry::getOwner).filter(this::isEmail).map(String::toLowerCase).forEach(recipients::add);
        if (isEmail(pool.getOwner())) {
            recipients.add(pool.getOwner().toLowerCase());
        }

        Map<String, ResultRow> rows = new LinkedHashMap<>();
        if (pool.getOptions() != null) {
            pool.getOptions().forEach(option -> rows.put(option, new ResultRow(option)));
        }
        List<TextAnswer> textAnswers = new ArrayList<>();
        for (PoolEntry entry : entries) {
            collectEntry(entry, rows, textAnswers);
        }

        Context context = new Context(Locale.ENGLISH);
        context.setVariable("pool", pool);
        context.setVariable("results", new ArrayList<>(rows.values()));
        context.setVariable("textAnswers", textAnswers);
        context.setVariable("totalResponses", entries.size());
        context.setVariable("generatedAt", DATE_FORMAT.format(Instant.now()));
        context.setVariable("baseUrl", jHipsterProperties.getMail().getBaseUrl());
        String html = templateEngine.process("mail/poolResultsEmail", context);

        if (!recipients.isEmpty()) {
            sendEmail(recipients, "Results: " + pool.getName(), html);
        }

        pool.setResultsSentAt(Instant.now());
        Pool saved = poolRepository.save(pool);
        log.info("Sent results for poll {} to {} recipients", pool.getId(), recipients.size());
        return saved;
    }

    private void collectEntry(PoolEntry entry, Map<String, ResultRow> rows, List<TextAnswer> textAnswers) {
        String raw = entry.getOptionValue() != null ? entry.getOptionValue() : entry.getOption();
        if (raw == null || raw.trim().isEmpty()) {
            return;
        }
        try {
            Map<String, Object> values = objectMapper.readValue(raw, new TypeReference<Map<String, Object>>() {});
            if (values.size() == 1 && values.containsKey("raspuns")) {
                textAnswers.add(new TextAnswer(entry.getOwner(), String.valueOf(values.get("raspuns"))));
                return;
            }
            values.forEach((option, value) -> rows.computeIfAbsent(option, ResultRow::new).add(String.valueOf(value)));
        } catch (Exception ignored) {
            textAnswers.add(new TextAnswer(entry.getOwner(), raw));
        }
    }

    private void sendEmail(Set<String> recipients, String subject, String html) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            List<String> addresses = new ArrayList<>(recipients);
            helper.setTo(addresses.get(0));
            if (addresses.size() > 1) {
                helper.setBcc(addresses.subList(1, addresses.size()).toArray(new String[0]));
            }
            helper.setFrom(jHipsterProperties.getMail().getFrom());
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MailException | MessagingException exception) {
            throw new IllegalStateException("Could not email poll results", exception);
        }
    }

    private boolean isEmail(String value) {
        return value != null && value.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public static final class ResultRow {
        private final String option;
        private int yes;
        private int maybe;
        private int no;

        ResultRow(String option) {
            this.option = option;
        }

        void add(String value) {
            String normalized = value == null ? "" : value.trim().toLowerCase();
            if (Set.of("poate", "maybe").contains(normalized)) {
                maybe++;
            } else if (Set.of("nu", "no", "unavailable").contains(normalized)) {
                no++;
            } else if (!normalized.isEmpty() && !"null".equals(normalized) && !"undefined".equals(normalized)) {
                yes++;
            }
        }

        public String getOption() { return option; }
        public int getYes() { return yes; }
        public int getMaybe() { return maybe; }
        public int getNo() { return no; }
        public int getTotal() { return yes + maybe + no; }
    }

    public static final class TextAnswer {
        private final String participant;
        private final String answer;

        TextAnswer(String participant, String answer) {
            this.participant = participant;
            this.answer = answer;
        }

        public String getParticipant() { return participant; }
        public String getAnswer() { return answer; }
    }
}
