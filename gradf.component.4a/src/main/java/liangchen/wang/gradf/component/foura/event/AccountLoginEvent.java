package liangchen.wang.gradf.component.foura.event;

import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;
import org.springframework.context.ApplicationEvent;

public class AccountLoginEvent extends ApplicationEvent {
    private final AccountLoginResultDomain account;

    public AccountLoginEvent(Object source, AccountLoginResultDomain account) {
        super(source);
        this.account = account;
    }

    public AccountLoginResultDomain getAccount() {
        return account;
    }
}
