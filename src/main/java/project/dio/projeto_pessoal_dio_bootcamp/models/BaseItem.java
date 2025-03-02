package project.dio.projeto_pessoal_dio_bootcamp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseItem {

    public BaseItem(Long id, String icon, String description) {
        this.id = id;
        this.icon = icon;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String icon;
    
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    private User user;

}
