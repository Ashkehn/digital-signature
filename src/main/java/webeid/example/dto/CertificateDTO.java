
package webeid.example.dto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateDTO {

    private String certificate;

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
    public X509Certificate toX509Certificate() throws CertificateException {
        byte[] certificateBytes = Base64.getDecoder().decode(certificate);
        InputStream inStream = new ByteArrayInputStream(certificateBytes);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(inStream);
    }

}
