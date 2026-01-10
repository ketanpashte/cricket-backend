package com.fastx.live_score.modules.file;

import com.fastx.live_score.core.utils.AppResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/files")
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "API for uploading files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a file (Image)")
    public AppResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.storeFile(file);
        return AppResponse.success(fileUrl);
    }
}
