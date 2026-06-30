package learning;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public static final String CURRENCY_NOT_THE_SAME_MESSAGE = "Currency is not the same.";
    public static final String CANT_ADD_WITHOUT_POSITIVE_AMOUNT_MESSAGE = "Amount should be positive to add";
    public static final String CURRENCY_WITH_MORE_THAN_THREE_DIGIT_MESSAGE = "Currency cant have more than three digit.";
    public static final String CURRENCY_WITH_LESS_THAN_THREE_DIGIT_MESSAGE = "Currency cant have less than three digit.";

    public Money {
        if (currency.length() > 3) {
            throw new IllegalArgumentException(CURRENCY_WITH_MORE_THAN_THREE_DIGIT_MESSAGE);
        }
        if (currency.length() < 3) {
            throw new IllegalArgumentException(CURRENCY_WITH_LESS_THAN_THREE_DIGIT_MESSAGE);
        }
    }

    public Money add(Money moneyToAdd) {
        if (!isSameCurrency(moneyToAdd.currency)) {
            throw new IllegalArgumentException(CURRENCY_NOT_THE_SAME_MESSAGE);
        }
        if (!canAdd(moneyToAdd.amount)) {
            throw new IllegalArgumentException(CANT_ADD_WITHOUT_POSITIVE_AMOUNT_MESSAGE);
        }
        return new Money(this.amount.add(moneyToAdd.amount), this.currency);
    }

    public Money subtract(Money moneyToSubtract) {
        if (!isSameCurrency(moneyToSubtract.currency)) {
            throw new IllegalArgumentException(CURRENCY_NOT_THE_SAME_MESSAGE);
        }
        return new Money(this.amount.subtract(moneyToSubtract.amount), this.currency);
    }

    private boolean canAdd(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    private boolean isSameCurrency(String currency) {
        return this.currency.equals(currency);
    }
}
