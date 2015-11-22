/*
 * Copyright 2013 Travis Pressler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   * 
   * YAMLparser.java
 */
package tp.aoi.grammars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import net.sourceforge.yamlbeans.YamlConfig;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public class YAMLparser {
    //mainGrammar is accessed from within Building.createInternalRooms()
    public static PlaceGrammar mainGrammar;
    public static TopologyGrammar mainTG;
    
    public YAMLparser() throws IOException, YamlException {
        System.out.println("Parsing topology grammar...");
        mainTG = (TopologyGrammar) loadYAML(TopologyGrammar.class, "grammars/topologyGrammar02.yml");
        System.out.println("Topology grammar parsed!");
        
        System.out.println("Parsing shape grammar...");
        // mainGrammar = (PlaceGrammar) loadYAML(PlaceGrammar.class, "grammars/testGrammar01.yml");
        System.out.println("Shape grammar parsed!");
        //parseGrammar(mainGrammar);
    }
    
    public void parseGrammar(PlaceGrammar pg) {
        for(ShapeSpec shapeSpec : pg.shapeRules.get(0).output.shapeSpecs) {
            shapeSpec.parseVertices(pg.constants);
        }
    }
    
    public Object loadYAML(Class daClass, String URL) throws YamlException {
        FileHandle handle = Gdx.files.internal(URL);
        
        YamlConfig config = new YamlConfig();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        config.readConfig.setClassLoader(Thread.currentThread().getContextClassLoader());
        YamlReader reader = new YamlReader(handle.reader(), config);
        return reader.read(daClass);
    }
}
