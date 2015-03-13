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
 */

 /*
   * 
   * Translator.java
   * 
   * A utility class for translating strings and characters into integers 
   * using the character sheet (charsheet.bmp) as a guide.
 */
package utils;

import java.util.HashMap;
import java.util.Map;

public class Translator {
	static Map<Character, Integer> intToChar = new HashMap<>();
	
	static final int ERROR_CHARACTER_NUMBER = 63; //should put up ? 
	
	public Translator() {
            intToChar.put('\n', -1);
            intToChar.put(' ', 0);

            intToChar.put('!', 33);
            intToChar.put('"', 34); 	//double quote
            intToChar.put('#', 35);
            intToChar.put('$', 36);
            intToChar.put('%', 37);
            intToChar.put('&', 38);
            intToChar.put('\'', 39);	//single quote
            intToChar.put('(', 40);
            intToChar.put(')', 41);
            intToChar.put('*', 42);
            intToChar.put('+', 43);
            intToChar.put(',', 44);
            intToChar.put('-', 45);
            intToChar.put('.', 46);
            intToChar.put('/', 47);
            intToChar.put('0', 48);
            intToChar.put('1', 49);
            intToChar.put('2', 50);
            intToChar.put('3', 51);
            intToChar.put('4', 52);
            intToChar.put('5', 53);
            intToChar.put('6', 54);
            intToChar.put('7', 55);
            intToChar.put('8', 56);
            intToChar.put('9', 57);
            intToChar.put(':', 58);
            intToChar.put(';', 59);
            intToChar.put('<', 60);
            intToChar.put('=', 61);
            intToChar.put('>', 62);
            intToChar.put('?', 63);
            intToChar.put('@', 64);
            intToChar.put('A', 65);
            intToChar.put('B', 66);
            intToChar.put('C', 67);
            intToChar.put('D', 68);
            intToChar.put('E', 69);
            intToChar.put('F', 70);
            intToChar.put('G', 71);
            intToChar.put('H', 72);
            intToChar.put('I', 73);
            intToChar.put('J', 74);
            intToChar.put('K', 75);
            intToChar.put('L', 76);
            intToChar.put('M', 77);
            intToChar.put('N', 78);
            intToChar.put('O', 79);
            intToChar.put('P', 80);
            intToChar.put('Q', 81);
            intToChar.put('R', 82);
            intToChar.put('S', 83);
            intToChar.put('T', 84);
            intToChar.put('U', 85);
            intToChar.put('V', 86);
            intToChar.put('W', 87);
            intToChar.put('X', 88);
            intToChar.put('Y', 89);
            intToChar.put('Z', 90);
            intToChar.put('[', 91);
            intToChar.put('\\', 92);            //backslash
            intToChar.put(']', 93);
            intToChar.put('^', 94);
            intToChar.put('_', 95);
            intToChar.put('`', 96);
            intToChar.put('a', 97);
            intToChar.put('b', 98);
            intToChar.put('c', 99);
            intToChar.put('d', 100);
            intToChar.put('e', 101);
            intToChar.put('f', 102);
            intToChar.put('g', 103);
            intToChar.put('h', 104);
            intToChar.put('i', 105);
            intToChar.put('j', 106);
            intToChar.put('k', 107);
            intToChar.put('l', 108);
            intToChar.put('m', 109);
            intToChar.put('n', 110);
            intToChar.put('o', 111);
            intToChar.put('p', 112);
            intToChar.put('q', 113);
            intToChar.put('r', 114);
            intToChar.put('s', 115);
            intToChar.put('t', 116);
            intToChar.put('u', 117);
            intToChar.put('v', 118);
            intToChar.put('w', 119);
            intToChar.put('x', 120);
            intToChar.put('y', 121);
            intToChar.put('z', 122);
            intToChar.put('{', 123);
            intToChar.put('|', 124);
            intToChar.put('}', 125);
            intToChar.put('~', 125);
	}
	
	//Takes a string input and returns the numerical positions on the charsheet that correspond to the characters within that string
	//Note...returns ? if a character isn't currently entered in the dictionary. No worries though, just add it.
	public static int[] translate(String input) {            
                int[] output = new int[input.length()];
		
		for(int i = 0; i < input.length(); i++) {
			if(intToChar.containsKey(input.charAt(i))){
                            output[i] = intToChar.get(input.charAt(i));
                        }
                        else {
                            output[i] = ERROR_CHARACTER_NUMBER;
                        }
		}
		
		return output;
	}
	
	//a lighter version of translate for individual chars
	public static int translate(char input) {
		int output = ERROR_CHARACTER_NUMBER;
		
		if(intToChar.containsKey(input)) 
			output = intToChar.get(input);
				
		return output;
	}
}