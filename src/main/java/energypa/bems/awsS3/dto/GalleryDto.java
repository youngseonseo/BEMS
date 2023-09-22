package energypa.bems.awsS3.dto;



import energypa.bems.awsS3.domain.entity.GalleryEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GalleryDto {
    private Long id;
    private String title;
    private String imgFullPath;

    public GalleryEntity toEntity(){
        GalleryEntity build = GalleryEntity.builder()
                .id(id)
                .filePath(imgFullPath)
                .build();
        return build;
    }

    @Builder
    public GalleryDto(Long id, String title, String imgFullPath) {
        this.id = id;
        this.title = title;
        this.imgFullPath = imgFullPath;
    }


}
