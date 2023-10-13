package energypa.bems.awsS3.dto;



import energypa.bems.awsS3.domain.GalleryEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GalleryDto {
    private Long id;
    private String title;
    private String imgFullPath;

    public GalleryEntity toEntity(){
        return GalleryEntity.builder()
                .id(id)
                .filePath(imgFullPath)
                .build();
    }


}
