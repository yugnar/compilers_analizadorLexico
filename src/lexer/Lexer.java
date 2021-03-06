package lexer;

import java.io.*;
import java.util.*;

public class Lexer {
    public int line = 1;
    private char peek = ' ';

    private Hashtable words = new Hashtable();

    void reserve(Word t) {
        words.put(t.lexeme, t);
    }

    public Lexer() {
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));
    }

    public Token scan() throws IOException {

        /* salta espacios en blanco */
        for (;; peek = (char) System.in.read()) {
            if (peek == ' ' || peek == '\t') {
                continue;
            }
            else if (peek == '\n')
                line = line + 1;
            else
                break;
        }

        //Ignorar comentarios
        if (Character.hashCode(peek) == 47) {
            boolean flag = true;
            do {
                peek = (char) System.in.read();
                if(Character.hashCode(peek) == 47) {
                    boolean flag0 = true;
                    do {
                        peek = (char) System.in.read();
                        if(Character.hashCode(peek) == 10) {
                            peek = (char) System.in.read();
                            flag0=false;
                            flag=false;
                        }
                    } while (flag0);
                } else if (Character.hashCode(peek) == 42) {
                    boolean flag1 = true;
                    do {
                        peek = (char) System.in.read();
                        if(Character.hashCode(peek) == 42) {
                            peek = (char) System.in.read();
                            if(Character.hashCode(peek) == 47) {
                                peek = (char) System.in.read();
                                flag = false;
                                flag1 = false;
                            }
                        }
                    } while (flag1);
                }
            } while (flag);
        }
        /* Para generar un entero */
        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                peek = (char) System.in.read();
            } while (Character.isDigit(peek));

            if(peek == '.'){
                peek = (char) System.in.read();
                if(Character.isDigit(peek)){
                    int v2 = 0;
                    do {
                        v2 = 10 * v2 + Character.digit(peek, 10);
                        peek = (char) System.in.read();
                    } while (Character.isDigit(peek));
                    String vTemp = Integer.toString(v) + "." + Integer.toString(v2);
                    return new DecimalNum(Double.parseDouble(vTemp));
                }
            }
            return new Num(v);
        }

        /* Para generar comentario en línea, el de bloque por separado */

        /* Para generar un identificador */
        if (Character.isLetter(peek)) {
            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);
                peek = (char) System.in.read();
            } while (Character.isLetterOrDigit(peek));
            String s = b.toString();
            Word w = (Word) words.get(s);
            if (w != null)
                return w;
            w = new Word(Tag.ID, s);
            words.put(s, w);
            return w;
        }

        /* Para extender los operadores relacionales  <, <=, ==, !=, >=, >           */
        String op = "";
        int opId = 0;
        if (isSymbol(peek)){
            int counter = 0;
            do{
                op += String.valueOf(peek);
                peek = (char) System.in.read();
                counter++;
            }while(counter < 2 && isSymbol(peek));
            switch (op) {
                case "<":
                    opId = Tag.LT;
                    break;
                case "<=":
                    opId = Tag.LE;
                    break;
                case "==":
                    opId = Tag.EQ;
                    break;
                case "!=":
                    opId = Tag.NE;
                    break;
                case ">=":
                    opId = Tag.GE;
                    break;
                case ">":
                    opId = Tag.GT;
                    break;
                default:
            }
            return new RelationalOP(opId ,op);
        }

        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    public boolean isSymbol(char c){
        if(c == '<' || c == '=' || c == '!' || c == '>'){
            return true;
        }
        else{
            return false;
        }
    }
}
