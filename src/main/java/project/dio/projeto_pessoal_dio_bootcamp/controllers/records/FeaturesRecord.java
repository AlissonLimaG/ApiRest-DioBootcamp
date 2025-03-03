package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import project.dio.projeto_pessoal_dio_bootcamp.models.Features;

public record FeaturesRecord(String icon, String description) {

    public FeaturesRecord(Features features){
        this(features.getIcon(),features.getDescription());
    }

    public Features toModel(){
        return new Features(
                null,
                this.icon,
                this.description
        );
    }
}
