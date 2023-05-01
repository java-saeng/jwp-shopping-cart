package cart.service;

import cart.dao.AccountDao;
import cart.service.dto.AccountSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountDao accountDao;

    public AccountService(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<AccountSearchResponse> searchAllAccounts() {

        return accountDao.findAll()
                         .stream()
                         .map(entity -> new AccountSearchResponse(
                                 entity.getId(),
                                 entity.getEmail(),
                                 entity.getPassword())
                         )
                         .collect(Collectors.toList());
    }
}