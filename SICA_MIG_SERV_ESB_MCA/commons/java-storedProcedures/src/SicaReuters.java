/*
 * $Id: SicaReuters.java,v 1.12 2008/02/22 18:25:50 ccovian Exp $
 * Ixe Grupo Financiero
 * Copyright © 2005 LegoSoft S.C.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author Jean C. Favila
 * @version $Revision: 1.12 $ $Date: 2008/02/22 18:25:50 $
 */
public class SicaReuters {

    public static void notificarCambioReuters(String host, int puerto, String path) {
        try {
            String servletURL = "http://" + host + ":" + puerto + path;
            URL url = new URL(servletURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Pragma", "no-cache");
            conn.connect();
            InputStream is = conn.getInputStream();
            int letra;
            while ((letra = is.read()) >= 0) {
                System.out.print((char) letra);

            }
            System.out.println(new Date() + " notificarCambioReuters: OK.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        notificarCambioReuters(args[0], Integer.valueOf(args[1]).intValue(), args[2]);
    }
}