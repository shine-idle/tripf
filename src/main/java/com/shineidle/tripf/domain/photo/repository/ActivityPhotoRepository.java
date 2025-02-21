package com.shineidle.tripf.domain.photo.repository;

import com.shineidle.tripf.domain.photo.entity.ActivityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto, Long> {
    /**
     * 활동Id와 사진Id에 해당하는 활동사진 반환
     *
     * @param activityId 활동 식별자
     * @param photoId    사진 식별자
     * @return {@link ActivityPhoto} 활동 사진
     */
    Optional<ActivityPhoto> findByActivityIdAndPhotoId(Long activityId, Long photoId);

    /**
     * 활동Id에 해당하는 모든 활동사진을 List 형태로 반환
     *
     * @param activityId 활동 식별자
     * @return {@link ActivityPhoto} 활동사진 리스트
     */
    List<ActivityPhoto> findAllByActivityId(Long activityId);
}