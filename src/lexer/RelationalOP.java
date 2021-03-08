package lexer;
public class RelationalOP extends Token {
    public final String opSymbol;
    public RelationalOP(int t, String s) {
        super(t); opSymbol = new String(s);
    }

    public String toString() {
        return super.toString() + "(" + opSymbol + ")";
    }
}
