package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import project.dio.projeto_pessoal_dio_bootcamp.models.Features;

public record FeaturesRecord(Long id, String icon, String description) {

    public FeaturesRecord(Features features){
        this(features.getId(),features.getIcon(),features.getDescription());
    }

    public Features toModel(){
        return new Features(
                this.id,
                this.icon,
                this.description
        );
    }
}
