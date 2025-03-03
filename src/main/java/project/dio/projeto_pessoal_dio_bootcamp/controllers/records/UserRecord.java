package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import project.dio.projeto_pessoal_dio_bootcamp.models.Account;
import project.dio.projeto_pessoal_dio_bootcamp.models.Features;
import project.dio.projeto_pessoal_dio_bootcamp.models.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public record UserRecord(
        @NotEmpty(message = "Name not be empty")
        @NotNull(message = "Name not be null")
        String name,
        @NotEmpty(message = "Username not be empty")
        @NotNull(message = "Username not be null")
        String username,
        @Size(min = 3, message = "The password must be greater than 3 characters.")
        @NotEmpty(message = "Password not be empty")
        @NotNull(message = "Password not be null")
        String password,
        @Valid
        @NotNull(message = "Account not be null")
        AccountRecord account,
        CardRecord card,
        List<FeaturesRecord> features,
        List<NewsRecord> news) {

    public UserRecord(User user){
        this(
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                Optional.ofNullable(user.getAccount()).map(AccountRecord::new).orElse(null),
                Optional.ofNullable(user.getCard()).map(CardRecord::new).orElse(null),
                Optional.ofNullable(user.getFeatures()).orElse(Collections.emptyList()).stream().map(FeaturesRecord::new).collect(toList()),
                Optional.ofNullable(user.getNews()).orElse(Collections.emptyList()).stream().map(NewsRecord::new).collect(toList())
        );
    }

    public User toModel(){
        User user = new User();
        user.setName(this.name);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setAccount(Optional.ofNullable(this.account).map(AccountRecord::toModel).orElse(null));
        user.setCard(Optional.ofNullable(this.card).map(CardRecord::toModel).orElse(null));
        user.setNews(Optional.ofNullable(this.news).orElse(Collections.emptyList()).stream().map(NewsRecord::toModel).collect(toList()));
        user.setFeatures(Optional.ofNullable(this.features).orElse(Collections.emptyList()).stream().map(FeaturesRecord::toModel).collect(toList()));
        return user;
    }
}
