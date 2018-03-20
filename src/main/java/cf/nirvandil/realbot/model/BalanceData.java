package cf.nirvandil.realbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
public class BalanceData {
    private double balance;
    private double credit;
    private double sum;
    @Override
    public String toString() {
        return "\n" + "*Баланс: " + balance + "*\n" + "*Кредит: " + credit + "*\n" + "*Суммарно: " + sum + "*\n";
    }
}
