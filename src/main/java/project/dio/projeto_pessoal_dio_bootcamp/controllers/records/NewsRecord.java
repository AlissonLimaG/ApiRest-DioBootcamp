package project.dio.projeto_pessoal_dio_bootcamp.controllers.records;

import project.dio.projeto_pessoal_dio_bootcamp.models.News;

public record NewsRecord(String icon, String description) {

    public NewsRecord(News news){
        this(news.getIcon(),news.getDescription());
    }

    public News toModel() {
        return new News(
                null,
                this.icon,
                this.description
        );
    }

}
