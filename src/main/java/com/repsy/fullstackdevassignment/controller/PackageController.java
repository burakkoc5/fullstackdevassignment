package com.repsy.fullstackdevassignment.controller;

import com.repsy.fullstackdevassignment.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@Tag(name = "Packages", description = "Package deployment and download operations")
public class PackageController {

    private final PackageService packageService;

    @Operation(summary = "Deploy a package", description = "Uploads a .rep package and its meta.json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Package uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping(value = "/{packageName}/{version}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPackage(
            @Parameter(description = "Package name") @PathVariable String packageName,
            @Parameter(description = "Package version") @PathVariable String version,
            @Parameter(description = "Package file (.rep)") @RequestParam("package") MultipartFile packageFile,
            @Parameter(description = "Metadata file (.json)") @RequestParam("meta") MultipartFile metaFile
    ) {
        try {
            packageService.deployPackage(packageName, version, packageFile, metaFile);
            return ResponseEntity.ok("Package uploaded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }


    @Operation(summary = "Download a package file", description = "Downloads the specified package or metadata file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File returned successfully"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Package name") @PathVariable String packageName,
            @Parameter(description = "Package version") @PathVariable String version,
            @Parameter(description = "File name to download") @PathVariable String fileName
    ) {
        try {
            return packageService.downloadPackage(packageName, version, fileName);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}