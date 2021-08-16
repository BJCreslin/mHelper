package ru.zhelper.zhelper.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

class ZakupkiParser615FzTest {
    private static final Logger logger = LoggerFactory.getLogger(ZakupkiParser615FzTest.class);
    private static final String fileName = "206520000012100111.html";
    private static final String fileBadName = "206520000012100111bad.html";
    private static final String UIN = "206520000012100111";
    private static final Integer FZ = 615;
    private ZakupkiParser615Fz parser;
    private String fineHtml;
    private String badHtml;

    @BeforeEach
    private void init() throws IOException, URISyntaxException {
        parser = new ZakupkiParser615Fz();
        fineHtml = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI())));
        badHtml = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(fileBadName).toURI())));
    }

    @Test
    void givenHtml_whenGetUin_getUin() {
        String result = parser.getUin(fineHtml);
        Assertions.assertEquals(result, UIN);
    }

    @Test
    void givenBadHtml_whenGetUin_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getUin(badHtml));
    }

    @Test
    void givenHtml_whenGetStage_getStage() {
        Stage result = parser.getStage(fineHtml);
        Assertions.assertEquals(result, Stage.SUBMISSION_OF_APPLICATION);
    }

    @Test
    void givenBadHtml_whenGetStage_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getStage(badHtml));
    }

    @Test
    void givenHtml_whenGetUin_getFzNumber() {
        Integer result = parser.getFzNumber(fineHtml);
        Assertions.assertEquals(result, FZ);
    }

    @Test
    void givenBadHtml_whenGetUin_getFzNumber() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getFzNumber(badHtml));
    }
}
