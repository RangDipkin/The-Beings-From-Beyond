/*
 * Copyright 2015 Travis Pressler

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ParsedTopologyRule.java
 */
package tp.aoi.grammars;

import java.util.HashSet;
import tp.aoi.topology.AnnotatedEdge;
import tp.aoi.topology.AnnotatedNode;
import tp.aoi.topology.TopologicalElement;

public class ParsedTopologyRule {
    public HashSet<TopologicalElement> parsedLabels;
    public HashSet<TopologicalElement> parsedOutput;
    
    public ParsedTopologyRule() {}
    
    public ParsedTopologyRule(TopologyRule unparsed) {
        for(String label : unparsed.labels) {
            parsedLabels.add(parse(label));
        }
        for(String output : unparsed.output) {
            parsedOutput.add(parse(output));
        }
    }
    
    static public TopologicalElement parse(String label) {
        if(label.contains("-")) {
            String[] halves = label.split("-");
            return new AnnotatedEdge(parseNode(halves[0]),parseNode(halves[1]));
        } else {
            return parseNode(label);
        }
    }
    
    static AnnotatedNode parseNode(String label) {
        String[] splitLabel = label.split("_");
        return new AnnotatedNode(splitLabel[0], splitLabel[1]);
    }
}
