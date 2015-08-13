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
    
    public YAMLparser() throws IOException, YamlException {
        System.out.println("Beginning YAML parser...");
        FileHandle handle = Gdx.files.internal("grammars/testGrammar01.yml");
        
        try {
            YamlConfig config = new YamlConfig();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            classLoader.loadClass("tp.aoi.grammars.Constant");
            //System.out.println("aClass.getName() = " + aClass.getName());
            config.readConfig.setClassLoader(Thread.currentThread().getContextClassLoader());
            YamlReader reader = new YamlReader(handle.reader(), config);
            
            PlaceGrammar pg = (PlaceGrammar)reader.read(PlaceGrammar.class); 
            for(ShapeSpec shapeSpec : pg.shapeRules.get(0).output.shapeSpecs) {
                shapeSpec.parseVertices(pg.constants);
            }
            mainGrammar = pg;
            System.out.println("YAML parser complete!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found!");
        }
    }
}
