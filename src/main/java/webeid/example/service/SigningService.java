
package webeid.example.service;

import webeid.example.dto.CertificateDTO;
import webeid.example.dto.DigestDTO;
import webeid.example.dto.FileDTO;
import webeid.example.dto.SignatureDTO;
import jakarta.xml.bind.DatatypeConverter;
import org.apache.commons.io.FilenameUtils;
import org.digidoc4j.Configuration;
import org.digidoc4j.Container;
import org.digidoc4j.ContainerBuilder;
import org.digidoc4j.DataFile;
import org.digidoc4j.DataToSign;
import org.digidoc4j.DigestAlgorithm;
import org.digidoc4j.Signature;
import org.digidoc4j.SignatureBuilder;
import org.digidoc4j.SignatureProfile;
import org.digidoc4j.utils.TokenAlgorithmSupport;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Service
public class SigningService {

    public FileDTO prepareContainer(CertificateDTO certificateDTO) throws CertificateException, NoSuchAlgorithmException, IOException {
        final X509Certificate certificate = certificateDTO.toX509Certificate();
        final FileDTO fileDTO = FileDTO.getExampleForSigningFromResources();
        final Container containerToSign = getContainerToSign(fileDTO);


        final DigestAlgorithm signatureDigestAlgorithm = TokenAlgorithmSupport.determineSignatureDigestAlgorithm(certificate);

        final DataToSign dataToSign = SignatureBuilder
                .aSignature(containerToSign)
                .withSignatureProfile(SignatureProfile.LT) // AIA OCSP is supported for signatures with LT or LTA profile.
                .withSigningCertificate(certificate)
                .withSignatureDigestAlgorithm(DigestAlgorithm.SHA256)
                .buildDataToSign();

        final byte[] digest = signatureDigestAlgorithm.getDssDigestAlgorithm().getMessageDigest()
                .digest(dataToSign.getDataToSign());

        final DigestDTO digestDTO = new DigestDTO();
        digestDTO.setHash(DatatypeConverter.printBase64Binary(digest));
        digestDTO.setHashFunction("SHA-256");

        SignatureDTO signatureDTO = new SignatureDTO();
        signatureDTO.setBase64Signature(digestDTO.getHash());
        return signContainer(signatureDTO, dataToSign, containerToSign, fileDTO);
    }


    public FileDTO signContainer(SignatureDTO signatureDTO, DataToSign dataToSign, Container containerToSign, FileDTO fileDTO) {

        byte[] signatureBytes = DatatypeConverter.parseBase64Binary(signatureDTO.getBase64Signature());
        Signature signature = dataToSign.finalize(signatureBytes);
        containerToSign.addSignature(signature);

        return new FileDTO(generateContainerName(fileDTO.getName()));
    }

    private Container getContainerToSign(FileDTO fileDTO) {

        final DataFile dataFile = new DataFile(fileDTO.getContentBytes(), fileDTO.getName(), fileDTO.getContentType());
        return ContainerBuilder
                .aContainer(Container.DocumentType.ASICE)
                .withDataFile(dataFile)
                .withConfiguration(Configuration.of(Configuration.Mode.TEST))
                .build();
    }

    private String generateContainerName(String fileName) {
        return FilenameUtils.removeExtension(fileName) + ".asice";
    }

}
