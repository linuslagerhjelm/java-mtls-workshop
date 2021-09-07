package se.omegapoint.kompetensdag.businessapplication;

import javax.security.auth.x500.X500Principal;

public record X509Fields(String email, String commonName, String organization, String locality,
                         String country) {

    public static X509Fields fromPrincipal(X500Principal principal) {
        var name = principal.getName(X500Principal.RFC1779);
        var fields = name.split(",");

        var email = "";
        var cn = "";
        var o = "";
        var l = "";
        var c = "";
        for (String field : fields) {
            var keyValue = field.split("=");
            keyValue[0] = keyValue[0].trim();
            if (keyValue[0].equals("OID.1.2.840.113549.1.9.1")) email = keyValue[1];
            else if (keyValue[0].equals("CN")) cn = keyValue[1];
            else if (keyValue[0].equals("O")) o = keyValue[1];
            else if (keyValue[0].equals("L")) l = keyValue[1];
            else if (keyValue[0].equals("C")) c = keyValue[1];
        }

        return new X509Fields(email, cn, o, l, c);
    }
}
