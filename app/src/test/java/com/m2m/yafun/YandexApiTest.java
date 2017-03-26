package com.m2m.yafun;

import com.m2m.yafun.model.api.TranslateApi;
import com.m2m.yafun.model.api.service.result.DetectedLanguage;
import com.m2m.yafun.model.api.service.result.Languages;
import com.m2m.yafun.model.api.service.result.TranslateResult;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class YandexApiTest {

    private TranslateApi api = new TranslateApi(null);

    @Test
    public void getLanguagesTest() throws Exception {
        Languages languages = api.getLanguagesSync("ru");
        assertNotNull(languages);
        assertTrue(languages.getDirections().size() > 100);
        assertTrue(languages.getLanguages().size() > 50);
        assertEquals("Русский", languages.getLanguages().get("ru"));
        assertEquals("Белорусский", languages.getLanguages().get("be"));
        assertEquals("Английский", languages.getLanguages().get("en"));
    }

    @Test
    public void detectLanguagesTest() throws Exception {
        checkLanguage("Detect me, please", "en");
        checkLanguage("Определите меня", "ru");
    }

    private void checkLanguage(String toDetect, String expected) {
        DetectedLanguage detectedLanguage = api.detectedLanguageSync(toDetect);
        assertNotNull(detectedLanguage);
        assertEquals(200, detectedLanguage.getResultCode());
        assertEquals(expected, detectedLanguage.getLanguage());
    }

    @Test
    public void translateTest() throws Exception {
        checkTranslation("Привет", "ru-en", "Hi");
    }

    private void checkTranslation(String toTranslate, String direction, String expected) {
        TranslateResult result = api.translateSync(toTranslate, direction);
        assertNotNull(result);
        assertEquals(200, result.getResultCode());
        assertTrue(result.getTranslatedText().size() > 0);
        assertEquals(expected, result.getTranslatedText().get(0));
    }


}