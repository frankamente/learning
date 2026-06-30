package learning;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void whenAddShouldReturnNewMoneyWithIncreasedAmount() {
        BigDecimal initialAmount = BigDecimal.valueOf(5);
        String initialCurrency = "EUR";
        var money = new Money(initialAmount, initialCurrency);
        var moneyToAdd = new Money(BigDecimal.ONE, initialCurrency);

        Money result = money.add(moneyToAdd);

        assertThat(result.amount()).isGreaterThan(initialAmount);
    }

    @Test
    void whenAddWithDifferentCurrencyShouldThrowIllegalArgumentException() {
        BigDecimal initialAmount = BigDecimal.valueOf(5);
        String initialCurrency = "EUR";
        var money = new Money(initialAmount, initialCurrency);
        var moneyToAdd = new Money(BigDecimal.ONE, "USD");

        assertThatThrownBy(() -> money.add(moneyToAdd)).isInstanceOf(IllegalArgumentException.class).hasMessage(Money.CURRENCY_NOT_THE_SAME_MESSAGE);
    }

    @Test
    void whenAddWithNegativeAmountShouldThrowIllegalArgumentException() {
        BigDecimal initialAmount = BigDecimal.valueOf(5);
        String initialCurrency = "EUR";
        var money = new Money(initialAmount, initialCurrency);
        var moneyToAdd = new Money(BigDecimal.valueOf(-5), initialCurrency);

        assertThatThrownBy(() -> money.add(moneyToAdd)).isInstanceOf(IllegalArgumentException.class).hasMessage(Money.CANT_ADD_WITHOUT_POSITIVE_AMOUNT_MESSAGE);
    }

    @Test
    void whenCurrencyHasMoreOrLessThanThreeDigitShouldThrowIllegalArgumentExceptionInConstructor() {

        String CURRENCY_WITH_LESS_THAN_THREE_DIGIT = "U";
        String CURRENCY_WITH_MORE_THAN_THREE_DIGIT = "EURO";
        assertThatThrownBy(() -> new Money(BigDecimal.ONE, CURRENCY_WITH_LESS_THAN_THREE_DIGIT)).isInstanceOf(IllegalArgumentException.class).hasMessage(Money.CURRENCY_WITH_LESS_THAN_THREE_DIGIT_MESSAGE);
        assertThatThrownBy(() -> new Money(BigDecimal.ONE, CURRENCY_WITH_MORE_THAN_THREE_DIGIT)).isInstanceOf(IllegalArgumentException.class).hasMessage(Money.CURRENCY_WITH_MORE_THAN_THREE_DIGIT_MESSAGE);
    }
}
