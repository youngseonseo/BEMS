package energypa.bems.awsS3.controller;

import java.util.List;
import energypa.bems.awsS3.dto.GalleryDto;
import energypa.bems.awsS3.service.GalleryService;
import energypa.bems.awsS3.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static energypa.bems.awsS3.service.S3Service.CLOUD_FRONT_DOMAIN_NAME;


@Slf4j
@RestController
@AllArgsConstructor
@Tag(name="gallery", description = "커뮤니티 이미지 저장 api")
@RequestMapping("/api")
public class GalleryController {
    private S3Service s3Service;
    private GalleryService galleryService;

    @Operation(method = "get", summary = "모든 이미지 조회")
    @ApiResponses(value=
    @ApiResponse(responseCode = "200", description = "이미지 조회 성공")
    )
    @GetMapping("/gallery")
    public  List<GalleryDto> dispWrite() {
        List<GalleryDto> galleryDtoList = galleryService.getList();
        return galleryDtoList;
    }

    @PostMapping(path= "/gallery", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method = "post", summary = "이미지 등록")
    @ApiResponse(responseCode = "200", description = "이미지 등록 성공")
    public ResponseEntity<Long> execWrite(@RequestPart(value="file",required = false)  MultipartFile file) throws IOException {

        GalleryDto galleryDto = new GalleryDto();
        String imgPath = s3Service.upload(file);

        galleryDto.setImgFullPath(CLOUD_FRONT_DOMAIN_NAME+"/"+imgPath);

        return ResponseEntity.ok(galleryService.saveMember(galleryDto));
    }


    @Operation(method="get", summary = "해당 이미지 조회")
    @ApiResponses(value=
    @ApiResponse(responseCode = "200", description = "해당 이미지 조회 성공")
    )
    @GetMapping("/gallery/{galleryId}")
    public GalleryDto read(@PathVariable Long galleryId){
        GalleryDto read = galleryService.read(galleryId);
        return read;
    }

    @Operation(method="delete", summary = "이미지 삭제")
    @ApiResponses(value=
    @ApiResponse(responseCode = "200", description = "이미지 삭제 성공")
    )
    @DeleteMapping("/gallery/{galleryId}")
    public ResponseEntity<?> delete(@PathVariable Long galleryId){
        galleryService.delete(galleryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

