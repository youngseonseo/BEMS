package energypa.bems.awsS3.service;


import energypa.bems.awsS3.domain.GalleryEntity;
import energypa.bems.awsS3.repository.GalleryRepository;
import energypa.bems.awsS3.dto.GalleryDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class GalleryService {

    private GalleryRepository galleryRepository;

    public String saveMember(GalleryDto galleryDto) {
        GalleryEntity save = galleryRepository.save(galleryDto.toEntity());
        String imageUrl = save.getFilePath();
        return imageUrl;
    }

    public List<GalleryDto> getList() {
        List<GalleryEntity> galleryEntityList = galleryRepository.findAll();
        List<GalleryDto> galleryDtoList = new ArrayList<>();

        for (GalleryEntity galleryEntity : galleryEntityList) {
            galleryDtoList.add(convertEntityToDto(galleryEntity));
        }

        return galleryDtoList;
    }

    private GalleryDto convertEntityToDto(GalleryEntity galleryEntity) {
        return GalleryDto.builder()
                .id(galleryEntity.getId())
                .imgFullPath(galleryEntity.getFilePath())
                .build();
    }

    @Transactional
    public void delete(Long galleryId) {
        GalleryEntity gallery = galleryRepository.findById(galleryId).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다. id=" + galleryId));

        galleryRepository.delete(gallery);
    }

    @Transactional
    public GalleryDto read(Long galleryId) {
        GalleryEntity gallery = galleryRepository.findById(galleryId).orElseThrow(() ->
                new IllegalArgumentException("해당 이미지가 존재하지 않습니다. id=" + galleryId));

        GalleryDto galleryDto = convertEntityToDto(gallery);
        return galleryDto;
    }
}
