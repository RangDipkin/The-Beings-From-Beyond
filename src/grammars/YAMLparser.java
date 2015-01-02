package grammars;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Travis
 */
public class YAMLparser {
    public YAMLparser() throws IOException{
        System.out.println("beginning YAML Parser...");
        Yaml yaml = new Yaml();
        InputStream is = YAMLparser.class.getResourceAsStream("testGrammar01.yaml");
        System.out.println(is);
        LinkedHashMap list = (LinkedHashMap)yaml.load(is);
        System.out.println(list);       
        is.close();
        System.out.println("YAML Parser complete!");
    }
}
