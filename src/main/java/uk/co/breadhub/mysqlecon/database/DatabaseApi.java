package uk.co.breadhub.mysqlecon.database;

import java.util.UUID;

public interface DatabaseApi {

    void connect();

    void disconnect();

    void init();

    int userBalance(UUID uniqueId);

    void userDeposit(UUID uniqueId, int amount);

    void userWithdraw(UUID uniqueId, int amount);

    void adminDeposit(UUID uniqueId, int amount);

    void adminWithdraw(UUID uniqueId, int amount);

    void adminSet(UUID uniqueId, int amount);
}
