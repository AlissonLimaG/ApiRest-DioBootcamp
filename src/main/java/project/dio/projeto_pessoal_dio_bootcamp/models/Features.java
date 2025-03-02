package project.dio.projeto_pessoal_dio_bootcamp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity(name = "tb_features")
public class Features extends BaseItem{

    public Features(Long id, String icon, String description){
        super(id,icon,description);
    }
//    public Features(Long id, String icon, String description) {
//        this.id = id;
//        this.icon = icon;
//        this.description = description;
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String icon;
//
//    private String description;
//
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
//    private User user;

}
