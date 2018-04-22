/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import model.Users;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 * @author mlei
 */
public class DigiSigner {

    private Users u;
    public String SignName;
    public String SignSignature;    // resource path

    public FileInputStream getSignature() throws FileNotFoundException {
        return new FileInputStream(SignSignature);
    }

    public DigiSigner(Users u) {
        this.u = u;
    }

    public Users getUsers() {
        return u;
    }

    public void setUsers(Users u) {
        this.u = u;
    }

    public String sign(String docContent) throws FileNotFoundException {
        System.out.println("found DATE? ");
        System.out.println(docContent.contains(DATE));
        docContent = docContent.replaceAll(DATE, DocDateUtil.getSignDate());
        System.out.println("found SIGNNAME? ");
        System.out.println(docContent.contains(SIGNNAME));
        docContent = docContent.replaceAll(SIGNNAME, u.userName);
        System.out.println("found SIGNSIGNATURE? ");
        System.out.println(docContent.contains(SIGNSIGNATURE));
        if (u.signatureReady) {
            getSignature();
        } else {
            docContent = docContent.replaceAll(SIGNSIGNATURE, u.userName);
        }
        System.out.println("found SIGNDEPARTMENT? ");
        System.out.println(docContent.contains(SIGNDEPARTMENT));
        docContent = docContent.replaceAll(SIGNDEPARTMENT, u.userDepartment);
        return docContent;
    }
    public static final String SIGNNAME = "SIGNNAME";
    public static final String SIGNSIGNATURE = "SIGNSIGNATURE";
    public static final String SIGNDEPARTMENT = "SIGNDEPARTMENT";
    public static final String DATE = "DATE";
}
