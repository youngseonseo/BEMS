package energypa.bems.awsS3.repository;


import energypa.bems.awsS3.domain.GalleryEntity;
import energypa.bems.login.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
    @Override
    List<GalleryEntity> findAll();

    Optional<GalleryEntity> findById(Long id);

    @Transactional
    @Modifying
    @Query("update gallery p set p.member = :member where p.id = :galleryId")
    int updateGallery(@Param("galleryId") Long galleryId, @Param("member") Member member);

}
