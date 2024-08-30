package webeid.example.controller;

import webeid.example.service.SigningService;
import webeid.example.dto.CertificateDTO;

import webeid.example.dto.FileDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping("sign")
public class SigningController {

    private final SigningService signingService;

    public SigningController(SigningService signingService) {
        this.signingService = signingService;
    }

    @PostMapping("/")
    public FileDTO prepareAndSign(@RequestBody CertificateDTO data) throws CertificateException, NoSuchAlgorithmException, IOException {
        return signingService.prepareContainer(data);
    }

}
