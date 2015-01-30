package proto.cederj.prownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import proto.cederj.prownloader.thirdparty.rio.FacadeRioXmlDecompiler;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.GenericPseudoHttpClient;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.PseudoHttpClient;
import proto.cederj.prownloader.thirdparty.rio.RioServerResolver;
import proto.cederj.prownloader.thirdparty.webripper.CederjWebRipper;
import proto.cederj.prownloader.thirdparty.webripper.WebRipper;
import proto.cederj.prownloader.util.Config;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        Prownloader prownloader = new Prownloader();
        prownloader.initialize();
    }
}
