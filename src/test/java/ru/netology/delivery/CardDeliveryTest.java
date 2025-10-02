package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        // Добавляем листенер в тестовый класс перед выполнением всех тестов
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        // Удаляем листенер после выполнения всех тестов
        SelenideLogger.removeListener("allure");
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting, "dd.MM.yyyy");
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting, "dd.MM.yyyy");

         Allure.step("Заполнение полей формы",() ->{
            $("[data-test-id='city'] input").setValue(validUser.getCity());
           Allure.attachment("Город", validUser.getCity().toString());
            $("[data-test-id='date'] .input__control")
                    .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                    .setValue(firstMeetingDate);
            $("[data-test-id='name'] input").setValue(validUser.getName());
            $("[data-test-id='phone'] input").setValue(validUser.getPhone());
            $("[data-test-id='agreement']").click();
        });

        $(".button_theme_alfa-on-white").click();

        $("[data-test-id='success-notification'] .notification__title")
                .shouldHave(Condition.text("Успешно!"), Duration.ofSeconds(15))
                .shouldBe(visible);
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);

        $("[data-test-id='date'] .input__control")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(secondMeetingDate);
        $(".button_theme_alfa-on-white").click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] .button").click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldHave(Condition.text("Успешно!"), Duration.ofSeconds(15))
                .shouldBe(visible);
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(visible);
    }
}
