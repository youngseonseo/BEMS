package energypa.bems.awsS3.domain.repository;


import energypa.bems.awsS3.domain.entity.GalleryEntity;
import energypa.bems.login.domain.Member;
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

    @Modifying
    @Query("update gallery p set p.member = :member where p.id = :id")
    int updatePosts(@Param("id") Long id,@Param("member") Member member);

}
