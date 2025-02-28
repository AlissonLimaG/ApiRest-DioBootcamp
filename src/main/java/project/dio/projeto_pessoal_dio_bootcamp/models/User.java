package project.dio.projeto_pessoal_dio_bootcamp.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
@Entity(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @JoinColumn(name = "id_account")
    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @JoinColumn(name = "id_card")
    @OneToOne(cascade = CascadeType.ALL)
    private Card card;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Features> features = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<News> news = new ArrayList<>();
}
